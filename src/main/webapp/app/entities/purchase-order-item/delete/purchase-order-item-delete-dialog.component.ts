import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';

@Component({
  templateUrl: './purchase-order-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PurchaseOrderItemDeleteDialogComponent {
  purchaseOrderItem?: IPurchaseOrderItem;

  protected purchaseOrderItemService = inject(PurchaseOrderItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.purchaseOrderItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
