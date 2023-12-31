import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatIconModule} from "@angular/material/icon";

import {CheckStatus} from "../../models/network-state";

@Component({
  selector: 'app-status-icon',
  standalone: true,
  imports: [
    MatIconModule
],
  templateUrl: './status-icon.component.html',
  styleUrl: './status-icon.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StatusIconComponent {
  @Input() status?: CheckStatus
}
