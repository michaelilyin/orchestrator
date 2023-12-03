package online.ilyin.migration

import liquibase.Liquibase
import liquibase.database.Database
import liquibase.database.DatabaseConnection
import liquibase.database.core.PostgresDatabase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import liquibase.resource.ResourceAccessor
import liquibase.util.LiquibaseUtil
import ru.tinkoff.kora.application.graph.GraphInterceptor
import ru.tinkoff.kora.database.jdbc.JdbcDatabase
import java.sql.Connection

class LiquibaseInterceptor : GraphInterceptor<JdbcDatabase> {
    override fun init(value: JdbcDatabase): JdbcDatabase {
        value.withConnection { conn: Connection ->
            val database = JdbcConnection(conn)
            val liquibase = Liquibase("db/changelog.yml", ClassLoaderResourceAccessor(), database)
            liquibase.changeLogSync("prod")
        }
        return value
    }

    override fun release(value: JdbcDatabase): JdbcDatabase = value
}