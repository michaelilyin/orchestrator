package online.ilyin.status.check.use.case

import kotlinx.coroutines.runBlocking
import online.ilyin.status.check.model.NetworkStatusChangeAction
import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.service.ActionsRunner
import online.ilyin.status.check.service.NetworkStateChecker
import online.ilyin.status.check.service.NetworkStateService
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

@ConfigurationProperties("network-state.watchdog")
data class NetworkStateWatchdogProperties(
    @NestedConfigurationProperty
    val types: Map<NetworkType, NetworkWatchdogProperties>
)

data class NetworkWatchdogProperties(
    val interval: Duration,
    val actions: Map<NetworkStateStatus, StatusChangeAction>
)

data class StatusChangeAction(
    val action: NetworkStatusChangeAction
)

@Component
class NetworkStateWatchdogUseCase(
    private val executor: TaskScheduler,
    private val networkStateChecker: NetworkStateChecker,
    private val properties: NetworkStateWatchdogProperties,
    private val networkStateService: NetworkStateService,
    private val actionsRunner: ActionsRunner
) : ApplicationListener<ApplicationReadyEvent> {
    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        checkNetworkState(NetworkType.LOCAL)
    }

    private fun checkNetworkState(type: NetworkType) {
        log.info { "Start network state check for $type" }
        try {
            val properties = this.properties.types[type]
                ?: throw IllegalStateException("Network $type does not have watchdog properties!")

            runBlocking {
                val previous = networkStateService.getStoredStatus(type)
                log.info { "Previous status of $type was $previous" }

                val status = networkStateChecker.checkNetworkState(type)

                networkStateService.saveCheckResult(type, status)

                if (previous != status) {
                    log.info { "Status of $type has changed from $previous to $status" }
                    val action = properties.actions[status]
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