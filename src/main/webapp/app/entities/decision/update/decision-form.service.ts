import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDecision, NewDecision } from '../decision.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDecision for edit and NewDecisionFormGroupInput for create.
 */
type DecisionFormGroupInput = IDecision | PartialWithRequiredKeyOf<NewDecision>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDecision | NewDecision> = Omit<T, 'decisionDate'> & {
  decisionDate?: string | null;
};

type DecisionFormRawValue = FormValueOf<IDecision>;

type NewDecisionFormRawValue = FormValueOf<NewDecision>;

type DecisionFormDefaults = Pick<NewDecision, 'id' | 'decisionDate'>;

type DecisionFormGroupContent = {
  id: FormControl<DecisionFormRawValue['id'] | NewDecision['id']>;
  decisionNumber: FormControl<DecisionFormRawValue['decisionNumber']>;
  decisionDate: FormControl<DecisionFormRawValue['decisionDate']>;
  engagement: FormControl<DecisionFormRawValue['engagement']>;
  annexDecision: FormControl<DecisionFormRawValue['annexDecision']>;
};

export type DecisionFormGroup = FormGroup<DecisionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DecisionFormService {
  createDecisionFormGroup(decision: DecisionFormGroupInput = { id: null }): DecisionFormGroup {
    const decisionRawValue = this.convertDecisionToDecisionRawValue({
      ...this.getFormDefaults(),
      ...decision,
    });
    return new FormGroup<DecisionFormGroupContent>({
      id: new FormControl(
        { value: decisionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      decisionNumber: new FormControl(decisionRawValue.decisionNumber, {
        validators: [Validators.required],
      }),
      decisionDate: new FormControl(decisionRawValue.decisionDate, {
        validators: [Validators.required],
      }),
      engagement: new FormControl(decisionRawValue.engagement),
      annexDecision: new FormControl(decisionRawValue.annexDecision),
    });
  }

  getDecision(form: DecisionFormGroup): IDecision | NewDecision {
    return this.convertDecisionRawValueToDecision(form.getRawValue() as DecisionFormRawValue | NewDecisionFormRawValue);
  }

  resetForm(form: DecisionFormGroup, decision: DecisionFormGroupInput): void {
    const decisionRawValue = this.convertDecisionToDecisionRawValue({ ...this.getFormDefaults(), ...decision });
    form.reset(
      {
        ...decisionRawValue,
        id: { value: decisionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DecisionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      decisionDate: currentTime,
    };
  }

  private convertDecisionRawValueToDecision(rawDecision: DecisionFormRawValue | NewDecisionFormRawValue): IDecision | NewDecision {
    return {
      ...rawDecision,
      decisionDate: dayjs(rawDecision.decisionDate, DATE_TIME_FORMAT),
    };
  }

  private convertDecisionToDecisionRawValue(
    decision: IDecision | (Partial<NewDecision> & DecisionFormDefaults),
  ): DecisionFormRawValue | PartialWithRequiredKeyOf<NewDecisionFormRawValue> {
    return {
      ...decision,
      decisionDate: decision.decisionDate ? decision.decisionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
