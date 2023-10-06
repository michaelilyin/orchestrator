package main

import (
	connectivity_check "github.com/michaelilyin/orchestartor/modules/connectivity-check"
	"github.com/michaelilyin/orchestartor/modules/framework"
	"github.com/michaelilyin/orchestartor/modules/index"
	"github.com/michaelilyin/orchestartor/modules/static"
)

func main() {
	app := framework.CreateApplication()

	httpModule := framework.InitHttpModule(app)

	static.InitStaticModule(app, httpModule)
	index.InitIndexModule(app, httpModule)
	connectivity_check.InitConnectivityCheckModule(app, httpModule)

	app.Start()
}
