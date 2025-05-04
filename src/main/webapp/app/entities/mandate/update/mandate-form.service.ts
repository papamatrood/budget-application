import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMandate, NewMandate } from '../mandate.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMandate for edit and NewMandateFormGroupInput for create.
 */
type MandateFormGroupInput = IMandate | PartialWithRequiredKeyOf<NewMandate>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMandate | NewMandate> = Omit<T, 'mandateDate'> & {
  mandateDate?: string | null;
};

type MandateFormRawValue = FormValueOf<IMandate>;

type NewMandateFormRawValue = FormValueOf<NewMandate>;

type MandateFormDefaults = Pick<NewMandate, 'id' | 'mandateDate'>;

type MandateFormGroupContent = {
  id: FormControl<MandateFormRawValue['id'] | NewMandate['id']>;
  mandateNumber: FormControl<MandateFormRawValue['mandateNumber']>;
  mandateDate: FormControl<MandateFormRawValue['mandateDate']>;
  issueSlipNumber: FormControl<MandateFormRawValue['issueSlipNumber']>;
  monthAndYearOfIssue: FormControl<MandateFormRawValue['monthAndYearOfIssue']>;
  supportingDocuments: FormControl<MandateFormRawValue['supportingDocuments']>;
  engagement: FormControl<MandateFormRawValue['engagement']>;
};

export type MandateFormGroup = FormGroup<MandateFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MandateFormService {
  createMandateFormGroup(mandate: MandateFormGroupInput = { id: null }): MandateFormGroup {
    const mandateRawValue = this.convertMandateToMandateRawValue({
      ...this.getFormDefaults(),
      ...mandate,
    });
    return new FormGroup<MandateFormGroupContent>({
      id: new FormControl(
        { value: mandateRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      mandateNumber: new FormControl(mandateRawValue.mandateNumber, {
        validators: [Validators.required],
      }),
      mandateDate: new FormControl(mandateRawValue.mandateDate, {
        validators: [Validators.required],
      }),
      issueSlipNumber: new FormControl(mandateRawValue.issueSlipNumber),
      monthAndYearOfIssue: new FormControl(mandateRawValue.monthAndYearOfIssue),
      supportingDocuments: new FormControl(mandateRawValue.supportingDocuments),
      engagement: new FormControl(mandateRawValue.engagement),
    });
  }

  getMandate(form: MandateFormGroup): IMandate | NewMandate {
    return this.convertMandateRawValueToMandate(form.getRawValue() as MandateFormRawValue | NewMandateFormRawValue);
  }

  resetForm(form: MandateFormGroup, mandate: MandateFormGroupInput): void {
    const mandateRawValue = this.convertMandateToMandateRawValue({ ...this.getFormDefaults(), ...mandate });
    form.reset(
      {
        ...mandateRawValue,
        id: { value: mandateRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MandateFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      mandateDate: currentTime,
    };
  }

  private convertMandateRawValueToMandate(rawMandate: MandateFormRawValue | NewMandateFormRawValue): IMandate | NewMandate {
    return {
      ...rawMandate,
      mandateDate: dayjs(rawMandate.mandateDate, DATE_TIME_FORMAT),
    };
  }

  private convertMandateToMandateRawValue(
    mandate: IMandate | (Partial<NewMandate> & MandateFormDefaults),
  ): MandateFormRawValue | PartialWithRequiredKeyOf<NewMandateFormRawValue> {
    return {
      ...mandate,
      mandateDate: mandate.mandateDate ? mandate.mandateDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
