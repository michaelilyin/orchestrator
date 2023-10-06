package framework

import (
	"fmt"
	"github.com/gorilla/mux"
	"log"
	"math"
	"net/http"
	"os"
)

type HttpModule struct {
	Router *mux.Router
}

func (m *HttpModule) startHttpServer() {
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("Defaulting to port %s", port)
	}

	log.Printf("Listening on port %s", port)
	log.Printf("Open http://localhost:%s in the browser", port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%s", port), m.Router))
}

func (m *HttpModule) StartupOrder() int {
	return math.MaxInt
}

func (m *HttpModule) Start() {
	m.startHttpServer()
}

func InitHttpModule(app *Application) *HttpModule {
	router := mux.NewRouter()
	module := &HttpModule{
		router,
	}
	app.AddModule("http", module)
	return module
}
