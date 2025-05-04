import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../engagement.test-samples';

import { EngagementFormService } from './engagement-form.service';

describe('Engagement Form Service', () => {
  let service: EngagementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EngagementFormService);
  });

  describe('Service methods', () => {
    describe('createEngagementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEngagementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            engagementNumber: expect.any(Object),
            engagementDate: expect.any(Object),
            objectOfExpense: expect.any(Object),
            notifiedCredits: expect.any(Object),
            creditCommitted: expect.any(Object),
            creditsAvailable: expect.any(Object),
            amountProposedCommitment: expect.any(Object),
            headDaf: expect.any(Object),
            financialController: expect.any(Object),
            generalManager: expect.any(Object),
          }),
        );
      });

      it('passing IEngagement should create a new form with FormGroup', () => {
        const formGroup = service.createEngagementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            engagementNumber: expect.any(Object),
            engagementDate: expect.any(Object),
            objectOfExpense: expect.any(Object),
            notifiedCredits: expect.any(Object),
            creditCommitted: expect.any(Object),
            creditsAvailable: expect.any(Object),
            amountProposedCommitment: expect.any(Object),
            headDaf: expect.any(Object),
            financialController: expect.any(Object),
            generalManager: expect.any(Object),
          }),
        );
      });
    });

    describe('getEngagement', () => {
      it('should return NewEngagement for default Engagement initial value', () => {
        const formGroup = service.createEngagementFormGroup(sampleWithNewData);

        const engagement = service.getEngagement(formGroup) as any;

        expect(engagement).toMatchObject(sampleWithNewData);
      });

      it('should return NewEngagement for empty Engagement initial value', () => {
        const formGroup = service.createEngagementFormGroup();

        const engagement = service.getEngagement(formGroup) as any;

        expect(engagement).toMatchObject({});
      });

      it('should return IEngagement', () => {
        const formGroup = service.createEngagementFormGroup(sampleWithRequiredData);

        const engagement = service.getEngagement(formGroup) as any;

        expect(engagement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEngagement should not enable id FormControl', () => {
        const formGroup = service.createEngagementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEngagement should disable id FormControl', () => {
        const formGroup = service.createEngagementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
