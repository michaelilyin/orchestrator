import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkStatusLogView, NetworkStatusView, NetworkType} from "../pages/models/network-state";

@Injectable({
  providedIn: 'root'
})
export class NetworkStateService {

  constructor(private readonly http: HttpClient) { }

  getNetworkState(): Observable<NetworkStatusView> {
    return this.http.get<NetworkStatusView>('/api/networks/status')
  }

  getNetworkCheckLog(networkType: NetworkType): Observable<NetworkStatusLogView> {
    return this.http.get<NetworkStatusLogView>(`/api/networks/${networkType}/log`, {
      params: {
        before: (new Date()).toISOString(),
        max: 10
      }
    })
  }
}
