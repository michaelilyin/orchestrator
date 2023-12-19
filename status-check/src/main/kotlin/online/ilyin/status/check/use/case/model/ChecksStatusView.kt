package online.ilyin.status.check.use.case.model

import online.ilyin.status.check.model.CheckStatus
import online.ilyin.status.check.model.CheckType

data class ChecksStatusView(
  val checks: List<CheckStatusView>
)

data class CheckStatusView(
    val type: CheckType,
    val status: CheckStatus
)
