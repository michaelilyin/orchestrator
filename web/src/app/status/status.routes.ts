import {Routes} from "@angular/router";
import {StatusPage} from "./pages/status/status.page";

export const STATUS_ROUTES: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: StatusPage
  }
]
