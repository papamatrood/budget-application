import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAppUser, NewAppUser } from '../app-user.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppUser for edit and NewAppUserFormGroupInput for create.
 */
type AppUserFormGroupInput = IAppUser | PartialWithRequiredKeyOf<NewAppUser>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAppUser | NewAppUser> = Omit<T, 'lastDateUpdate' | 'dateCreated' | 'birthDate'> & {
  lastDateUpdate?: string | null;
  dateCreated?: string | null;
  birthDate?: string | null;
};

type AppUserFormRawValue = FormValueOf<IAppUser>;

type NewAppUserFormRawValue = FormValueOf<NewAppUser>;

type AppUserFormDefaults = Pick<NewAppUser, 'id' | 'accountStatus' | 'lastDateUpdate' | 'dateCreated' | 'birthDate'>;

type AppUserFormGroupContent = {
  id: FormControl<AppUserFormRawValue['id'] | NewAppUser['id']>;
  accountStatus: FormControl<AppUserFormRawValue['accountStatus']>;
  lastDateUpdate: FormControl<AppUserFormRawValue['lastDateUpdate']>;
  dateCreated: FormControl<AppUserFormRawValue['dateCreated']>;
  firstname: FormControl<AppUserFormRawValue['firstname']>;
  lastname: FormControl<AppUserFormRawValue['lastname']>;
  phoneNumber: FormControl<AppUserFormRawValue['phoneNumber']>;
  birthDate: FormControl<AppUserFormRawValue['birthDate']>;
  birthPlace: FormControl<AppUserFormRawValue['birthPlace']>;
  gender: FormControl<AppUserFormRawValue['gender']>;
  familySituation: FormControl<AppUserFormRawValue['familySituation']>;
  position: FormControl<AppUserFormRawValue['position']>;
  address: FormControl<AppUserFormRawValue['address']>;
  user: FormControl<AppUserFormRawValue['user']>;
};

export type AppUserFormGroup = FormGroup<AppUserFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppUserFormService {
  createAppUserFormGroup(appUser: AppUserFormGroupInput = { id: null }): AppUserFormGroup {
    const appUserRawValue = this.convertAppUserToAppUserRawValue({
      ...this.getFormDefaults(),
      ...appUser,
    });
    return new FormGroup<AppUserFormGroupContent>({
      id: new FormControl(
        { value: appUserRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accountStatus: new FormControl(appUserRawValue.accountStatus),
      lastDateUpdate: new FormControl(appUserRawValue.lastDateUpdate),
      dateCreated: new FormControl(appUserRawValue.dateCreated),
      firstname: new FormControl(appUserRawValue.firstname),
      lastname: new FormControl(appUserRawValue.lastname),
      phoneNumber: new FormControl(appUserRawValue.phoneNumber),
      birthDate: new FormControl(appUserRawValue.birthDate),
      birthPlace: new FormControl(appUserRawValue.birthPlace),
      gender: new FormControl(appUserRawValue.gender),
      familySituation: new FormControl(appUserRawValue.familySituation),
      position: new FormControl(appUserRawValue.position),
      address: new FormControl(appUserRawValue.address),
      user: new FormControl(appUserRawValue.user),
    });
  }

  getAppUser(form: AppUserFormGroup): IAppUser | NewAppUser {
    return this.convertAppUserRawValueToAppUser(form.getRawValue() as AppUserFormRawValue | NewAppUserFormRawValue);
  }

  resetForm(form: AppUserFormGroup, appUser: AppUserFormGroupInput): void {
    const appUserRawValue = this.convertAppUserToAppUserRawValue({ ...this.getFormDefaults(), ...appUser });
    form.reset(
      {
        ...appUserRawValue,
        id: { value: appUserRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AppUserFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      accountStatus: false,
      lastDateUpdate: currentTime,
      dateCreated: currentTime,
      birthDate: currentTime,
    };
  }

  private convertAppUserRawValueToAppUser(rawAppUser: AppUserFormRawValue | NewAppUserFormRawValue): IAppUser | NewAppUser {
    return {
      ...rawAppUser,
      lastDateUpdate: dayjs(rawAppUser.lastDateUpdate, DATE_TIME_FORMAT),
      dateCreated: dayjs(rawAppUser.dateCreated, DATE_TIME_FORMAT),
      birthDate: dayjs(rawAppUser.birthDate, DATE_TIME_FORMAT),
    };
  }

  private convertAppUserToAppUserRawValue(
    appUser: IAppUser | (Partial<NewAppUser> & AppUserFormDefaults),
  ): AppUserFormRawValue | PartialWithRequiredKeyOf<NewAppUserFormRawValue> {
    return {
      ...appUser,
      lastDateUpdate: appUser.lastDateUpdate ? appUser.lastDateUpdate.format(DATE_TIME_FORMAT) : undefined,
      dateCreated: appUser.dateCreated ? appUser.dateCreated.format(DATE_TIME_FORMAT) : undefined,
      birthDate: appUser.birthDate ? appUser.birthDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
