import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubTitle } from 'app/entities/sub-title/sub-title.model';
import { SubTitleService } from 'app/entities/sub-title/service/sub-title.service';
import { IChapter } from '../chapter.model';
import { ChapterService } from '../service/chapter.service';
import { ChapterFormGroup, ChapterFormService } from './chapter-form.service';

@Component({
  selector: 'jhi-chapter-update',
  templateUrl: './chapter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ChapterUpdateComponent implements OnInit {
  isSaving = false;
  chapter: IChapter | null = null;

  subTitlesSharedCollection: ISubTitle[] = [];

  protected chapterService = inject(ChapterService);
  protected chapterFormService = inject(ChapterFormService);
  protected subTitleService = inject(SubTitleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ChapterFormGroup = this.chapterFormService.createChapterFormGroup();

  compareSubTitle = (o1: ISubTitle | null, o2: ISubTitle | null): boolean => this.subTitleService.compareSubTitle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ chapter }) => {
      this.chapter = chapter;
      if (chapter) {
        this.updateForm(chapter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IChapter>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
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

    this.subTitlesSharedCollection = this.subTitleService.addSubTitleToCollectionIfMissing<ISubTitle>(
      this.subTitlesSharedCollection,
      chapter.subTitle,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.subTitleService
      .query()
      .pipe(map((res: HttpResponse<ISubTitle[]>) => res.body ?? []))
      .pipe(
        map((subTitles: ISubTitle[]) =>
          this.subTitleService.addSubTitleToCollectionIfMissing<ISubTitle>(subTitles, this.chapter?.subTitle),
        ),
      )
      .subscribe((subTitles: ISubTitle[]) => (this.subTitlesSharedCollection = subTitles));
  }
}
