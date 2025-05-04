import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEngagement } from '../engagement.model';
import { EngagementService } from '../service/engagement.service';
import { EngagementFormGroup, EngagementFormService } from './engagement-form.service';

@Component({
  selector: 'jhi-engagement-update',
  templateUrl: './engagement-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EngagementUpdateComponent implements OnInit {
  isSaving = false;
  engagement: IEngagement | null = null;

  protected engagementService = inject(EngagementService);
  protected engagementFormService = inject(EngagementFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EngagementFormGroup = this.engagementFormService.createEngagementFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ engagement }) => {
      this.engagement = engagement;
      if (engagement) {
        this.updateForm(engagement);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const engagement = this.engagementFormService.getEngagement(this.editForm);
    if (engagement.id !== null) {
      this.subscribeToSaveResponse(this.engagementService.update(engagement));
    } else {
      this.subscribeToSaveResponse(this.engagementService.create(engagement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEngagement>>): void {
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

  protected updateForm(engagement: IEngagement): void {
    this.engagement = engagement;
    this.engagementFormService.resetForm(this.editForm, engagement);
  }
}
