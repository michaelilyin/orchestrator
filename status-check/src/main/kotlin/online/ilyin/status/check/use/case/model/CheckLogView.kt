package online.ilyin.status.check.use.case.model

import online.ilyin.status.check.model.CheckStatus
import java.time.Instant

data class CheckLogView(
  val records: List<CheckLogRecordView>
)

data class CheckLogRecordView(
  val id: Long,
  val createdAt: Instant,
  val status: CheckStatus,
  val hasDetails: Boolean
)

