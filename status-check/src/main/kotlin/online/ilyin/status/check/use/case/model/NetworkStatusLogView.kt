package online.ilyin.status.check.use.case.model

import online.ilyin.status.check.model.NetworkStateStatus
import java.time.Instant

data class NetworkStatusLogView(
  val checks: List<NetworkStatusLogEntryView>
)

data class NetworkStatusLogEntryView(
  val id: Long,
  val createdAt: Instant,
  val status: NetworkStateStatus
)

