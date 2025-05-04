import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';

const purchaseOrderItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IPurchaseOrderItem> => {
  const id = route.params.id;
  if (id) {
    return inject(PurchaseOrderItemService)
      .find(id)
      .pipe(
        mergeMap((purchaseOrderItem: HttpResponse<IPurchaseOrderItem>) => {
          if (purchaseOrderItem.body) {
            return of(purchaseOrderItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default purchaseOrderItemResolve;
