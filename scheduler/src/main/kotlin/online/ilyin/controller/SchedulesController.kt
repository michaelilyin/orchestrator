package online.ilyin.controller

import online.ilyin.dto.ScheduleView
import online.ilyin.dto.SchedulesView
import online.ilyin.usecase.SchedulesViewUseCase
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.http.common.HttpMethod
import ru.tinkoff.kora.http.common.annotation.HttpRoute
import ru.tinkoff.kora.http.server.common.annotation.HttpController
import ru.tinkoff.kora.json.common.annotation.Json

@HttpController
class SchedulesController(
        private val schedulesViewUseCase: SchedulesViewUseCase
) {
    @Json
    @HttpRoute(method = HttpMethod.GET, path = "schedules")
    fun getAllSchedules(): SchedulesView {
        return schedulesViewUseCase.findAll()
    }
}