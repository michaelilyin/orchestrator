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

export interface NetworkStatusLogView {
  readonly checks: NetworkStatusLogEntryView[]
}

export interface NetworkStatusLogEntryView {
  readonly id: number,
  readonly createdAt: string,
  readonly status: NetworkStateStatus
}

