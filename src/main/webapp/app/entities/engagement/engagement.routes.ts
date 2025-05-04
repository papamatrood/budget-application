import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EngagementResolve from './route/engagement-routing-resolve.service';

const engagementRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/engagement.component').then(m => m.EngagementComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/engagement-detail.component').then(m => m.EngagementDetailComponent),
    resolve: {
      engagement: EngagementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/engagement-update.component').then(m => m.EngagementUpdateComponent),
    resolve: {
      engagement: EngagementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/engagement-update.component').then(m => m.EngagementUpdateComponent),
    resolve: {
      engagement: EngagementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default engagementRoute;
