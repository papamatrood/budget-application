import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AnnexDecisionResolve from './route/annex-decision-routing-resolve.service';

const annexDecisionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/annex-decision.component').then(m => m.AnnexDecisionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/annex-decision-detail.component').then(m => m.AnnexDecisionDetailComponent),
    resolve: {
      annexDecision: AnnexDecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/annex-decision-update.component').then(m => m.AnnexDecisionUpdateComponent),
    resolve: {
      annexDecision: AnnexDecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/annex-decision-update.component').then(m => m.AnnexDecisionUpdateComponent),
    resolve: {
      annexDecision: AnnexDecisionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default annexDecisionRoute;
