package online.ilyin.status.check.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.ilyin.status.check.entity.NetworkPingData
import online.ilyin.status.check.model.CheckResult
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.support.logging.error
import online.ilyin.status.check.support.logging.info
import online.ilyin.status.check.support.logging.logger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.stereotype.Service
import java.io.IOException
import java.net.InetAddress
import java.time.Duration
import kotlin.time.measureTimedValue
import kotlin.time.toJavaDuration

private val logger = logger { }

@ConfigurationProperties("network-state.check")
data class NetworkStateCheckProperties(
  @NestedConfigurationProperty
  val types: Map<CheckType, NetworkCheckProperties>
)

data class NetworkCheckProperties(
  val host: InetAddress,
  val timeout: Duration
)

@Service
class StateChecker(
  val properties: NetworkStateCheckProperties
) {
  suspend fun checkNetworkState(checkType: CheckType): CheckResult {
    logger.info { "Check network state for $checkType" }
    val properties = this.properties.types[checkType]
      ?: throw IllegalStateException("Network type $checkType does not have check properties!")

    val isReachable = measureTimedValue {
      withContext(Dispatchers.IO) {
        try {
          properties.host.isReachable(properties.timeout.toMillis().toInt())
        } catch (e: IOException) {
          logger.error(e) { "Failed to check network state for ${properties.host}" }
          false
        }
      }
    }

    val data = NetworkPingData(
      host = properties.host,
      duration = isReachable.duration.toJavaDuration()
    )

    val res = if (isReachable.value) {
      CheckStatus.OK
    } else {
      CheckStatus.FAILED
    }
    logger.info { "Check network state has completed for $checkType, ${properties.host} with result: $res" }
    return CheckResult(res, data)
  }
}
