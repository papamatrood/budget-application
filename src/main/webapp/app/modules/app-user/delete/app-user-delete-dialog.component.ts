/* eslint-disable prettier/prettier */
import { Component, inject } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import SharedModule from "app/shared/shared.module";
import { ITEM_DELETED_EVENT } from "app/config/navigation.constants";
import { IAppUser } from "app/entities/app-user/app-user.model";
import { AppUserService } from "app/entities/app-user/service/app-user.service";
import { IUserAppUserDTO } from "app/shared/dto/user-appuser-dto.model";

@Component({
  templateUrl: "./app-user-delete-dialog.component.html",
  imports: [SharedModule, FormsModule],
})
export class AppUserDeleteDialogComponent {
  appUser?: IAppUser;
  userAppUserDTO?: IUserAppUserDTO;

  protected appUserService = inject(AppUserService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(userAppUserDTO: IUserAppUserDTO): void {
    this.appUserService.deleteUserAppUser(userAppUserDTO).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
