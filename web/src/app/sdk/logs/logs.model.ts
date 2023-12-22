import {CheckLogData} from "../../status/models/network-state";

export enum Level {
  DEBUG = 'DEBUG',
  INFO = 'INFO',
  WARN = 'WARN',
  ERROR = 'ERROR'
}

export interface LogEntry {
  id: number
  timestamp: string
  message: string
  level: Level
  expandable: boolean
}

export interface LogEntryDetails {
  data: CheckLogData
}

export interface LogEntryDetailsMap {
  [p: number]: LogEntryDetails
}
