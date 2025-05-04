import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import MandateResolve from './route/mandate-routing-resolve.service';

const mandateRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/mandate.component').then(m => m.MandateComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/mandate-detail.component').then(m => m.MandateDetailComponent),
    resolve: {
      mandate: MandateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/mandate-update.component').then(m => m.MandateUpdateComponent),
    resolve: {
      mandate: MandateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/mandate-update.component').then(m => m.MandateUpdateComponent),
    resolve: {
      mandate: MandateResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default mandateRoute;
