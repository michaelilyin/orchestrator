package online.ilyin.status.check.controller.model

import online.ilyin.status.check.model.NetworkStateStatus
import online.ilyin.status.check.model.NetworkType

data class NetworkStatusView(
  val networks: List<NetworkTypeStatusView>
)

data class NetworkTypeStatusView(
  val type: NetworkType,
  val status: NetworkStateStatus
)
