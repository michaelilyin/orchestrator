import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {NetworkStatusView} from "../pages/models/network-state";

@Injectable({
  providedIn: 'root'
})
export class NetworkStateService {

  constructor(private readonly http: HttpClient) { }

  getNetworkState(): Observable<NetworkStatusView> {
    return this.http.get<NetworkStatusView>('/api/network/status')
  }
}
