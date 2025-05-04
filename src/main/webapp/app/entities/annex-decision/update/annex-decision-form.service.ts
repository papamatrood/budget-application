import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAnnexDecision, NewAnnexDecision } from '../annex-decision.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnnexDecision for edit and NewAnnexDecisionFormGroupInput for create.
 */
type AnnexDecisionFormGroupInput = IAnnexDecision | PartialWithRequiredKeyOf<NewAnnexDecision>;

type AnnexDecisionFormDefaults = Pick<NewAnnexDecision, 'id'>;

type AnnexDecisionFormGroupContent = {
  id: FormControl<IAnnexDecision['id'] | NewAnnexDecision['id']>;
  designation: FormControl<IAnnexDecision['designation']>;
  expenseAmount: FormControl<IAnnexDecision['expenseAmount']>;
  creditsAlreadyOpen: FormControl<IAnnexDecision['creditsAlreadyOpen']>;
  creditsOpen: FormControl<IAnnexDecision['creditsOpen']>;
  financialYear: FormControl<IAnnexDecision['financialYear']>;
};

export type AnnexDecisionFormGroup = FormGroup<AnnexDecisionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnnexDecisionFormService {
  createAnnexDecisionFormGroup(annexDecision: AnnexDecisionFormGroupInput = { id: null }): AnnexDecisionFormGroup {
    const annexDecisionRawValue = {
      ...this.getFormDefaults(),
      ...annexDecision,
    };
    return new FormGroup<AnnexDecisionFormGroupContent>({
      id: new FormControl(
        { value: annexDecisionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      designation: new FormControl(annexDecisionRawValue.designation),
      expenseAmount: new FormControl(annexDecisionRawValue.expenseAmount),
      creditsAlreadyOpen: new FormControl(annexDecisionRawValue.creditsAlreadyOpen),
      creditsOpen: new FormControl(annexDecisionRawValue.creditsOpen),
      financialYear: new FormControl(annexDecisionRawValue.financialYear),
    });
  }

  getAnnexDecision(form: AnnexDecisionFormGroup): IAnnexDecision | NewAnnexDecision {
    return form.getRawValue() as IAnnexDecision | NewAnnexDecision;
  }

  resetForm(form: AnnexDecisionFormGroup, annexDecision: AnnexDecisionFormGroupInput): void {
    const annexDecisionRawValue = { ...this.getFormDefaults(), ...annexDecision };
    form.reset(
      {
        ...annexDecisionRawValue,
        id: { value: annexDecisionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AnnexDecisionFormDefaults {
    return {
      id: null,
    };
  }
}
