package online.ilyin.status.check.use.case

import kotlinx.coroutines.runBlocking
import online.ilyin.status.check.model.NetworkStatusChangeAction
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.service.ActionsRunner
import online.ilyin.status.check.service.StateChecker
import online.ilyin.status.check.service.CheckStateService
import online.ilyin.status.check.support.logging.error
import online.ilyin.status.check.support.logging.info
import online.ilyin.status.check.support.logging.logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.context.ApplicationListener
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

private val log = logger { }

@ConfigurationProperties("check-state.watchdog")
data class ChecksWatchdogProperties(
    @NestedConfigurationProperty
    val types: Map<CheckType, CheckWatchdogProperties>
)

data class CheckWatchdogProperties(
    val interval: Duration,
    val actions: Map<CheckStatus, StatusChangeAction>
)

data class StatusChangeAction(
    val action: NetworkStatusChangeAction
)

@Component
class StateWatchdogUseCase(
  private val executor: TaskScheduler,
  private val stateChecker: StateChecker,
  private val properties: ChecksWatchdogProperties,
  private val checkStateService: CheckStateService,
  private val actionsRunner: ActionsRunner
) : ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        checkNetworkState(CheckType.NETWORK_LOCAL)
    }

    private fun checkNetworkState(type: CheckType) {
        log.info { "Start network state check for $type" }
        try {
            val properties = this.properties.types[type]
                ?: throw IllegalStateException("Network $type does not have watchdog properties!")

            runBlocking {
                val previous = checkStateService.getStoredStatus(type)
                log.info { "Previous status of $type was $previous" }

                val result = stateChecker.checkNetworkState(type)

                checkStateService.saveCheckResult(type, result)

                if (previous != result.status) {
                    log.info { "Status of $type has changed from $previous to ${result.status}" }
                    val action = properties.actions[result.status]
                    if (action != null) {
                        actionsRunner.runAction(action.action)
                    }
                }
            }

            val nextCheckAt = Instant.now().plus(properties.interval)
            log.info { "Schedule next checking for $type at $nextCheckAt" }
            executor.schedule({ checkNetworkState(type) }, nextCheckAt)

        } catch (e: Exception) {
            log.error(e) { "Failed to complete network state check for $type" }
        }
    }
}
