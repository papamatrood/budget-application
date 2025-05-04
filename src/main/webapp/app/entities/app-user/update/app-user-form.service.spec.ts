import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../app-user.test-samples';

import { AppUserFormService } from './app-user-form.service';

describe('AppUser Form Service', () => {
  let service: AppUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppUserFormService);
  });

  describe('Service methods', () => {
    describe('createAppUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountStatus: expect.any(Object),
            lastDateUpdate: expect.any(Object),
            dateCreated: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            phoneNumber: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            gender: expect.any(Object),
            familySituation: expect.any(Object),
            position: expect.any(Object),
            address: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing IAppUser should create a new form with FormGroup', () => {
        const formGroup = service.createAppUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountStatus: expect.any(Object),
            lastDateUpdate: expect.any(Object),
            dateCreated: expect.any(Object),
            firstname: expect.any(Object),
            lastname: expect.any(Object),
            phoneNumber: expect.any(Object),
            birthDate: expect.any(Object),
            birthPlace: expect.any(Object),
            gender: expect.any(Object),
            familySituation: expect.any(Object),
            position: expect.any(Object),
            address: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getAppUser', () => {
      it('should return NewAppUser for default AppUser initial value', () => {
        const formGroup = service.createAppUserFormGroup(sampleWithNewData);

        const appUser = service.getAppUser(formGroup) as any;

        expect(appUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppUser for empty AppUser initial value', () => {
        const formGroup = service.createAppUserFormGroup();

        const appUser = service.getAppUser(formGroup) as any;

        expect(appUser).toMatchObject({});
      });

      it('should return IAppUser', () => {
        const formGroup = service.createAppUserFormGroup(sampleWithRequiredData);

        const appUser = service.getAppUser(formGroup) as any;

        expect(appUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppUser should not enable id FormControl', () => {
        const formGroup = service.createAppUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppUser should disable id FormControl', () => {
        const formGroup = service.createAppUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
