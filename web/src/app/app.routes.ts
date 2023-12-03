import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'status',
    loadComponent: () => import('./status/pages/status/status.page').then(m => m.StatusPage)
  }
];
