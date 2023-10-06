package connectivity_check

import (
	"fmt"
	"github.com/amoghe/distillog"
	"github.com/michaelilyin/orchestartor/modules/connectivity-check/support"
	"github.com/michaelilyin/orchestartor/modules/connectivity-check/view"
	"github.com/michaelilyin/orchestartor/modules/framework"
	"github.com/michaelilyin/orchestartor/modules/reboot"
	"github.com/natefinch/lumberjack"
	probing "github.com/prometheus-community/pro-bing"
	"gopkg.in/yaml.v3"
	"io"
	"log"
	"os"
	"time"
)

type Path struct {
	Address string
}

type Action struct {
	OnRepeat string `yaml:"on_repeat"`
}

func (a *Action) String() string {
	return fmt.Sprintf("Act{on_repeat:%s}", a.OnRepeat)
}

type Group struct {
	Action   string
	ActionIf string `yaml:"action_if"`
	Type     string
	Paths    []Path
}

func (g *Group) String() string {
	return fmt.Sprintf("Group{act:%s, if:%s}", g.Action, g.ActionIf)
}

type ConnectivityCheckConfig struct {
	Actions map[string]Action
	Groups  map[string]Group
}

func (c *ConnectivityCheckConfig) String() string {
	return fmt.Sprintf("config{actions:%s, groups:%s}", c.Actions, c.Groups)
}

type groupState struct {
	LastTriggered  time.Time `yaml:"last_triggered"`
	TimesTriggered int       `yaml:"times_triggered"`
}

func (g *groupState) String() string {
	return fmt.Sprintf("last:%d, times:%d", g.LastTriggered.Unix(), g.TimesTriggered)
}

type Module struct {
	states map[string]groupState
	logger distillog.Logger
}

func (m *Module) StartupOrder() int {
	return 0
}

func (m *Module) Start() {
	m.logger.Infof(`
======================================
Starting check with current state %v
======================================`,
		m.states)
	go m.heartBeatLoop()
}

func InitConnectivityCheckModule(app *framework.Application, http *framework.HttpModule) *Module {
	view.LogViewRoute(http.Router)

	states := readState()

	module := &Module{
		states: states,
		logger: initLogging(),
	}
	app.AddModule("connectivity-check", module)
	return module
}

func (m *Module) checkConnectivity(address string) bool {
	pinger, err := probing.NewPinger(address)
	if err != nil {
		m.logger.Errorf("Cannot obtain new pinger, %s", err)
		return false
	}
	timeout, err := time.ParseDuration("1m")
	if err != nil {
		m.logger.Errorf("Cannot parse duration, %s", err)
		return false
	}
	pinger.SetPrivileged(true)
	pinger.Count = 4
	pinger.Timeout = timeout
	err = pinger.Run() // Blocks until finished.
	if err != nil {
		m.logger.Errorf("Cannot run ping, %s", err)
		return false
	}
	stats := pinger.Statistics() // get send/receive/duplicate/rtt stats
	m.logger.Infof("Stats for %s: sent=%d, recv=%d, loss=%f, rtt=%s",
		address, stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss, stats.AvgRtt)
	return stats.PacketsRecv > 2
}

func (m *Module) heartBeatLoop() {
	for range time.Tick(time.Minute * 15) {
		m.heartBeat()
	}
}

func (m *Module) heartBeat() {
	config := readConfig()
	m.logger.Infof("=================== START ===================")
	for key, group := range config.Groups {
		m.logger.Infof("----------------- %s -----------------", key)

		anySuccess := false
		allSuccess := true

		for _, val := range group.Paths {
			res := m.checkConnectivity(val.Address)
			anySuccess = anySuccess || res
			allSuccess = allSuccess && res
		}

		m.logger.Infof("Any=%t, All=%t", anySuccess, allSuccess)
		var needRunAction bool
		switch group.ActionIf {
		case "all":
			needRunAction = !anySuccess
			break
		case "any":
			needRunAction = anySuccess && !allSuccess
			break
		default:
			needRunAction = false
			m.logger.Warningf("Unknown condition %s", group.ActionIf)
			break
		}
		m.logger.Infof("Need run action: %t", needRunAction)
		if needRunAction {
			action, found := config.Actions[group.Action]
			if found {
				m.logger.Infof("Run action if needed %s", group.Action)
				m.runActionIfNeeded(key, group.Action, action)
			}
		} else {
			m.cleanupRunStatusIfNeeded(key)
		}

		m.logger.Infof("----------------------------------------\n")
	}
	m.logger.Infof("=================== COMPLETE ===================\n\n")
}

