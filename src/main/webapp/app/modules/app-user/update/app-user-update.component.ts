/* eslint-disable prettier/prettier */
import { Component, OnInit, inject, signal } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";

import SharedModule from "app/shared/shared.module";
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";

import { IUser } from "app/entities/user/user.model";
import { UserService } from "app/entities/user/service/user.service";
import { GenderEnum } from "app/entities/enumerations/gender-enum.model";
import { FamilySituationEnum } from "app/entities/enumerations/family-situation-enum.model";
import { IAppUser } from "app/entities/app-user/app-user.model";
import { AppUserService } from "app/entities/app-user/service/app-user.service";
import { AppUserFormGroup, AppUserFormService } from "app/entities/app-user/update/app-user-form.service";
import { ITEM_CREATED_EVENT, ITEM_UPDATED_EVENT } from "app/config/navigation.constants";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import { IUserAppUserDTO, NewUserAppUserDTO } from "app/shared/dto/user-appuser-dto.model";
import { LANGUAGES } from "app/config/language.constants";



const userTemplate = {} as IUserAppUserDTO;

const newUser: IUserAppUserDTO = {
  langKey: "fr",
  activated: true,
} as IUserAppUserDTO;



@Component({
  selector: "jhi-app-user-update",
  templateUrl: "./app-user-update-modal.component.html",
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AppUserUpdateComponent implements OnInit {
  languages = LANGUAGES;
  authorities = signal<string[]>([]);
  isSavingUser = signal(false);

  isSaving = false;
  appUser: IAppUser | null = null;
  userAppUserDTO: IUserAppUserDTO | null = null;
  genderEnumValues = Object.keys(GenderEnum);
  familySituationEnumValues = Object.keys(FamilySituationEnum);

  usersSharedCollection: IUser[] = [];

  public readonly router = inject(Router);
  protected appUserService = inject(AppUserService);
  protected appUserFormService = inject(AppUserFormService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);
  protected activeModal = inject(NgbActiveModal);
  protected formBuilder = inject(FormBuilder);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AppUserFormGroup = this.appUserFormService.createAppUserFormGroup();

  // eslint-disable-next-line @typescript-eslint/member-ordering
  myEditForm = new FormGroup({
    id: new FormControl(userTemplate.id),
    login: new FormControl(userTemplate.login, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern(
          "^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$"
        ),
      ],
    }),
    firstName: new FormControl(userTemplate.firstName, {
      validators: [Validators.maxLength(50)],
    }),
    lastName: new FormControl(userTemplate.lastName, {
      validators: [Validators.maxLength(50)],
    }),
    email: new FormControl(userTemplate.email, {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.minLength(5),
        Validators.maxLength(254),
        Validators.email,
      ],
    }),
    activated: new FormControl(userTemplate.activated ?? true, {
      nonNullable: true,
    }),
    langKey: new FormControl(userTemplate.langKey ?? 'fr', {
      nonNullable: true,
    }),
    authorities: new FormControl(userTemplate.authorities, {
      nonNullable: true,
    }),

    // AppUser fields
    accountStatus: new FormControl(userTemplate.accountStatus ?? true),
    phoneNumber: new FormControl(userTemplate.phoneNumber, {
      validators: [Validators.maxLength(20)],
    }),
    birthDate: new FormControl(userTemplate.birthDate),
    birthPlace: new FormControl(userTemplate.birthPlace, {
      validators: [Validators.maxLength(100)],
    }),
    gender: new FormControl(userTemplate.gender),
    familySituation: new FormControl(userTemplate.familySituation),
    position: new FormControl(userTemplate.position, {
      validators: [Validators.maxLength(100)],
    }),
    address: new FormControl(userTemplate.address, {
      validators: [Validators.maxLength(255)],
    }),
    jhipsterUserId: new FormControl(userTemplate.jhipsterUserId, {
      nonNullable: false,
    }),
  });

  compareUser = (o1: IUser | null, o2: IUser | null): boolean =>
    this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    if (this.appUser) {
      userTemplate.id = this.appUser.id;
      userTemplate.firstName = this.appUser.firstname;
      userTemplate.lastName = this.appUser.lastname;
      userTemplate.activated = this.appUser.user?.activated;
      userTemplate.langKey = this.appUser.user?.langKey;
      userTemplate.accountStatus = this.appUser.accountStatus;
      userTemplate.address = this.appUser.address;
      userTemplate.gender = this.appUser.gender;
      userTemplate.familySituation = this.appUser.familySituation;
      userTemplate.birthDate = this.appUser.birthDate;
      userTemplate.birthPlace = this.appUser.birthPlace;
      userTemplate.phoneNumber = this.appUser.phoneNumber;
      userTemplate.position = this.appUser.position;
      userTemplate.jhipsterUserId = this.appUser.user?.id ?? null;
      if (this.appUser.user?.email) {
        userTemplate.email = this.appUser.user.email;
      }
      if (this.appUser.user?.login) {
        userTemplate.login = this.appUser.user.login;
      }
      if (this.appUser.user?.authorities) {
        userTemplate.authorities = this.appUser.user.authorities;
      }
      this.myEditForm.reset(userTemplate);
    } else {
      this.myEditForm.reset(newUser);
    }
    this.appUserService
      .authorities()
      .subscribe((authorities) => this.authorities.set(authorities));
  }

  previousState(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSavingUser.set(true);
    const user = this.myEditForm.getRawValue();
    user.accountStatus = user.activated;
    if (user.id !== null) {
      this.appUserService.updateUserAppUser(user as IUserAppUserDTO).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.appUserService.createUserAppUser(user as NewUserAppUserDTO).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  protected onSaveSuccess(): void {
    // Ferme le modal avec le bon événement selon qu'il s'agit d'une création ou modification
    const event = this.editForm.get("id")?.value
      ? ITEM_UPDATED_EVENT
      : ITEM_CREATED_EVENT;
    this.isSavingUser.set(false);
    this.activeModal.close(event);
    this.reloadCurrentRoute();
  }

  protected onSaveError(): void {
    this.isSavingUser.set(false);
    // Api for inheritance.
  }

  protected reloadCurrentRoute(): void {
    const currentUrl = this.router.url;
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate([currentUrl]);
    });
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }
}
