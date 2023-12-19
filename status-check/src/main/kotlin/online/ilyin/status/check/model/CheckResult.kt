package online.ilyin.status.check.model

import online.ilyin.status.check.entity.CheckLogData

data class CheckResult(
  val status: CheckStatus,
  val data: CheckLogData
)
