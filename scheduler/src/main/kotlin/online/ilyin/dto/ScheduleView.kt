package online.ilyin.dto

import ru.tinkoff.kora.json.common.annotation.Json

data class SchedulesView(
        val schedules: List<ScheduleView>
)

data class ScheduleView(
        val id: Long,
        val title: String,
        val action: String
)