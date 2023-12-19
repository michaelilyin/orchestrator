package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.CheckLogDetails
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono

interface CheckLogDetailsRepository : ReactiveCrudRepository<CheckLogDetails, Long> {
  fun findByLogId(logId: Long): Mono<CheckLogDetails>

  fun existsByLogId(logId: Long): Mono<Boolean>
}
