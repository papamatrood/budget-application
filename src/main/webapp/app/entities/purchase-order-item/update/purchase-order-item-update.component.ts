import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';
import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';
import { PurchaseOrderItemFormGroup, PurchaseOrderItemFormService } from './purchase-order-item-form.service';

@Component({
  selector: 'jhi-purchase-order-item-update',
  templateUrl: './purchase-order-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PurchaseOrderItemUpdateComponent implements OnInit {
  isSaving = false;
  purchaseOrderItem: IPurchaseOrderItem | null = null;

  purchaseOrdersSharedCollection: IPurchaseOrder[] = [];

  protected purchaseOrderItemService = inject(PurchaseOrderItemService);
  protected purchaseOrderItemFormService = inject(PurchaseOrderItemFormService);
  protected purchaseOrderService = inject(PurchaseOrderService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PurchaseOrderItemFormGroup = this.purchaseOrderItemFormService.createPurchaseOrderItemFormGroup();

  comparePurchaseOrder = (o1: IPurchaseOrder | null, o2: IPurchaseOrder | null): boolean =>
    this.purchaseOrderService.comparePurchaseOrder(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ purchaseOrderItem }) => {
      this.purchaseOrderItem = purchaseOrderItem;
      if (purchaseOrderItem) {
        this.updateForm(purchaseOrderItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const purchaseOrderItem = this.purchaseOrderItemFormService.getPurchaseOrderItem(this.editForm);
    if (purchaseOrderItem.id !== null) {
      this.subscribeToSaveResponse(this.purchaseOrderItemService.update(purchaseOrderItem));
    } else {
      this.subscribeToSaveResponse(this.purchaseOrderItemService.create(purchaseOrderItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPurchaseOrderItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(purchaseOrderItem: IPurchaseOrderItem): void {
    this.purchaseOrderItem = purchaseOrderItem;
    this.purchaseOrderItemFormService.resetForm(this.editForm, purchaseOrderItem);

    this.purchaseOrdersSharedCollection = this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing<IPurchaseOrder>(
      this.purchaseOrdersSharedCollection,
      purchaseOrderItem.purchaseOrder,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.purchaseOrderService
      .query()
      .pipe(map((res: HttpResponse<IPurchaseOrder[]>) => res.body ?? []))
      .pipe(
        map((purchaseOrders: IPurchaseOrder[]) =>
          this.purchaseOrderService.addPurchaseOrderToCollectionIfMissing<IPurchaseOrder>(
            purchaseOrders,
            this.purchaseOrderItem?.purchaseOrder,
          ),
        ),
      )
      .subscribe((purchaseOrders: IPurchaseOrder[]) => (this.purchaseOrdersSharedCollection = purchaseOrders));
  }
}
