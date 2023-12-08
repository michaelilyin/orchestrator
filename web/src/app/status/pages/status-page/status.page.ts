import {ChangeDetectionStrategy, Component} from '@angular/core';
import {NetworkStateService} from "../../service/network-state.service";
import {CommonModule} from "@angular/common";
import {map} from "rxjs";
import {MatExpansionModule} from "@angular/material/expansion";
import {MatIconModule} from "@angular/material/icon";
import {CheckDetailsComponent} from "../../components/check-details/check-details.component";
import {StatusIconComponent} from "../../components/status-icon/status-icon.component";
import {MatCardModule} from "@angular/material/card";

@Component({
  selector: 'app-status',
  standalone: true,
  imports: [
    CommonModule,
    MatExpansionModule,
    MatIconModule,
    CheckDetailsComponent,
    StatusIconComponent,
    MatCardModule
  ],
  templateUrl: './status.page.html',
  styleUrl: './status.page.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class StatusPage {
  public states$ = this.networkStateService.getNetworkState().pipe(
    map(state => state.networks)
  )

  constructor(private readonly networkStateService: NetworkStateService) {
  }
}
