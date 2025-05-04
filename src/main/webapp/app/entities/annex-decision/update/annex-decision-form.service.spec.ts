import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../annex-decision.test-samples';

import { AnnexDecisionFormService } from './annex-decision-form.service';

describe('AnnexDecision Form Service', () => {
  let service: AnnexDecisionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnnexDecisionFormService);
  });

  describe('Service methods', () => {
    describe('createAnnexDecisionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAnnexDecisionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            expenseAmount: expect.any(Object),
            creditsAlreadyOpen: expect.any(Object),
            creditsOpen: expect.any(Object),
            financialYear: expect.any(Object),
          }),
        );
      });

      it('passing IAnnexDecision should create a new form with FormGroup', () => {
        const formGroup = service.createAnnexDecisionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            designation: expect.any(Object),
            expenseAmount: expect.any(Object),
            creditsAlreadyOpen: expect.any(Object),
            creditsOpen: expect.any(Object),
            financialYear: expect.any(Object),
          }),
        );
      });
    });

    describe('getAnnexDecision', () => {
      it('should return NewAnnexDecision for default AnnexDecision initial value', () => {
        const formGroup = service.createAnnexDecisionFormGroup(sampleWithNewData);

        const annexDecision = service.getAnnexDecision(formGroup) as any;

        expect(annexDecision).toMatchObject(sampleWithNewData);
      });

      it('should return NewAnnexDecision for empty AnnexDecision initial value', () => {
        const formGroup = service.createAnnexDecisionFormGroup();

        const annexDecision = service.getAnnexDecision(formGroup) as any;

        expect(annexDecision).toMatchObject({});
      });

      it('should return IAnnexDecision', () => {
        const formGroup = service.createAnnexDecisionFormGroup(sampleWithRequiredData);

        const annexDecision = service.getAnnexDecision(formGroup) as any;

        expect(annexDecision).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAnnexDecision should not enable id FormControl', () => {
        const formGroup = service.createAnnexDecisionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAnnexDecision should disable id FormControl', () => {
        const formGroup = service.createAnnexDecisionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
