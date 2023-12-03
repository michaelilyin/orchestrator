package online.ilyin.status.check.entity

import online.ilyin.status.check.model.NetworkType
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("network_state_check_log")
data class NetworkStateCheckLogEntity(
    val type: NetworkType,
    val createdAt: Instant
) {
    @Id
    var id: Long = 0
}