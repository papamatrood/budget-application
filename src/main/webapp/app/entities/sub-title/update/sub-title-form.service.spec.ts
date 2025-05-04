import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sub-title.test-samples';

import { SubTitleFormService } from './sub-title-form.service';

describe('SubTitle Form Service', () => {
  let service: SubTitleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubTitleFormService);
  });

  describe('Service methods', () => {
    describe('createSubTitleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubTitleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            designation: expect.any(Object),
          }),
        );
      });

      it('passing ISubTitle should create a new form with FormGroup', () => {
        const formGroup = service.createSubTitleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            designation: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubTitle', () => {
      it('should return NewSubTitle for default SubTitle initial value', () => {
        const formGroup = service.createSubTitleFormGroup(sampleWithNewData);

        const subTitle = service.getSubTitle(formGroup) as any;

        expect(subTitle).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubTitle for empty SubTitle initial value', () => {
        const formGroup = service.createSubTitleFormGroup();

        const subTitle = service.getSubTitle(formGroup) as any;

        expect(subTitle).toMatchObject({});
      });

      it('should return ISubTitle', () => {
        const formGroup = service.createSubTitleFormGroup(sampleWithRequiredData);

        const subTitle = service.getSubTitle(formGroup) as any;

        expect(subTitle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubTitle should not enable id FormControl', () => {
        const formGroup = service.createSubTitleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubTitle should disable id FormControl', () => {
        const formGroup = service.createSubTitleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
