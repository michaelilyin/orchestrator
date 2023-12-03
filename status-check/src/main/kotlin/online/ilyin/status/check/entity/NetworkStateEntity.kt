package online.ilyin.status.check.entity

import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("network_state")
data class NetworkStateEntity(
    @Id
    val type: NetworkType,

    var status: NetworkStateStatus,
): Persistable<NetworkType> {
    @Transient
    private var new: Boolean = false
    override fun getId(): NetworkType = type

    override fun isNew(): Boolean = new

    fun markAsNew(): NetworkStateEntity {
        new = true
        return this
    }
}