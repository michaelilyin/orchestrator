import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: 'status',
    loadChildren: () => import('./status/status.routes').then(m => m.STATUS_ROUTES)
  }
];
