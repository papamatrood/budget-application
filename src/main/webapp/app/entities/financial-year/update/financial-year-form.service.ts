import { Injectable } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";

import { IFinancialYear, NewFinancialYear } from "../financial-year.model";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<
  Omit<T, "id">
> & { id: T["id"] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFinancialYear for edit and NewFinancialYearFormGroupInput for create.
 */
type FinancialYearFormGroupInput =
  | IFinancialYear
  | PartialWithRequiredKeyOf<NewFinancialYear>;

type FinancialYearFormDefaults = Pick<NewFinancialYear, "id">;

type FinancialYearFormGroupContent = {
  id: FormControl<IFinancialYear["id"] | NewFinancialYear["id"]>;
  theYear: FormControl<IFinancialYear["theYear"]>;
};

export type FinancialYearFormGroup = FormGroup<FinancialYearFormGroupContent>;

@Injectable({ providedIn: "root" })
export class FinancialYearFormService {
  createFinancialYearFormGroup(
    financialYear: FinancialYearFormGroupInput = { id: null },
  ): FinancialYearFormGroup {
    const financialYearRawValue = {
      ...this.getFormDefaults(),
      ...financialYear,
    };
    return new FormGroup<FinancialYearFormGroupContent>({
      id: new FormControl(
        { value: financialYearRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      theYear: new FormControl(financialYearRawValue.theYear, {
        validators: [Validators.required],
      }),
    });
  }

  getFinancialYear(
    form: FinancialYearFormGroup,
  ): IFinancialYear | NewFinancialYear {
    return form.getRawValue() as IFinancialYear | NewFinancialYear;
  }

  resetForm(
    form: FinancialYearFormGroup,
    financialYear: FinancialYearFormGroupInput,
  ): void {
    const financialYearRawValue = {
      ...this.getFormDefaults(),
      ...financialYear,
    };
    form.reset(
      {
        ...financialYearRawValue,
        id: { value: financialYearRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FinancialYearFormDefaults {
    return {
      id: null,
    };
  }
}
