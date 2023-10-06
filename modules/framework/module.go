package framework

type AppModule interface {
	StartupOrder() int
	Start()
}
