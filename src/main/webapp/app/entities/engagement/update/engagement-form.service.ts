import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEngagement, NewEngagement } from '../engagement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEngagement for edit and NewEngagementFormGroupInput for create.
 */
type EngagementFormGroupInput = IEngagement | PartialWithRequiredKeyOf<NewEngagement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEngagement | NewEngagement> = Omit<T, 'engagementDate'> & {
  engagementDate?: string | null;
};

type EngagementFormRawValue = FormValueOf<IEngagement>;

type NewEngagementFormRawValue = FormValueOf<NewEngagement>;

type EngagementFormDefaults = Pick<NewEngagement, 'id' | 'engagementDate'>;

type EngagementFormGroupContent = {
  id: FormControl<EngagementFormRawValue['id'] | NewEngagement['id']>;
  engagementNumber: FormControl<EngagementFormRawValue['engagementNumber']>;
  engagementDate: FormControl<EngagementFormRawValue['engagementDate']>;
  objectOfExpense: FormControl<EngagementFormRawValue['objectOfExpense']>;
  notifiedCredits: FormControl<EngagementFormRawValue['notifiedCredits']>;
  creditCommitted: FormControl<EngagementFormRawValue['creditCommitted']>;
  creditsAvailable: FormControl<EngagementFormRawValue['creditsAvailable']>;
  amountProposedCommitment: FormControl<EngagementFormRawValue['amountProposedCommitment']>;
  headDaf: FormControl<EngagementFormRawValue['headDaf']>;
  financialController: FormControl<EngagementFormRawValue['financialController']>;
  generalManager: FormControl<EngagementFormRawValue['generalManager']>;
};

export type EngagementFormGroup = FormGroup<EngagementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EngagementFormService {
  createEngagementFormGroup(engagement: EngagementFormGroupInput = { id: null }): EngagementFormGroup {
    const engagementRawValue = this.convertEngagementToEngagementRawValue({
      ...this.getFormDefaults(),
      ...engagement,
    });
    return new FormGroup<EngagementFormGroupContent>({
      id: new FormControl(
        { value: engagementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      engagementNumber: new FormControl(engagementRawValue.engagementNumber, {
        validators: [Validators.required],
      }),
      engagementDate: new FormControl(engagementRawValue.engagementDate, {
        validators: [Validators.required],
      }),
      objectOfExpense: new FormControl(engagementRawValue.objectOfExpense),
      notifiedCredits: new FormControl(engagementRawValue.notifiedCredits),
      creditCommitted: new FormControl(engagementRawValue.creditCommitted),
      creditsAvailable: new FormControl(engagementRawValue.creditsAvailable),
      amountProposedCommitment: new FormControl(engagementRawValue.amountProposedCommitment),
      headDaf: new FormControl(engagementRawValue.headDaf),
      financialController: new FormControl(engagementRawValue.financialController),
      generalManager: new FormControl(engagementRawValue.generalManager),
    });
  }

  getEngagement(form: EngagementFormGroup): IEngagement | NewEngagement {
    return this.convertEngagementRawValueToEngagement(form.getRawValue() as EngagementFormRawValue | NewEngagementFormRawValue);
  }

  resetForm(form: EngagementFormGroup, engagement: EngagementFormGroupInput): void {
    const engagementRawValue = this.convertEngagementToEngagementRawValue({ ...this.getFormDefaults(), ...engagement });
    form.reset(
      {
        ...engagementRawValue,
        id: { value: engagementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EngagementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      engagementDate: currentTime,
    };
  }

  private convertEngagementRawValueToEngagement(
    rawEngagement: EngagementFormRawValue | NewEngagementFormRawValue,
  ): IEngagement | NewEngagement {
    return {
      ...rawEngagement,
      engagementDate: dayjs(rawEngagement.engagementDate, DATE_TIME_FORMAT),
    };
  }

  private convertEngagementToEngagementRawValue(
    engagement: IEngagement | (Partial<NewEngagement> & EngagementFormDefaults),
  ): EngagementFormRawValue | PartialWithRequiredKeyOf<NewEngagementFormRawValue> {
    return {
      ...engagement,
      engagementDate: engagement.engagementDate ? engagement.engagementDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
