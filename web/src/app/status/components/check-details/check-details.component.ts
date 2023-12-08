import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {
  NetworkStateStatus,
  NetworkStatusLogEntryView,
  NetworkStatusLogView,
  NetworkType
} from "../../models/network-state";
import {EMPTY, map, Observable, shareReplay} from "rxjs";
import {NetworkStateService} from "../../service/network-state.service";
import {AsyncPipe, DatePipe, NgForOf, NgIf} from "@angular/common";
import {MatListModule} from "@angular/material/list";
import {StatusIconComponent} from "../status-icon/status-icon.component";
import {MatIconModule} from "@angular/material/icon";
import {LogsComponent} from "../../../sdk/logs/logs.component";
import {Level, LogEntry} from "../../../sdk/logs/logs.model";

@Component({
  selector: 'app-check-details',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    NgForOf,
    MatListModule,
    DatePipe,
    StatusIconComponent,
    MatIconModule,
    LogsComponent
  ],
  templateUrl: './check-details.component.html',
  styleUrl: './check-details.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckDetailsComponent implements OnChanges {
  @Input() type?: NetworkType

  log$: Observable<NetworkStatusLogView> = EMPTY
  entries$: Observable<LogEntry[]> = EMPTY

  constructor(private readonly networkStateService: NetworkStateService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('type' in changes) {
      this.log$ = this.networkStateService.getNetworkCheckLog(changes['type'].currentValue).pipe(
        shareReplay(1)
      )
      this.entries$ = this.log$.pipe(map(log => {
        return log.checks.map(check => {
          return {
            timestamp: check.createdAt,
            message: this.extractMessage(check),
            level: this.extractLevel(check)
          }
        })
      }))
    }
  }

  private extractMessage(check: NetworkStatusLogEntryView): string {
    switch (check.status) {
      case NetworkStateStatus.OK:
        return 'Check has successfully completed';
      case NetworkStateStatus.FAILED:
        return 'Check has failed';
    }
  }

  private extractLevel(check: NetworkStatusLogEntryView): Level {
    switch (check.status) {
      case NetworkStateStatus.OK:
        return Level.INFO;
      case NetworkStateStatus.FAILED:
        return Level.ERROR;
    }
  }
}
