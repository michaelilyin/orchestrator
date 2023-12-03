export enum NetworkType {
  LOOPBACK = 'LOOPBACK',
  LOCAL = 'LOCAL',
  GLOBAL = 'GLOBAL',
}

export enum NetworkStateStatus {
  OK = 'OK',
  FAILED = 'FAILED',
}

export interface NetworkTypeStatusView {
  readonly type: NetworkType
  readonly status: NetworkStateStatus
}


export interface NetworkStatusView {
  readonly networks: NetworkTypeStatusView[]
}
