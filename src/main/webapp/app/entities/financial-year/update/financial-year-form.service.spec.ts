import { TestBed } from "@angular/core/testing";

import {
  sampleWithNewData,
  sampleWithRequiredData,
} from "../financial-year.test-samples";

import { FinancialYearFormService } from "./financial-year-form.service";

describe("FinancialYear Form Service", () => {
  let service: FinancialYearFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FinancialYearFormService);
  });

  describe("Service methods", () => {
    describe("createFinancialYearFormGroup", () => {
      it("should create a new form with FormControl", () => {
        const formGroup = service.createFinancialYearFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            theYear: expect.any(Object),
          }),
        );
      });

      it("passing IFinancialYear should create a new form with FormGroup", () => {
        const formGroup = service.createFinancialYearFormGroup(
          sampleWithRequiredData,
        );

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            theYear: expect.any(Object),
          }),
        );
      });
    });

    describe("getFinancialYear", () => {
      it("should return NewFinancialYear for default FinancialYear initial value", () => {
        const formGroup =
          service.createFinancialYearFormGroup(sampleWithNewData);

        const financialYear = service.getFinancialYear(formGroup) as any;

        expect(financialYear).toMatchObject(sampleWithNewData);
      });

      it("should return NewFinancialYear for empty FinancialYear initial value", () => {
        const formGroup = service.createFinancialYearFormGroup();

        const financialYear = service.getFinancialYear(formGroup) as any;

        expect(financialYear).toMatchObject({});
      });

      it("should return IFinancialYear", () => {
        const formGroup = service.createFinancialYearFormGroup(
          sampleWithRequiredData,
        );

        const financialYear = service.getFinancialYear(formGroup) as any;

        expect(financialYear).toMatchObject(sampleWithRequiredData);
      });
    });

    describe("resetForm", () => {
      it("passing IFinancialYear should not enable id FormControl", () => {
        const formGroup = service.createFinancialYearFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it("passing NewFinancialYear should disable id FormControl", () => {
        const formGroup = service.createFinancialYearFormGroup(
          sampleWithRequiredData,
        );
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
