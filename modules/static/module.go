package static

import (
	"github.com/michaelilyin/orchestartor/modules/framework"
	"net/http"
)

type Module struct {
}

func (m *Module) StartupOrder() int {
	return 0
}

func (m *Module) Start() {

}

func InitStaticModule(app *framework.Application, httpModule *framework.HttpModule) *Module {
	static := http.FileServer(http.Dir("static/"))
	router := httpModule.Router

	router.PathPrefix("/static").Handler(http.StripPrefix("/static/", cacheControlWrapper(static)))

	module := &Module{}
	app.AddModule("static", module)
	return module
}

func cacheControlWrapper(h http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		w.Header().Set("Cache-Control", "max-age=0")
		h.ServeHTTP(w, r)
	})
}
