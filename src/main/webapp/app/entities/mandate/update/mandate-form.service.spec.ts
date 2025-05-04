import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../mandate.test-samples';

import { MandateFormService } from './mandate-form.service';

describe('Mandate Form Service', () => {
  let service: MandateFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MandateFormService);
  });

  describe('Service methods', () => {
    describe('createMandateFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMandateFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mandateNumber: expect.any(Object),
            mandateDate: expect.any(Object),
            issueSlipNumber: expect.any(Object),
            monthAndYearOfIssue: expect.any(Object),
            supportingDocuments: expect.any(Object),
            engagement: expect.any(Object),
          }),
        );
      });

      it('passing IMandate should create a new form with FormGroup', () => {
        const formGroup = service.createMandateFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            mandateNumber: expect.any(Object),
            mandateDate: expect.any(Object),
            issueSlipNumber: expect.any(Object),
            monthAndYearOfIssue: expect.any(Object),
            supportingDocuments: expect.any(Object),
            engagement: expect.any(Object),
          }),
        );
      });
    });

    describe('getMandate', () => {
      it('should return NewMandate for default Mandate initial value', () => {
        const formGroup = service.createMandateFormGroup(sampleWithNewData);

        const mandate = service.getMandate(formGroup) as any;

        expect(mandate).toMatchObject(sampleWithNewData);
      });

      it('should return NewMandate for empty Mandate initial value', () => {
        const formGroup = service.createMandateFormGroup();

        const mandate = service.getMandate(formGroup) as any;

        expect(mandate).toMatchObject({});
      });

      it('should return IMandate', () => {
        const formGroup = service.createMandateFormGroup(sampleWithRequiredData);

        const mandate = service.getMandate(formGroup) as any;

        expect(mandate).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMandate should not enable id FormControl', () => {
        const formGroup = service.createMandateFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMandate should disable id FormControl', () => {
        const formGroup = service.createMandateFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
