import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubTitle } from '../sub-title.model';
import { SubTitleService } from '../service/sub-title.service';
import { SubTitleFormGroup, SubTitleFormService } from './sub-title-form.service';

@Component({
  selector: 'jhi-sub-title-update',
  templateUrl: './sub-title-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubTitleUpdateComponent implements OnInit {
  isSaving = false;
  subTitle: ISubTitle | null = null;

  protected subTitleService = inject(SubTitleService);
  protected subTitleFormService = inject(SubTitleFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubTitleFormGroup = this.subTitleFormService.createSubTitleFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subTitle }) => {
      this.subTitle = subTitle;
      if (subTitle) {
        this.updateForm(subTitle);
      }
    });
  }

  previousState(): void {
    window.history.back();
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubTitle>>): void {
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

  protected updateForm(subTitle: ISubTitle): void {
    this.subTitle = subTitle;
    this.subTitleFormService.resetForm(this.editForm, subTitle);
  }
}
