import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPurchaseOrderItem, NewPurchaseOrderItem } from '../purchase-order-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPurchaseOrderItem for edit and NewPurchaseOrderItemFormGroupInput for create.
 */
type PurchaseOrderItemFormGroupInput = IPurchaseOrderItem | PartialWithRequiredKeyOf<NewPurchaseOrderItem>;

type PurchaseOrderItemFormDefaults = Pick<NewPurchaseOrderItem, 'id'>;

type PurchaseOrderItemFormGroupContent = {
  id: FormControl<IPurchaseOrderItem['id'] | NewPurchaseOrderItem['id']>;
  productName: FormControl<IPurchaseOrderItem['productName']>;
  quantity: FormControl<IPurchaseOrderItem['quantity']>;
  unitPrice: FormControl<IPurchaseOrderItem['unitPrice']>;
  totalAmount: FormControl<IPurchaseOrderItem['totalAmount']>;
  purchaseOrder: FormControl<IPurchaseOrderItem['purchaseOrder']>;
};

export type PurchaseOrderItemFormGroup = FormGroup<PurchaseOrderItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderItemFormService {
  createPurchaseOrderItemFormGroup(purchaseOrderItem: PurchaseOrderItemFormGroupInput = { id: null }): PurchaseOrderItemFormGroup {
    const purchaseOrderItemRawValue = {
      ...this.getFormDefaults(),
      ...purchaseOrderItem,
    };
    return new FormGroup<PurchaseOrderItemFormGroupContent>({
      id: new FormControl(
        { value: purchaseOrderItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      productName: new FormControl(purchaseOrderItemRawValue.productName, {
        validators: [Validators.required],
      }),
      quantity: new FormControl(purchaseOrderItemRawValue.quantity, {
        validators: [Validators.required],
      }),
      unitPrice: new FormControl(purchaseOrderItemRawValue.unitPrice, {
        validators: [Validators.required],
      }),
      totalAmount: new FormControl(purchaseOrderItemRawValue.totalAmount, {
        validators: [Validators.required],
      }),
      purchaseOrder: new FormControl(purchaseOrderItemRawValue.purchaseOrder),
    });
  }

  getPurchaseOrderItem(form: PurchaseOrderItemFormGroup): IPurchaseOrderItem | NewPurchaseOrderItem {
    return form.getRawValue() as IPurchaseOrderItem | NewPurchaseOrderItem;
  }

  resetForm(form: PurchaseOrderItemFormGroup, purchaseOrderItem: PurchaseOrderItemFormGroupInput): void {
    const purchaseOrderItemRawValue = { ...this.getFormDefaults(), ...purchaseOrderItem };
    form.reset(
      {
        ...purchaseOrderItemRawValue,
        id: { value: purchaseOrderItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PurchaseOrderItemFormDefaults {
    return {
      id: null,
    };
  }
}