func (m *Module) runActionIfNeeded(groupName string, actionName string, action Action) {
	state, exists := m.states[groupName]
	if !exists || m.shouldRun(state, action) {
		m.raiseRunStatusIfNeeded(groupName)
		m.runAction(actionName)
	}
}

func (m *Module) shouldRun(state groupState, action Action) bool {
	switch action.OnRepeat {
	case "none":
		return false
	case "fibonacci":
		// todo make real fibonacci
		if state.TimesTriggered < 3 {
			return true
		} else {
			return false
		}
	default:
		m.logger.Warningf("Unknown repeat action %s", action.OnRepeat)
		return false
	}
}

func (m *Module) runAction(actionName string) {
	switch actionName {
	case "reboot":
		m.runRebootAction()
		break
	default:
		m.logger.Warningf("Unknown action %s", actionName)
	}
}

func (m *Module) raiseRunStatusIfNeeded(groupName string) {
	existing, present := m.states[groupName]
	if !present {
		m.states[groupName] = groupState{
			LastTriggered:  time.Now(),
			TimesTriggered: 1,
		}
	} else {
		m.states[groupName] = groupState{
			LastTriggered:  time.Now(),
			TimesTriggered: existing.TimesTriggered + 1,
		}
	}
	writeState(&m.states)
}

func (m *Module) cleanupRunStatusIfNeeded(groupName string) {
	delete(m.states, groupName)
	writeState(&m.states)
}

func (m *Module) runRebootAction() {
	err := m.logger.Close()
	if err != nil {
		log.Fatal(err)
	}
	duration, err := time.ParseDuration("10s")
	if err != nil {
		panic(err)
	}
	time.Sleep(duration)
	reboot.Reboot()
}

func readConfig() ConnectivityCheckConfig {
	res, err := os.ReadFile("/app/config/connectivity-check-config.yml")
	if err != nil {
		panic(err)
	}
	config := ConnectivityCheckConfig{}
	err = yaml.Unmarshal(res, &config)
	if err != nil {
		panic(err)
	}
	log.Printf("Got config %s", config)
	return config
}

func readState() map[string]groupState {
	res, err := os.ReadFile("/app/state/state.yml")
	if err != nil {
		return make(map[string]groupState)
	}

	state := make(map[string]groupState)
	err = yaml.Unmarshal(res, &state)
	if err != nil {
		panic(err)
	}
	return state
}

func writeState(state *map[string]groupState) {
	res, err := yaml.Marshal(state)
	if err != nil {
		panic(err)
	}
	err = os.WriteFile("/app/state/state.yml", res, 0644)
	if err != nil {
		panic(err)
	}
}

type combinedLogger struct {
	file    io.WriteCloser
	console io.Writer
}

func (c *combinedLogger) Write(p []byte) (n int, err error) {
	_, cErr := c.console.Write(p)
	if cErr != nil {
		panic(cErr)
	}
	fRes, fErr := c.file.Write(p)
	return fRes, fErr
}

func (c *combinedLogger) Close() error {
	return c.file.Close()
}

func makeCombinedLogger() io.WriteCloser {
	fileHandle := &lumberjack.Logger{
		Filename:   support.LogFileLocation,
		MaxSize:    25,
		MaxBackups: 5,
		MaxAge:     30,
	}
	return &combinedLogger{
		file:    fileHandle,
		console: log.Writer(),
	}
}

func initLogging() distillog.Logger {
	stream := makeCombinedLogger()
	logger := distillog.NewStreamLogger("connectivity", stream)
	distillog.SetOutput(stream)
	return logger
}
