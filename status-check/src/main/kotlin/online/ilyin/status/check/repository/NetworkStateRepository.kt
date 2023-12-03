package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.NetworkStateEntity
import online.ilyin.status.check.model.NetworkType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface NetworkStateRepository : ReactiveCrudRepository<NetworkStateEntity, NetworkType>,
    ReactiveSortingRepository<NetworkStateEntity, NetworkType> {
}