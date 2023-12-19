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
