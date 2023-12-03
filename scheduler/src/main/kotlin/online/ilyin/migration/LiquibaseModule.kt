package online.ilyin.migration

interface LiquibaseModule {
    fun liquibaseInterceptor(): LiquibaseInterceptor = LiquibaseInterceptor()
}