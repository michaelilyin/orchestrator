import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {
  CheckStatus,
  CheckLogRecordView,
  CheckLogView,
  NetworkType
} from "../../models/network-state";
import {
  BehaviorSubject,
  combineLatest,
  combineLatestWith,
  distinctUntilChanged,
  EMPTY,
  map,
  Observable,
  shareReplay,
  switchMap
} from "rxjs";
import {CheckStateService} from "../../service/check-state.service";
import {AsyncPipe, DatePipe} from "@angular/common";
import {MatListModule} from "@angular/material/list";
import {StatusIconComponent} from "../status-icon/status-icon.component";
import {MatIconModule} from "@angular/material/icon";
import {LogsComponent} from "../../../sdk/logs/logs.component";
import {Level, LogEntry} from "../../../sdk/logs/logs.model";
import {TypedChanges} from "../../../sdk/angular/changes";
import {MatButtonToggleModule} from "@angular/material/button-toggle";

function convertBucketToLogEntries(log: CheckLogView): LogEntry[] {
  return log.records.map(check => {
    return {
      id: check.id,
      timestamp: check.createdAt,
      message: extractMessage(check),
      level: extractLevel(check),
      expandable: check.hasDetails
    }
  });
}

function extractMessage(check: CheckLogRecordView): string {
  switch (check.status) {
    case CheckStatus.OK:
      return 'Check has successfully completed';
    case CheckStatus.FAILED:
      return 'Check has failed';
  }
}

function extractLevel(check: CheckLogRecordView): Level {
  switch (check.status) {
    case CheckStatus.OK:
      return Level.INFO;
    case CheckStatus.FAILED:
      return Level.ERROR;
  }
}

@Component({
  selector: 'app-check-details',
  standalone: true,
  imports: [
    AsyncPipe,
    MatListModule,
    DatePipe,
    StatusIconComponent,
    MatIconModule,
    LogsComponent,
    MatButtonToggleModule
  ],
  templateUrl: './check-details.component.html',
  styleUrl: './check-details.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckDetailsComponent implements OnChanges {
  @Input() type?: NetworkType

  private readonly type$ = new BehaviorSubject<NetworkType | null>(null)

  readonly status$ = new BehaviorSubject<CheckStatus | null>(null)

  readonly log$ = combineLatest([
    this.type$.pipe(distinctUntilChanged()),
    this.status$.pipe(distinctUntilChanged())
  ]).pipe(
    switchMap(([type, status]) => {
      if (type == null) {
        return EMPTY
      }
      console.info("call", type, status)
      return this.networkStateService.getNetworkCheckLog(type, status)
    }),
    shareReplay(1)
  )

  readonly entries$: Observable<LogEntry[]> = this.log$.pipe(
    map(bucket => convertBucketToLogEntries(bucket)),
    shareReplay(1)
  )

  constructor(private readonly networkStateService: CheckStateService) {
  }

  ngOnChanges(changes: TypedChanges<this>): void {
    if (changes.type?.currentValue != null) {
      this.type$.next(changes.type.currentValue)
    }
  }

  expand(entry: LogEntry) {
    this.networkStateService.getCheckDetails(entry.id).subscribe()
  }
}
