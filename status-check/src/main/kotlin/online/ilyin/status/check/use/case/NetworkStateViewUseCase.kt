package online.ilyin.status.check.use.case

import kotlinx.coroutines.flow.toList
import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.service.NetworkCheckLogService
import online.ilyin.status.check.service.NetworkStateService
import online.ilyin.status.check.use.case.model.NetworkStatusLogEntryView
import online.ilyin.status.check.use.case.model.NetworkStatusLogView
import online.ilyin.status.check.use.case.model.NetworkStatusView
import online.ilyin.status.check.use.case.model.NetworkTypeStatusView
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class NetworkStateViewUseCase(
  private val networkStateService: NetworkStateService,
  private val networkCheckLogService: NetworkCheckLogService
) {
  suspend fun getAllNetworksState(): NetworkStatusView {
    val states = networkStateService.getAllStates()
    return NetworkStatusView(
      networks = states.map {
        NetworkTypeStatusView(it.type, it.status)
      }
    )
  }

  suspend fun getNetworkStatusCheckLog(
    networkType: NetworkType,
    status: NetworkStateStatus?,
    before: Instant,
    max: Int
  ): NetworkStatusLogView {
    val logs = networkCheckLogService.getChecksForNetwork(networkType, status, before, max).toList()
    return NetworkStatusLogView(
      checks = logs.map {
        NetworkStatusLogEntryView(it.id, it.createdAt, it.status)
      }
    )
  }
}
