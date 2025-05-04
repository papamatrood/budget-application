import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISupplier, NewSupplier } from '../supplier.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISupplier for edit and NewSupplierFormGroupInput for create.
 */
type SupplierFormGroupInput = ISupplier | PartialWithRequiredKeyOf<NewSupplier>;

type SupplierFormDefaults = Pick<NewSupplier, 'id'>;

type SupplierFormGroupContent = {
  id: FormControl<ISupplier['id'] | NewSupplier['id']>;
  companyName: FormControl<ISupplier['companyName']>;
  address: FormControl<ISupplier['address']>;
  phone: FormControl<ISupplier['phone']>;
  nifNumber: FormControl<ISupplier['nifNumber']>;
  commercialRegister: FormControl<ISupplier['commercialRegister']>;
  bankAccount: FormControl<ISupplier['bankAccount']>;
  mandatingEstablishment: FormControl<ISupplier['mandatingEstablishment']>;
  email: FormControl<ISupplier['email']>;
  website: FormControl<ISupplier['website']>;
  description: FormControl<ISupplier['description']>;
  contactFirstname: FormControl<ISupplier['contactFirstname']>;
  contactlastname: FormControl<ISupplier['contactlastname']>;
};

export type SupplierFormGroup = FormGroup<SupplierFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SupplierFormService {
  createSupplierFormGroup(supplier: SupplierFormGroupInput = { id: null }): SupplierFormGroup {
    const supplierRawValue = {
      ...this.getFormDefaults(),
      ...supplier,
    };
    return new FormGroup<SupplierFormGroupContent>({
      id: new FormControl(
        { value: supplierRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      companyName: new FormControl(supplierRawValue.companyName),
      address: new FormControl(supplierRawValue.address),
      phone: new FormControl(supplierRawValue.phone),
      nifNumber: new FormControl(supplierRawValue.nifNumber),
      commercialRegister: new FormControl(supplierRawValue.commercialRegister),
      bankAccount: new FormControl(supplierRawValue.bankAccount),
      mandatingEstablishment: new FormControl(supplierRawValue.mandatingEstablishment),
      email: new FormControl(supplierRawValue.email),
      website: new FormControl(supplierRawValue.website),
      description: new FormControl(supplierRawValue.description),
      contactFirstname: new FormControl(supplierRawValue.contactFirstname),
      contactlastname: new FormControl(supplierRawValue.contactlastname),
    });
  }

  getSupplier(form: SupplierFormGroup): ISupplier | NewSupplier {
    return form.getRawValue() as ISupplier | NewSupplier;
  }

  resetForm(form: SupplierFormGroup, supplier: SupplierFormGroupInput): void {
    const supplierRawValue = { ...this.getFormDefaults(), ...supplier };
    form.reset(
      {
        ...supplierRawValue,
        id: { value: supplierRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SupplierFormDefaults {
    return {
      id: null,
    };
  }
}
