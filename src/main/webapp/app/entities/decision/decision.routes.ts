import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DecisionResolve from './route/decision-routing-resolve.service';

const decisionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/decision.component').then(m => m.DecisionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/decision-detail.component').then(m => m.DecisionDetailComponent),
    resolve: {
      decision: DecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/decision-update.component').then(m => m.DecisionUpdateComponent),
    resolve: {
      decision: DecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/decision-update.component').then(m => m.DecisionUpdateComponent),
    resolve: {
      decision: DecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default decisionRoute;
