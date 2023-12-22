import {
  ChangeDetectionStrategy,
  Component,
  ContentChild,
  ContentChildren,
  EventEmitter,
  Input,
  Output
} from '@angular/core';
import {LogEntry, LogEntryDetailsMap} from "./logs.model";
import {DatePipe, JsonPipe, NgTemplateOutlet} from "@angular/common";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {LogLineComponent} from "./log-line/log-line.component";
import {LineExpandableContentDirective} from "./log-line/line-expandable-content.directive";

@Component({
  selector: 'app-logs',
  standalone: true,
  imports: [
    DatePipe,
    MatIconModule,
    MatButtonModule,
    JsonPipe,
    LogLineComponent,
    LineExpandableContentDirective,
    NgTemplateOutlet
  ],
  templateUrl: './logs.component.html',
  styleUrl: './logs.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LogsComponent {
  @Input() entries: LogEntry[] = []

  @Input() details: LogEntryDetailsMap = {}

  @Output() expand = new EventEmitter<LogEntry>()

  @ContentChild(LineExpandableContentDirective, {
    descendants: true,
    read: LineExpandableContentDirective
  })
  readonly expandableContent?: LineExpandableContentDirective

  expanded = new Set<number>()

  focused: LogEntry | null = null

  focus(entry: LogEntry | null) {
    this.focused = entry
  }

  toggle(entry: LogEntry) {
    if (this.expanded.has(entry.id)) {
      this.expanded.delete(entry.id)
    } else {
      this.expand.emit(entry)
      this.expanded.add(entry.id)
    }
  }
}
