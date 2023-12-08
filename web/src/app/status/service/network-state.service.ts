import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkStateStatus, NetworkStatusLogView, NetworkStatusView, NetworkType} from "../models/network-state";

@Injectable({
  providedIn: 'root'
})
export class NetworkStateService {

  constructor(private readonly http: HttpClient) {
  }

  getNetworkState(): Observable<NetworkStatusView> {
    return this.http.get<NetworkStatusView>('/api/networks/status')
  }

  getNetworkCheckLog(networkType: NetworkType, status?: NetworkStateStatus): Observable<NetworkStatusLogView> {
    let params = new HttpParams()
      .append('before', (new Date()).toISOString())
      .append('max', 10)
    if (status != null) {
      params = params.append('status', status)
    }
    console.info(params)
    return this.http.get<NetworkStatusLogView>(`/api/networks/${networkType}/log`, {
      params: params
    })
  }
}
