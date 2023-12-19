package online.ilyin.status.check.entity

import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("check_state")
data class CheckState(
  @Id
    val type: CheckType,

  var status: CheckStatus,
): Persistable<CheckType> {
    @Transient
    private var new: Boolean = false
    override fun getId(): CheckType = type

    override fun isNew(): Boolean = new

    fun markAsNew(): CheckState {
        new = true
        return this
    }
}
