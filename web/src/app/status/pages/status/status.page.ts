import {ChangeDetectionStrategy, Component} from '@angular/core';
import {NetworkStateService} from "../../service/network-state.service";
import {CommonModule} from "@angular/common";
import {map} from "rxjs";

@Component({
  selector: 'app-status',
  standalone: true,
  imports: [
    CommonModule
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
