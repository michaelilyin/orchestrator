package online.ilyin.status.check.controller

import online.ilyin.status.check.controller.model.NetworkStatusView
import online.ilyin.status.check.controller.model.NetworkTypeStatusView
import online.ilyin.status.check.service.NetworkStateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/network/status")
class StatusCheckController(
  private val networkStateService: NetworkStateService
) {
  @GetMapping
  suspend fun getNetworkStatus(): NetworkStatusView {
    val states = networkStateService.getAllStates()
    return NetworkStatusView(
      networks = states.map {
        NetworkTypeStatusView(it.type, it.status)
      }
    )
  }
}
