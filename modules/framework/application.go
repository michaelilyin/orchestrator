package framework

import (
	"log"
	"sort"
)

type Application struct {
	modules map[string]AppModule
}

func CreateApplication() *Application {
	return &Application{
		modules: make(map[string]AppModule),
	}
}

func (app *Application) AddModule(name string, module AppModule) {
	app.modules[name] = module
}

type ModuleDef struct {
	name   string
	module AppModule
}

func (app *Application) Start() {
	var modules []ModuleDef
	for name, module := range app.modules {
		modules = append(modules, ModuleDef{
			name:   name,
			module: module,
		})
	}
	sort.Slice(modules, func(i, j int) bool {
		return modules[i].module.StartupOrder() < modules[j].module.StartupOrder()
	})
	for _, module := range modules {
		log.Printf("Starting module [%s]", module.name)
		module.module.Start()
	}
}
