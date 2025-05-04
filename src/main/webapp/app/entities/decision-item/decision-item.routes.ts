import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import DecisionItemResolve from './route/decision-item-routing-resolve.service';

const decisionItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/decision-item.component').then(m => m.DecisionItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/decision-item-detail.component').then(m => m.DecisionItemDetailComponent),
    resolve: {
      decisionItem: DecisionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/decision-item-update.component').then(m => m.DecisionItemUpdateComponent),
    resolve: {
      decisionItem: DecisionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/decision-item-update.component').then(m => m.DecisionItemUpdateComponent),
    resolve: {
      decisionItem: DecisionItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default decisionItemRoute;
