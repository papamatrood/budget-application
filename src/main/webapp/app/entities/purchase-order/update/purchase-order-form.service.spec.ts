import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../purchase-order.test-samples';

import { PurchaseOrderFormService } from './purchase-order-form.service';

describe('PurchaseOrder Form Service', () => {
  let service: PurchaseOrderFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PurchaseOrderFormService);
  });

  describe('Service methods', () => {
    describe('createPurchaseOrderFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPurchaseOrderFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameOfTheMinistry: expect.any(Object),
            orderNumber: expect.any(Object),
            orderDate: expect.any(Object),
            totalAmountWithoutTax: expect.any(Object),
            taxRate: expect.any(Object),
            totalTaxAmount: expect.any(Object),
            prepaidTaxAmount: expect.any(Object),
            totalAmountWithTax: expect.any(Object),
            authExpenditureNumber: expect.any(Object),
            allocatedCredits: expect.any(Object),
            committedExpenditures: expect.any(Object),
            availableBalance: expect.any(Object),
            annexDecision: expect.any(Object),
            supplier: expect.any(Object),
            engagement: expect.any(Object),
          }),
        );
      });

      it('passing IPurchaseOrder should create a new form with FormGroup', () => {
        const formGroup = service.createPurchaseOrderFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nameOfTheMinistry: expect.any(Object),
            orderNumber: expect.any(Object),
            orderDate: expect.any(Object),
            totalAmountWithoutTax: expect.any(Object),
            taxRate: expect.any(Object),
            totalTaxAmount: expect.any(Object),
            prepaidTaxAmount: expect.any(Object),
            totalAmountWithTax: expect.any(Object),
            authExpenditureNumber: expect.any(Object),
            allocatedCredits: expect.any(Object),
            committedExpenditures: expect.any(Object),
            availableBalance: expect.any(Object),
            annexDecision: expect.any(Object),
            supplier: expect.any(Object),
            engagement: expect.any(Object),
          }),
        );
      });
    });

    describe('getPurchaseOrder', () => {
      it('should return NewPurchaseOrder for default PurchaseOrder initial value', () => {
        const formGroup = service.createPurchaseOrderFormGroup(sampleWithNewData);

        const purchaseOrder = service.getPurchaseOrder(formGroup) as any;

        expect(purchaseOrder).toMatchObject(sampleWithNewData);
      });

      it('should return NewPurchaseOrder for empty PurchaseOrder initial value', () => {
        const formGroup = service.createPurchaseOrderFormGroup();

        const purchaseOrder = service.getPurchaseOrder(formGroup) as any;

        expect(purchaseOrder).toMatchObject({});
      });

      it('should return IPurchaseOrder', () => {
        const formGroup = service.createPurchaseOrderFormGroup(sampleWithRequiredData);

        const purchaseOrder = service.getPurchaseOrder(formGroup) as any;

        expect(purchaseOrder).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPurchaseOrder should not enable id FormControl', () => {
        const formGroup = service.createPurchaseOrderFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPurchaseOrder should disable id FormControl', () => {
        const formGroup = service.createPurchaseOrderFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
