package online.ilyin.status.check.job

import online.ilyin.status.check.use.case.NetworkStateWatchdogUseCase
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.TaskScheduler

@Configuration
class CheckJobsConfiguration