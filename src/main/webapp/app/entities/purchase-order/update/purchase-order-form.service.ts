import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPurchaseOrder, NewPurchaseOrder } from '../purchase-order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPurchaseOrder for edit and NewPurchaseOrderFormGroupInput for create.
 */
type PurchaseOrderFormGroupInput = IPurchaseOrder | PartialWithRequiredKeyOf<NewPurchaseOrder>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPurchaseOrder | NewPurchaseOrder> = Omit<T, 'orderDate'> & {
  orderDate?: string | null;
};

type PurchaseOrderFormRawValue = FormValueOf<IPurchaseOrder>;

type NewPurchaseOrderFormRawValue = FormValueOf<NewPurchaseOrder>;

type PurchaseOrderFormDefaults = Pick<NewPurchaseOrder, 'id' | 'orderDate'>;

type PurchaseOrderFormGroupContent = {
  id: FormControl<PurchaseOrderFormRawValue['id'] | NewPurchaseOrder['id']>;
  nameOfTheMinistry: FormControl<PurchaseOrderFormRawValue['nameOfTheMinistry']>;
  orderNumber: FormControl<PurchaseOrderFormRawValue['orderNumber']>;
  orderDate: FormControl<PurchaseOrderFormRawValue['orderDate']>;
  totalAmountWithoutTax: FormControl<PurchaseOrderFormRawValue['totalAmountWithoutTax']>;
  taxRate: FormControl<PurchaseOrderFormRawValue['taxRate']>;
  totalTaxAmount: FormControl<PurchaseOrderFormRawValue['totalTaxAmount']>;
  prepaidTaxAmount: FormControl<PurchaseOrderFormRawValue['prepaidTaxAmount']>;
  totalAmountWithTax: FormControl<PurchaseOrderFormRawValue['totalAmountWithTax']>;
  authExpenditureNumber: FormControl<PurchaseOrderFormRawValue['authExpenditureNumber']>;
  allocatedCredits: FormControl<PurchaseOrderFormRawValue['allocatedCredits']>;
  committedExpenditures: FormControl<PurchaseOrderFormRawValue['committedExpenditures']>;
  availableBalance: FormControl<PurchaseOrderFormRawValue['availableBalance']>;
  annexDecision: FormControl<PurchaseOrderFormRawValue['annexDecision']>;
  supplier: FormControl<PurchaseOrderFormRawValue['supplier']>;
  engagement: FormControl<PurchaseOrderFormRawValue['engagement']>;
};

export type PurchaseOrderFormGroup = FormGroup<PurchaseOrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderFormService {
  createPurchaseOrderFormGroup(purchaseOrder: PurchaseOrderFormGroupInput = { id: null }): PurchaseOrderFormGroup {
    const purchaseOrderRawValue = this.convertPurchaseOrderToPurchaseOrderRawValue({
      ...this.getFormDefaults(),
      ...purchaseOrder,
    });
    return new FormGroup<PurchaseOrderFormGroupContent>({
      id: new FormControl(
        { value: purchaseOrderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nameOfTheMinistry: new FormControl(purchaseOrderRawValue.nameOfTheMinistry, {
        validators: [Validators.required],
      }),
      orderNumber: new FormControl(purchaseOrderRawValue.orderNumber, {
        validators: [Validators.required],
      }),
      orderDate: new FormControl(purchaseOrderRawValue.orderDate, {
        validators: [Validators.required],
      }),
      totalAmountWithoutTax: new FormControl(purchaseOrderRawValue.totalAmountWithoutTax),
      taxRate: new FormControl(purchaseOrderRawValue.taxRate),
      totalTaxAmount: new FormControl(purchaseOrderRawValue.totalTaxAmount),
      prepaidTaxAmount: new FormControl(purchaseOrderRawValue.prepaidTaxAmount),
      totalAmountWithTax: new FormControl(purchaseOrderRawValue.totalAmountWithTax),
      authExpenditureNumber: new FormControl(purchaseOrderRawValue.authExpenditureNumber),
      allocatedCredits: new FormControl(purchaseOrderRawValue.allocatedCredits),
      committedExpenditures: new FormControl(purchaseOrderRawValue.committedExpenditures),
      availableBalance: new FormControl(purchaseOrderRawValue.availableBalance),
      annexDecision: new FormControl(purchaseOrderRawValue.annexDecision),
      supplier: new FormControl(purchaseOrderRawValue.supplier),
      engagement: new FormControl(purchaseOrderRawValue.engagement),
    });
  }

  getPurchaseOrder(form: PurchaseOrderFormGroup): IPurchaseOrder | NewPurchaseOrder {
    return this.convertPurchaseOrderRawValueToPurchaseOrder(form.getRawValue() as PurchaseOrderFormRawValue | NewPurchaseOrderFormRawValue);
  }

  resetForm(form: PurchaseOrderFormGroup, purchaseOrder: PurchaseOrderFormGroupInput): void {
    const purchaseOrderRawValue = this.convertPurchaseOrderToPurchaseOrderRawValue({ ...this.getFormDefaults(), ...purchaseOrder });
    form.reset(
      {
        ...purchaseOrderRawValue,
        id: { value: purchaseOrderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PurchaseOrderFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      orderDate: currentTime,
    };
  }

  private convertPurchaseOrderRawValueToPurchaseOrder(
    rawPurchaseOrder: PurchaseOrderFormRawValue | NewPurchaseOrderFormRawValue,
  ): IPurchaseOrder | NewPurchaseOrder {
    return {
      ...rawPurchaseOrder,
      orderDate: dayjs(rawPurchaseOrder.orderDate, DATE_TIME_FORMAT),
    };
  }

  private convertPurchaseOrderToPurchaseOrderRawValue(
    purchaseOrder: IPurchaseOrder | (Partial<NewPurchaseOrder> & PurchaseOrderFormDefaults),
  ): PurchaseOrderFormRawValue | PartialWithRequiredKeyOf<NewPurchaseOrderFormRawValue> {
    return {
      ...purchaseOrder,
      orderDate: purchaseOrder.orderDate ? purchaseOrder.orderDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
