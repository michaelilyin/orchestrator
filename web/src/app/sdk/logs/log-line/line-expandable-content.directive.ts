import {Directive, TemplateRef} from '@angular/core';

export interface LineExtendableContentContext<T> {
  $implicit?: T
}

@Directive({
  selector: '[appLineExpandableContent]',
  standalone: true
})
export class LineExpandableContentDirective {

  constructor(readonly template: TemplateRef<LineExtendableContentContext<object>>) {
  }

}
