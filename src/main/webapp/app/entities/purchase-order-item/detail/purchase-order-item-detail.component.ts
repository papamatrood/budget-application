import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPurchaseOrderItem } from '../purchase-order-item.model';

@Component({
  selector: 'jhi-purchase-order-item-detail',
  templateUrl: './purchase-order-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PurchaseOrderItemDetailComponent {
  purchaseOrderItem = input<IPurchaseOrderItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
