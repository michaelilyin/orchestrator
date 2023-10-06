package index

import (
	"github.com/michaelilyin/orchestartor/modules/framework"
	"github.com/michaelilyin/orchestartor/modules/index/view"
)

type Module struct {
}

func (m *Module) StartupOrder() int {
	return 0
}

func (m *Module) Start() {
}

func InitIndexModule(app *framework.Application, httpModule *framework.HttpModule) *Module {
	view.RootViewRoute(httpModule.Router)
	module := &Module{}
	app.AddModule("index", module)
	return module
}
