import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {CheckStatus, CheckLogView, ChecksStatusView, NetworkType, CheckLogData} from "../models/network-state";

@Injectable({
  providedIn: 'root'
})
export class CheckStateService {

  constructor(private readonly http: HttpClient) {
  }

  getNetworkState(): Observable<ChecksStatusView> {
    return this.http.get<ChecksStatusView>('/api/checks/status')
  }

  getNetworkCheckLog(networkType: NetworkType, status?: CheckStatus | null): Observable<CheckLogView> {
    let params = new HttpParams()
      .append('before', (new Date()).toISOString())
      .append('max', 10)
    if (status != null) {
      params = params.append('status', status)
    }
    return this.http.get<CheckLogView>(`/api/checks/${networkType}/log`, {
      params: params
    })
  }

  getCheckDetails(logId: number): Observable<CheckLogData> {
    return this.http.get<CheckLogView>(`/api/checks/log/${logId}/details`)
  }
}
