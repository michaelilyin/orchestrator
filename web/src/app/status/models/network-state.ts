export enum NetworkType {
  LOOPBACK = 'LOOPBACK',
  LOCAL = 'LOCAL',
  GLOBAL = 'GLOBAL',
}

export enum CheckStatus {
  OK = 'OK',
  FAILED = 'FAILED',
}

export interface CheckStatusView {
  readonly type: NetworkType
  readonly status: CheckStatus
}


export interface ChecksStatusView {
  readonly checks: CheckStatusView[]
}

export interface CheckLogView {
  readonly records: CheckLogRecordView[]
}

export interface CheckLogRecordView {
  readonly id: number,
  readonly createdAt: string,
  readonly status: CheckStatus
  readonly hasDetails: boolean
}

export interface CheckLogData {

}


export interface NetworkPingData extends CheckLogData {
  readonly host: string
  readonly duration: string
}
