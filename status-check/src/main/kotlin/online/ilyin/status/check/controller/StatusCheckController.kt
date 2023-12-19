package online.ilyin.status.check.controller

import online.ilyin.status.check.entity.CheckLogData
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.use.case.CheckStateViewUseCase
import online.ilyin.status.check.use.case.model.CheckLogView
import online.ilyin.status.check.use.case.model.ChecksStatusView
import org.springframework.web.bind.annotation.*
import java.time.Instant

@RestController
@RequestMapping("/api/checks")
class StatusCheckController(
  private val checkStateViewUseCase: CheckStateViewUseCase
) {
  @GetMapping("/status")
  suspend fun getNetworkStatus(): ChecksStatusView = checkStateViewUseCase.getAllChecksState()

  @GetMapping("{checkType}/log")
  suspend fun getCheckLog(
    @PathVariable checkType: CheckType,
    @RequestParam(required = true) before: Instant,
    @RequestParam(required = true) max: Int,
    @RequestParam(required = false) status: CheckStatus?
  ): CheckLogView = checkStateViewUseCase.getCheckLog(checkType, status, before, max)

  @GetMapping("/log/{logId}/details")
  suspend fun getCheckLogDetails(
    @PathVariable logId: Long
  ): CheckLogData? = checkStateViewUseCase.getCheckLogDetails(logId)
}
