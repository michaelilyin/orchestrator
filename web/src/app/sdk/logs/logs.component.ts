import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {LogEntry} from "./logs.model";
import {DatePipe, NgForOf} from "@angular/common";

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [
    NgForOf,
    DatePipe
  ],
  templateUrl: './logs.component.html',
  styleUrl: './logs.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LogsComponent {
  @Input() entries: LogEntry[] = []

  focused: LogEntry | null = null

  focus(entry: LogEntry | null) {
    this.focused = entry
  }
}
