import {ChangeDetectionStrategy, Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {NetworkStatusLogView, NetworkType} from "../pages/models/network-state";
import {EMPTY, Observable, shareReplay} from "rxjs";
import {NetworkStateService} from "../service/network-state.service";
import {AsyncPipe, NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-check-details',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    NgForOf
  ],
  templateUrl: './check-details.component.html',
  styleUrl: './check-details.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CheckDetailsComponent implements OnChanges {
  @Input() type?: NetworkType

  log$: Observable<NetworkStatusLogView> = EMPTY

  constructor(private readonly networkStateService: NetworkStateService) {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if ('type' in changes) {
      this.log$ = this.networkStateService.getNetworkCheckLog(changes['type'].currentValue).pipe(
        shareReplay(1)
      )
    }
  }

}
