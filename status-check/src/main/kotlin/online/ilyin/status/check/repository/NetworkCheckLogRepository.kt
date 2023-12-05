package online.ilyin.status.check.repository

import online.ilyin.status.check.entity.NetworkStateCheckLogEntity
import online.ilyin.status.check.model.NetworkType
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.data.repository.reactive.ReactiveSortingRepository
import reactor.core.publisher.Flux
import java.time.Instant

interface NetworkCheckLogRepository : ReactiveCrudRepository<NetworkStateCheckLogEntity, Long>,
  ReactiveSortingRepository<NetworkStateCheckLogEntity, Long> {

  @Query(
       """
         select *
         from network_state_check_log
         where type = :networkType
            and created_at < :since
         order by created_at desc
         limit :max
       """
  )
  fun selectAllByType(networkType: NetworkType, since: Instant, max: Int): Flux<NetworkStateCheckLogEntity>
}
