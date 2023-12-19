package online.ilyin.status.check.service

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import online.ilyin.status.check.entity.CheckLog
import online.ilyin.status.check.entity.CheckLogDetails
import online.ilyin.status.check.entity.CheckState
import online.ilyin.status.check.model.CheckResult
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import online.ilyin.status.check.repository.CheckLogDetailsRepository
import online.ilyin.status.check.repository.CheckLogRepository
import online.ilyin.status.check.repository.CheckStateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class CheckStateService(
  private val checkStateRepository: CheckStateRepository,
  private val checkLogRepository: CheckLogRepository,
  private val checkLogDetailsRepository: CheckLogDetailsRepository
) {

  suspend fun getStoredStatus(checkType: CheckType): CheckStatus {
    val state = checkStateRepository.findById(checkType).awaitSingleOrNull()
    return state?.status
      ?: CheckStatus.OK
  }

  @Transactional
  suspend fun saveCheckResult(checkType: CheckType, result: CheckResult) {
    val state = checkStateRepository.findById(checkType).awaitSingleOrNull()
      ?: CheckState(checkType, result.status).markAsNew()

    state.status = result.status

    checkStateRepository.save(state).awaitSingle()

    val log = CheckLog(checkType, Instant.now(), result.status)
    checkLogRepository.save(log).awaitSingle()

    val details = CheckLogDetails(log.id, result.data)
    checkLogDetailsRepository.save(details).awaitSingle()
  }

  suspend fun getAllStates(): List<CheckState> {
    return checkStateRepository.findAll().collectList().awaitSingle()
  }
}
