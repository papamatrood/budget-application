import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SubTitleResolve from './route/sub-title-routing-resolve.service';

const subTitleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sub-title.component').then(m => m.SubTitleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sub-title-detail.component').then(m => m.SubTitleDetailComponent),
    resolve: {
      subTitle: SubTitleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sub-title-update.component').then(m => m.SubTitleUpdateComponent),
    resolve: {
      subTitle: SubTitleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sub-title-update.component').then(m => m.SubTitleUpdateComponent),
    resolve: {
      subTitle: SubTitleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default subTitleRoute;
