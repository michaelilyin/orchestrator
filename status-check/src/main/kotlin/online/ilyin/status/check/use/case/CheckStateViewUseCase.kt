package online.ilyin.status.check.use.case

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import online.ilyin.status.check.entity.CheckLogData
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.repository.CheckLogDetailsRepository
import online.ilyin.status.check.service.CheckLogService
import online.ilyin.status.check.service.CheckStateService
import online.ilyin.status.check.use.case.model.CheckLogRecordView
import online.ilyin.status.check.use.case.model.CheckLogView
import online.ilyin.status.check.use.case.model.ChecksStatusView
import online.ilyin.status.check.use.case.model.CheckStatusView
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class CheckStateViewUseCase(
  private val checkStateService: CheckStateService,
  private val checkLogService: CheckLogService,
  private val checkLogDetailsRepository: CheckLogDetailsRepository
) {
  suspend fun getAllChecksState(): ChecksStatusView {
    val states = checkStateService.getAllStates()
    return ChecksStatusView(
      checks = states.map {
        CheckStatusView(it.type, it.status)
      }
    )
  }

  suspend fun getCheckLog(
    checkType: CheckType,
    status: CheckStatus?,
    before: Instant,
    max: Int
  ): CheckLogView {
    val logs = checkLogService.getLogs(checkType, status, before, max)
      .map {
        val hasDetails = checkLogDetailsRepository.existsByLogId(it.id)
        CheckLogRecordView(it.id, it.createdAt, it.status, hasDetails.awaitSingle())
      }

    return CheckLogView(
      records = logs.toList()
    )
  }

  suspend fun getCheckLogDetails(logId: Long): CheckLogData? {
    return checkLogDetailsRepository.findByLogId(logId).awaitSingleOrNull()?.data
  }
}
