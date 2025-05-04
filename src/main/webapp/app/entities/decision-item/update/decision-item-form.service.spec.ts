import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../decision-item.test-samples';

import { DecisionItemFormService } from './decision-item-form.service';

describe('DecisionItem Form Service', () => {
  let service: DecisionItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DecisionItemFormService);
  });

  describe('Service methods', () => {
    describe('createDecisionItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDecisionItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            beneficiary: expect.any(Object),
            amount: expect.any(Object),
            observation: expect.any(Object),
            decision: expect.any(Object),
          }),
        );
      });

      it('passing IDecisionItem should create a new form with FormGroup', () => {
        const formGroup = service.createDecisionItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            beneficiary: expect.any(Object),
            amount: expect.any(Object),
            observation: expect.any(Object),
            decision: expect.any(Object),
          }),
        );
      });
    });

    describe('getDecisionItem', () => {
      it('should return NewDecisionItem for default DecisionItem initial value', () => {
        const formGroup = service.createDecisionItemFormGroup(sampleWithNewData);

        const decisionItem = service.getDecisionItem(formGroup) as any;

        expect(decisionItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewDecisionItem for empty DecisionItem initial value', () => {
        const formGroup = service.createDecisionItemFormGroup();

        const decisionItem = service.getDecisionItem(formGroup) as any;

        expect(decisionItem).toMatchObject({});
      });

      it('should return IDecisionItem', () => {
        const formGroup = service.createDecisionItemFormGroup(sampleWithRequiredData);

        const decisionItem = service.getDecisionItem(formGroup) as any;

        expect(decisionItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDecisionItem should not enable id FormControl', () => {
        const formGroup = service.createDecisionItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDecisionItem should disable id FormControl', () => {
        const formGroup = service.createDecisionItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
