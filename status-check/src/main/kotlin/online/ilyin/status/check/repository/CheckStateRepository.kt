package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.CheckState
import online.ilyin.status.check.model.CheckType
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository

interface CheckStateRepository : ReactiveCrudRepository<CheckState, CheckType>,
    ReactiveSortingRepository<CheckState, CheckType> {
}
