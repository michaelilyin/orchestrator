package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.NetworkStateCheckLogEntity
import org.springframework.data.repository.Repository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface NetworkCheckLogRepository : ReactiveCrudRepository<NetworkStateCheckLogEntity, Long>,
    ReactiveSortingRepository<NetworkStateCheckLogEntity, Long> {
}