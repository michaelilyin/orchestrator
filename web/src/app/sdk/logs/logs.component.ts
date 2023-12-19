import {ChangeDetectionStrategy, Component, EventEmitter, Input, Output} from '@angular/core';
import {LogEntry} from "./logs.model";
import { DatePipe } from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [
    DatePipe,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './logs.component.html',
  styleUrl: './logs.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LogsComponent {
  @Input() entries: LogEntry[] = []

  @Output() expand = new EventEmitter<LogEntry>()

  focused: LogEntry | null = null

  focus(entry: LogEntry | null) {
    this.focused = entry
  }
}
