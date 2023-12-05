package online.ilyin.status.check.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import online.ilyin.status.check.entity.NetworkStateCheckLogEntity
import online.ilyin.status.check.model.NetworkType
import online.ilyin.status.check.repository.NetworkCheckLogRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class NetworkCheckLogService(
  private val networkCheckLogRepository: NetworkCheckLogRepository
) {
  fun getChecksForNetwork(networkType: NetworkType, before: Instant, max: Int): Flow<NetworkStateCheckLogEntity> {
    return networkCheckLogRepository.selectAllByType(networkType, before, max).asFlow()
  }
}
