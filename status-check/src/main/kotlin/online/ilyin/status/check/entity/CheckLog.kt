package online.ilyin.status.check.entity

import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("check_log")
data class CheckLog(
  val type: CheckType,
  val createdAt: Instant,
  val status: CheckStatus
) {
  @Id
  var id: Long = 0
}
