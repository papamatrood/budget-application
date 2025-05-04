import { Component, OnInit, inject } from "@angular/core";
import { HttpResponse } from "@angular/common/http";
import { ActivatedRoute } from "@angular/router";
import { Observable } from "rxjs";
import { finalize, map } from "rxjs/operators";

import SharedModule from "app/shared/shared.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

import { ISubTitle } from "app/entities/sub-title/sub-title.model";
import { SubTitleService } from "app/entities/sub-title/service/sub-title.service";
import { IChapter } from "app/entities/chapter/chapter.model";
import {
  ChapterFormGroup,
  ChapterFormService,
} from "app/entities/chapter/update/chapter-form.service";
import { ChapterService } from "app/entities/chapter/service/chapter.service";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";
import {
  ITEM_CREATED_EVENT,
  ITEM_UPDATED_EVENT,
} from "app/config/navigation.constants";

@Component({
  selector: "jhi-chapter-update",
  templateUrl: "./chapter-update-modal.component.html",
  styleUrl: "./chapter-update-modal.component.css",
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChapterUpdateComponent implements OnInit {
  isSaving = false;
  chapter: IChapter | null = null;
  // selectedSubTitleCode: string | null = null;

  subTitlesSharedCollection: ISubTitle[] = [];

  protected chapterService = inject(ChapterService);
  protected chapterFormService = inject(ChapterFormService);
  protected subTitleService = inject(SubTitleService);
  protected activatedRoute = inject(ActivatedRoute);
  protected activeModal = inject(NgbActiveModal);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChapterFormGroup = this.chapterFormService.createChapterFormGroup();

  compareSubTitle = (o1: ISubTitle | null, o2: ISubTitle | null): boolean =>
    this.subTitleService.compareSubTitle(o1, o2);

  ngOnInit(): void {
    if (this.chapter) {
      this.updateForm(this.chapter);
      // this.selectedSubTitleCode = this.chapter.subTitle?.code ?? '';
    }
    this.loadRelationshipsOptions();

    // Écoute les changements de sélection du subTitle
    // this.editForm.get('subTitle')?.valueChanges.subscribe((subTitle: ISubTitle | null | undefined) => {
    //   if (subTitle) {
    //     this.selectedSubTitleCode = subTitle.code ?? '';
    //     // Si en mode édition et que le code existe déjà, on split le code
    //     if (this.chapter?.code?.startsWith(this.selectedSubTitleCode)) {
    //       const chapterCodePart = this.chapter.code.substring(this.selectedSubTitleCode.length);
    //       this.editForm.get('code')?.setValue(chapterCodePart);
    //     }
    //   } else {
    //     this.selectedSubTitleCode = '';
    //   }
    //   this.editForm.get('code')?.updateValueAndValidity();
    // });

    // Dans ngOnInit()
    this.editForm.get("code")?.valueChanges.subscribe((value) => {
      if (value && !/^\d*$/.test(value)) {
        this.editForm.get("code")?.setValue(value.replace(/[^\d]/g, ""));
      }
    });

    // this.activatedRoute.data.subscribe(({ chapter }) => {
    //   this.chapter = chapter;
    //   if (chapter) {
    //     this.updateForm(chapter);
    //   }

    //   this.loadRelationshipsOptions();
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
    const chapter = this.chapterFormService.getChapter(this.editForm);

    if (chapter.id !== null) {
      this.subscribeToSaveResponse(this.chapterService.update(chapter));
    } else {
      this.subscribeToSaveResponse(this.chapterService.create(chapter));
    }
  }

  protected subscribeToSaveResponse(
    result: Observable<HttpResponse<IChapter>>,
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
    // Ferme le modal avec le bon événement selon qu'il s'agit d'une création ou modification
    const event = this.editForm.get("id")?.value
      ? ITEM_UPDATED_EVENT
      : ITEM_CREATED_EVENT;
    this.activeModal.close(event);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(chapter: IChapter): void {
    this.chapter = chapter;
    this.chapterFormService.resetForm(this.editForm, chapter);

    this.subTitlesSharedCollection =
      this.subTitleService.addSubTitleToCollectionIfMissing<ISubTitle>(
        this.subTitlesSharedCollection,
        chapter.subTitle,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.subTitleService
      .query({
        page: 0,
        size: 200,
        sort: ["designation,asc"],
      })
      .pipe(map((res: HttpResponse<ISubTitle[]>) => res.body ?? []))
      .pipe(
        map((subTitles: ISubTitle[]) =>
          this.subTitleService.addSubTitleToCollectionIfMissing<ISubTitle>(
            subTitles,
            this.chapter?.subTitle,
          ),
        ),
      )
      .subscribe(
        (subTitles: ISubTitle[]) =>
          (this.subTitlesSharedCollection = subTitles),
      );
  }
}
