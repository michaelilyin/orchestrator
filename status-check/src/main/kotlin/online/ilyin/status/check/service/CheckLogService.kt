package online.ilyin.status.check.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import online.ilyin.status.check.entity.CheckLog
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.repository.CheckLogRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CheckLogService(
  private val checkLogRepository: CheckLogRepository
) {
  fun getLogs(
    checkType: CheckType,
    status: CheckStatus?,
    before: Instant,
    max: Int
  ): Flow<CheckLog> {
    return checkLogRepository.selectAllByType(checkType, status, before, max).asFlow()
  }
}
