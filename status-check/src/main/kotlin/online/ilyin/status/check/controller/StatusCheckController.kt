package online.ilyin.status.check.controller

import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.use.case.NetworkStateViewUseCase
import online.ilyin.status.check.use.case.model.NetworkStatusLogView
import online.ilyin.status.check.use.case.model.NetworkStatusView
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/networks")
class StatusCheckController(
  private val networkStateViewUseCase: NetworkStateViewUseCase
) {
  @GetMapping("/status")
  suspend fun getNetworkStatus(): NetworkStatusView = networkStateViewUseCase.getAllNetworksState()

  @GetMapping("{networkType}/log")
  suspend fun getNetworkCheckLog(
    @PathVariable networkType: NetworkType,
    @RequestParam(required = true) before: Instant,
    @RequestParam(required = true) max: Int,
    @RequestParam(required = false) status: NetworkStateStatus?
  ): NetworkStatusLogView = networkStateViewUseCase.getNetworkStatusCheckLog(networkType, status, before, max)
}
