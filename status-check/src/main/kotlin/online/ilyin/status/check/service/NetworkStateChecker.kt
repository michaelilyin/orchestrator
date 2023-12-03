package online.ilyin.status.check.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.support.logging.error
import online.ilyin.status.check.support.logging.info
import online.ilyin.status.check.support.logging.logger
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.net.InetAddress
import java.time.Duration
import java.util.*
import kotlin.time.DurationUnit

private val logger = logger { }

@ConfigurationProperties("network-state.check")
data class NetworkStateCheckProperties(
    @NestedConfigurationProperty
    val types: Map<NetworkType, NetworkCheckProperties>
)

data class NetworkCheckProperties(
    val host: InetAddress,
    val timeout: Duration
)

@Service
class NetworkStateChecker(
    val properties: NetworkStateCheckProperties
) {
    suspend fun checkNetworkState(networkType: NetworkType): NetworkStateStatus {
        logger.info { "Check network state for $networkType" }
        val properties = this.properties.types[networkType]
            ?: throw IllegalStateException("Network type $networkType does not have check properties!")

        val isReachable = withContext(Dispatchers.IO) {
            try {
                properties.host.isReachable(properties.timeout.toMillis().toInt())
            } catch (e: IOException) {
                logger.error(e) { "Failed to check network state for ${properties.host}" }
                false
            }
        }

        val res = if (isReachable) {
            NetworkStateStatus.OK
        } else {
            NetworkStateStatus.FAILED
        }
        logger.info { "Check network state has completed for $networkType, ${properties.host} with result: $res" }
        return res
    }
}