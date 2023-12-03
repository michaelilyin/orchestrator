package online.ilyin

import online.ilyin.migration.LiquibaseModule
import ru.tinkoff.kora.application.graph.KoraApplication
import ru.tinkoff.kora.common.KoraApp
import ru.tinkoff.kora.config.hocon.HoconConfigModule
import ru.tinkoff.kora.database.jdbc.JdbcDatabaseModule
import ru.tinkoff.kora.http.client.common.HttpClientModule
import ru.tinkoff.kora.http.server.undertow.UndertowHttpServerModule
import ru.tinkoff.kora.json.module.JsonModule
import ru.tinkoff.kora.logging.logback.LogbackModule
import ru.tinkoff.kora.micrometer.module.MetricsModule


@KoraApp
interface SchedulerApplication :
        HoconConfigModule,
        MetricsModule,
        UndertowHttpServerModule,
        JsonModule,
        HttpClientModule,
        LiquibaseModule,
        JdbcDatabaseModule,
        LogbackModule {
}

fun main(args: Array<String>) {
    KoraApplication.run(SchedulerApplicationGraph::graph)
}