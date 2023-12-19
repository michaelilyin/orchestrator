package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.CheckLog
import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import java.time.Instant

interface CheckLogRepository : ReactiveCrudRepository<CheckLog, Long>,
  ReactiveSortingRepository<CheckLog, Long> {
  @Query(
    """
         select *
         from check_log
         where type = :networkType
            and (:status is null or :status = status)
            and created_at < :since
         order by created_at desc
         limit :max
       """
  )
  fun selectAllByType(
    checkType: CheckType,
    status: CheckStatus?,
    since: Instant,
    max: Int
  ): Flux<CheckLog>
}
