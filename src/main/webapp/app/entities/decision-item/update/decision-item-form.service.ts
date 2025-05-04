import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IDecisionItem, NewDecisionItem } from '../decision-item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDecisionItem for edit and NewDecisionItemFormGroupInput for create.
 */
type DecisionItemFormGroupInput = IDecisionItem | PartialWithRequiredKeyOf<NewDecisionItem>;

type DecisionItemFormDefaults = Pick<NewDecisionItem, 'id'>;

type DecisionItemFormGroupContent = {
  id: FormControl<IDecisionItem['id'] | NewDecisionItem['id']>;
  beneficiary: FormControl<IDecisionItem['beneficiary']>;
  amount: FormControl<IDecisionItem['amount']>;
  observation: FormControl<IDecisionItem['observation']>;
  decision: FormControl<IDecisionItem['decision']>;
};

export type DecisionItemFormGroup = FormGroup<DecisionItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DecisionItemFormService {
  createDecisionItemFormGroup(decisionItem: DecisionItemFormGroupInput = { id: null }): DecisionItemFormGroup {
    const decisionItemRawValue = {
      ...this.getFormDefaults(),
      ...decisionItem,
    };
    return new FormGroup<DecisionItemFormGroupContent>({
      id: new FormControl(
        { value: decisionItemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      beneficiary: new FormControl(decisionItemRawValue.beneficiary, {
        validators: [Validators.required],
      }),
      amount: new FormControl(decisionItemRawValue.amount),
      observation: new FormControl(decisionItemRawValue.observation),
      decision: new FormControl(decisionItemRawValue.decision),
    });
  }

  getDecisionItem(form: DecisionItemFormGroup): IDecisionItem | NewDecisionItem {
    return form.getRawValue() as IDecisionItem | NewDecisionItem;
  }

  resetForm(form: DecisionItemFormGroup, decisionItem: DecisionItemFormGroupInput): void {
    const decisionItemRawValue = { ...this.getFormDefaults(), ...decisionItem };
    form.reset(
      {
        ...decisionItemRawValue,
        id: { value: decisionItemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DecisionItemFormDefaults {
    return {
      id: null,
    };
  }
}
