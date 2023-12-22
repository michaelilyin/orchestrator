import {
  ChangeDetectionStrategy,
  Component,
  ContentChild,
  EventEmitter,
  Input,
  Output, TemplateRef,
  ViewEncapsulation
} from '@angular/core';
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {AsyncPipe, JsonPipe, NgTemplateOutlet} from "@angular/common";
import {LineExpandableContentDirective, LineExtendableContentContext} from "./line-expandable-content.directive";
import {throws} from "node:assert";

@Component({
  selector: 'app-log-line',
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    NgTemplateOutlet,
    AsyncPipe,
    JsonPipe
  ],
  templateUrl: './log-line.component.html',
  styleUrl: './log-line.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {
    '[class.line]': 'true',
    '[class.expand-padding]': "!expandable"
  },
})
export class LogLineComponent {
  @Input() expandable: boolean = false

  @Input() expandData?: object

  @Output() expand = new EventEmitter<boolean>()

  expanded = false

  @ContentChild(LineExpandableContentDirective, {
    descendants: true,
    read: LineExpandableContentDirective
  })
  readonly expandableContent?: LineExpandableContentDirective

  @Input()
  expandableTemplate?: TemplateRef<LineExtendableContentContext<object>>

  toggle() {
    this.expanded = !this.expanded;
    this.expand.emit(this.expanded)
  }

  get expandIcon(): string {
    if (this.expanded) {
      return 'keyboard_arrow_down'
    } else {
      return 'keyboard_arrow_right'
    }
  }

  get context(): LineExtendableContentContext<object> {
    return {
      $implicit: this.expandData
    }
  }

  get template(): TemplateRef<object> {
    if (this.expandableContent != null) {
      return this.expandableContent.template
    }
    if (this.expandableTemplate != null) {
      return this.expandableTemplate
    }
    throw Error("Template is required")
  }
}
