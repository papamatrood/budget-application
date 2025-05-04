import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import FinancialYearResolve from './route/financial-year-routing-resolve.service';

const financialYearRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/financial-year.component').then(m => m.FinancialYearComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/financial-year-detail.component').then(m => m.FinancialYearDetailComponent),
    resolve: {
      financialYear: FinancialYearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/financial-year-update.component').then(m => m.FinancialYearUpdateComponent),
    resolve: {
      financialYear: FinancialYearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/financial-year-update.component').then(m => m.FinancialYearUpdateComponent),
    resolve: {
      financialYear: FinancialYearResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default financialYearRoute;
