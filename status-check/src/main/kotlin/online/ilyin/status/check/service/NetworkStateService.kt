package online.ilyin.status.check.service

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import online.ilyin.status.check.entity.NetworkStateCheckLogEntity
import online.ilyin.status.check.entity.NetworkStateEntity
import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.repository.NetworkCheckLogRepository
import online.ilyin.status.check.repository.NetworkStateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class NetworkStateService(
    private val networkStateRepository: NetworkStateRepository,
    private val networkCheckLogRepository: NetworkCheckLogRepository
) {

    suspend fun getStoredStatus(networkType: NetworkType): NetworkStateStatus {
        val state = networkStateRepository.findById(networkType).awaitSingleOrNull()
        return state?.status
            ?: NetworkStateStatus.OK
    }

    @Transactional
    suspend fun saveCheckResult(networkType: NetworkType, status: NetworkStateStatus) {
        val state = networkStateRepository.findById(networkType).awaitSingleOrNull()
            ?: NetworkStateEntity(networkType, status).markAsNew()

        state.status = status

        networkStateRepository.save(state).awaitSingle()

        val log = NetworkStateCheckLogEntity(networkType, Instant.now())
        networkCheckLogRepository.save(log).awaitSingle()
    }

  suspend fun getAllStates(): List<NetworkStateEntity> {
    return networkStateRepository.findAll().collectList().awaitSingle()
  }
}
