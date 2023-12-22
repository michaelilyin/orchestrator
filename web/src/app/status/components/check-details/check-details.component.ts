import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {
  CheckStatus,
  CheckLogRecordView,
  CheckLogView,
  NetworkType, CheckLogData
} from "../../models/network-state";
import {
  BehaviorSubject,
  combineLatest,
  combineLatestWith,
  distinctUntilChanged,
  EMPTY,
  map,
  Observable, reduce, scan,
  shareReplay, startWith, Subject,
  switchMap
} from "rxjs";
import {CheckStateService} from "../../service/check-state.service";
import {AsyncPipe, DatePipe, JsonPipe} from "@angular/common";
import {MatListModule} from "@angular/material/list";
import {StatusIconComponent} from "../status-icon/status-icon.component";
import {MatIconModule} from "@angular/material/icon";
import {LogsComponent} from "../../../sdk/logs/logs.component";
import {Level, LogEntry, LogEntryDetailsMap} from "../../../sdk/logs/logs.model";
import {TypedChanges} from "../../../sdk/angular/changes";
import {MatButtonToggleModule} from "@angular/material/button-toggle";
import {LineExpandableContentDirective} from "../../../sdk/logs/log-line/line-expandable-content.directive";
import {LogLineComponent} from "../../../sdk/logs/log-line/log-line.component";

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
    MatButtonToggleModule,
    JsonPipe,
    LineExpandableContentDirective,
    LogLineComponent
  ],
  templateUrl: './check-details.component.html',
  styleUrl: './check-details.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckDetailsComponent implements OnChanges {
  @Input() type?: NetworkType

  private readonly detailsRequest$ = new Subject<number>()

  readonly detailsMap$: Observable<LogEntryDetailsMap> = this.detailsRequest$.pipe(
    switchMap(id => this.networkStateService.getCheckDetails(id).pipe(
      map(res => [id, res] as [number, CheckLogData])
    )),
    scan((acc, [id, data]) => {
      return {
        ...acc,
        [id]: {
          data
        }
      }
    }, {} as LogEntryDetailsMap),
    startWith({}),
  )

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
    this.detailsRequest$.next(entry.id)
  }
}
