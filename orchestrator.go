package main

import (
	"fmt"
	probing "github.com/prometheus-community/pro-bing"
	"log"
	"net/http"
	"os"
	"time"
)

func main() {
	go heartBeat()

	http.HandleFunc("/", indexHandler)
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
		log.Printf("Defaulting to port %s", port)
	}

	log.Printf("Listening on port %s", port)
	log.Printf("Open http://localhost:%s in the browser", port)
	log.Fatal(http.ListenAndServe(fmt.Sprintf(":%s", port), nil))
}

func checkConnectivity(address string) {
	pinger, err := probing.NewPinger(address)
	if err != nil {
		log.Fatal(err)
	}
	pinger.Count = 3
	err = pinger.Run() // Blocks until finished.
	if err != nil {
		log.Fatal(err)
	}
	stats := pinger.Statistics() // get send/receive/duplicate/rtt stats
	log.Printf("Stats for %s: sent=%d, recv=%d, loss=%f, rtt=%s",
		address, stats.PacketsSent, stats.PacketsRecv, stats.PacketLoss, stats.AvgRtt)
}

func heartBeat() {
	for range time.Tick(time.Minute * 5) {
		log.Printf("Checking network connectivity")

		checkConnectivity("77.88.8.8")
		checkConnectivity("8.8.8.8")
		checkConnectivity("192.168.1.1")
		checkConnectivity("192.168.1.64")

		log.Printf("Check completed")
	}
}

func indexHandler(w http.ResponseWriter, r *http.Request) {
	if r.URL.Path != "/" {
		http.NotFound(w, r)
		return
	}
	_, err := fmt.Fprint(w, "Hello, World!")
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
	}
}
