package online.ilyin.usecase

import online.ilyin.dao.ScheduleRepository
import online.ilyin.dto.ScheduleView
import online.ilyin.dto.SchedulesView
import ru.tinkoff.kora.common.Component

@Component
class SchedulesViewUseCase(
        private val scheduleRepository: ScheduleRepository
) {
    fun findAll(): SchedulesView {
        val schedules = scheduleRepository.findAll().map {
            ScheduleView(
                    id = it.id,
                    title =  it.title,
                    action = it.action,
            )
        }
        return SchedulesView(schedules)
    }
}