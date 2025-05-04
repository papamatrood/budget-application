import { Component, OnInit, inject } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize } from "rxjs/operators";

import SharedModule from "app/shared/shared.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { SubTitleService } from "../service/sub-title.service";
import { ISubTitle } from "app/entities/sub-title/sub-title.model";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import {
  ITEM_CREATED_EVENT,
  ITEM_UPDATED_EVENT,
} from "app/config/navigation.constants";
import {
  SubTitleFormGroup,
  SubTitleFormService,
} from "app/entities/sub-title/update/sub-title-form.service";
import { EventManager } from "app/core/util/event-manager.service";

@Component({
  selector: "jhi-sub-title-update",
  templateUrl: "./sub-title-update-modal.component.html",
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubTitleUpdateComponent implements OnInit {
  isSaving = false;
  subTitle: ISubTitle | null = null;

  protected subTitleService = inject(SubTitleService);
  protected subTitleFormService = inject(SubTitleFormService);
  protected activatedRoute = inject(ActivatedRoute);
  protected activeModal = inject(NgbActiveModal);
  protected eventManager = inject(EventManager);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubTitleFormGroup =
    this.subTitleFormService.createSubTitleFormGroup();

  ngOnInit(): void {
    if (this.subTitle) {
      this.updateForm(this.subTitle);
    }
    // this.activatedRoute.data.subscribe(({ subTitle }) => {
    //   this.subTitle = subTitle;
    //   if (subTitle) {
    //     this.updateForm(subTitle);
    //   }
    // });
  }

  // previousState(): void {
  //   window.history.back();
  // }

  previousState(): void {
    this.activeModal.dismiss();
  }

  save(): void {
    this.isSaving = true;
    const subTitle = this.subTitleFormService.getSubTitle(this.editForm);
    if (subTitle.id !== null) {
      this.subscribeToSaveResponse(this.subTitleService.update(subTitle));
    } else {
      this.subscribeToSaveResponse(this.subTitleService.create(subTitle));
    }
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<ISubTitle>>,
  ): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  // protected onSaveSuccess(): void {
  //   this.previousState();
  // }

  protected onSaveSuccess(): void {
    const isUpdate = !!this.editForm.get("id")?.value;
    const event = isUpdate ? ITEM_UPDATED_EVENT : ITEM_CREATED_EVENT;

    // Message personnalisé
    const alertMessage = isUpdate
      ? `Le sous-titre "${this.editForm.get("designation")?.value}" a été mis à jour avec succès`
      : `Le sous-titre "${this.editForm.get("designation")?.value}" a été créé avec succès`;

    // Diffusez l'événement avec le message personnalisé
    this.eventManager.broadcast({
      name: "subTitleListModification",
      content: {
        type: "success",
        message: alertMessage,
        params: { id: this.editForm.get("id")?.value },
      },
    });

    this.activeModal.close(event);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(subTitle: ISubTitle): void {
    this.subTitle = subTitle;
    this.subTitleFormService.resetForm(this.editForm, subTitle);
  }
}
