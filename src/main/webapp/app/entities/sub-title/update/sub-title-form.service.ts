import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISubTitle, NewSubTitle } from '../sub-title.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubTitle for edit and NewSubTitleFormGroupInput for create.
 */
type SubTitleFormGroupInput = ISubTitle | PartialWithRequiredKeyOf<NewSubTitle>;

type SubTitleFormDefaults = Pick<NewSubTitle, 'id'>;

type SubTitleFormGroupContent = {
  id: FormControl<ISubTitle['id'] | NewSubTitle['id']>;
  code: FormControl<ISubTitle['code']>;
  designation: FormControl<ISubTitle['designation']>;
};

export type SubTitleFormGroup = FormGroup<SubTitleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubTitleFormService {
  createSubTitleFormGroup(subTitle: SubTitleFormGroupInput = { id: null }): SubTitleFormGroup {
    const subTitleRawValue = {
      ...this.getFormDefaults(),
      ...subTitle,
    };
    return new FormGroup<SubTitleFormGroupContent>({
      id: new FormControl(
        { value: subTitleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(subTitleRawValue.code, {
        validators: [Validators.required],
      }),
      designation: new FormControl(subTitleRawValue.designation, {
        validators: [Validators.required],
      }),
    });
  }

  getSubTitle(form: SubTitleFormGroup): ISubTitle | NewSubTitle {
    return form.getRawValue() as ISubTitle | NewSubTitle;
  }

  resetForm(form: SubTitleFormGroup, subTitle: SubTitleFormGroupInput): void {
    const subTitleRawValue = { ...this.getFormDefaults(), ...subTitle };
    form.reset(
      {
        ...subTitleRawValue,
        id: { value: subTitleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubTitleFormDefaults {
    return {
      id: null,
    };
  }
}
