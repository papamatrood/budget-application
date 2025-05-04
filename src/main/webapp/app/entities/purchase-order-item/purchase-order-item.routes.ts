import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PurchaseOrderItemResolve from './route/purchase-order-item-routing-resolve.service';

const purchaseOrderItemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/purchase-order-item.component').then(m => m.PurchaseOrderItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/purchase-order-item-detail.component').then(m => m.PurchaseOrderItemDetailComponent),
    resolve: {
      purchaseOrderItem: PurchaseOrderItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/purchase-order-item-update.component').then(m => m.PurchaseOrderItemUpdateComponent),
    resolve: {
      purchaseOrderItem: PurchaseOrderItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/purchase-order-item-update.component').then(m => m.PurchaseOrderItemUpdateComponent),
    resolve: {
      purchaseOrderItem: PurchaseOrderItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default purchaseOrderItemRoute;
