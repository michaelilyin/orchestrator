package view

import (
	"github.com/gorilla/mux"
	"github.com/michaelilyin/orchestartor/modules/connectivity-check/support"
	"io"
	"log"
	"net/http"
	"os"
)

func LogViewRoute(router *mux.Router) {
	router.Path("/api/log").Methods("GET").HandlerFunc(logRouteHandler())

}

func logRouteHandler() func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		file, err := os.Open(support.LogFileLocation)
		if err != nil {
			log.Fatal(err)
		}
		_, err = io.Copy(w, file)
		if err != nil {
			log.Fatal(err)
		}
	}
}
