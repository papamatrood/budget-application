import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../purchase-order-item.test-samples';

import { PurchaseOrderItemFormService } from './purchase-order-item-form.service';

describe('PurchaseOrderItem Form Service', () => {
  let service: PurchaseOrderItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PurchaseOrderItemFormService);
  });

  describe('Service methods', () => {
    describe('createPurchaseOrderItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            productName: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            totalAmount: expect.any(Object),
            purchaseOrder: expect.any(Object),
          }),
        );
      });

      it('passing IPurchaseOrderItem should create a new form with FormGroup', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            productName: expect.any(Object),
            quantity: expect.any(Object),
            unitPrice: expect.any(Object),
            totalAmount: expect.any(Object),
            purchaseOrder: expect.any(Object),
          }),
        );
      });
    });

    describe('getPurchaseOrderItem', () => {
      it('should return NewPurchaseOrderItem for default PurchaseOrderItem initial value', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup(sampleWithNewData);

        const purchaseOrderItem = service.getPurchaseOrderItem(formGroup) as any;

        expect(purchaseOrderItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewPurchaseOrderItem for empty PurchaseOrderItem initial value', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup();

        const purchaseOrderItem = service.getPurchaseOrderItem(formGroup) as any;

        expect(purchaseOrderItem).toMatchObject({});
      });

      it('should return IPurchaseOrderItem', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup(sampleWithRequiredData);

        const purchaseOrderItem = service.getPurchaseOrderItem(formGroup) as any;

        expect(purchaseOrderItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPurchaseOrderItem should not enable id FormControl', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPurchaseOrderItem should disable id FormControl', () => {
        const formGroup = service.createPurchaseOrderItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
