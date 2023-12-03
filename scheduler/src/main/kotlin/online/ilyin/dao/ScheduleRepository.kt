package online.ilyin.dao

import online.ilyin.model.Schedule
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository

@Repository
interface ScheduleRepository: JdbcRepository {
    @Query("""
        SELECT * FROM schedules;
    """)
    fun findAll(): List<Schedule>
}