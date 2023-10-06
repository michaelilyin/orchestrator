package view

import (
	"github.com/gorilla/mux"
	"html/template"
	"log"
	"net/http"
)

type Index struct {
	Hello string
}

func RootViewRoute(router *mux.Router) {
	tmpl := template.Must(template.ParseFiles("templates/index.html", "templates/base/page.html"))
	router.Path("/").Methods("GET").HandlerFunc(indexRouteHandler(tmpl))

}

func indexRouteHandler(tmpl *template.Template) func(w http.ResponseWriter, r *http.Request) {
	return func(w http.ResponseWriter, r *http.Request) {
		er := tmpl.ExecuteTemplate(w, "page", Index{
			Hello: "test",
		})
		if er != nil {
			log.Fatal(er)
		}
	}
}
