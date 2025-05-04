import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEngagement } from 'app/entities/engagement/engagement.model';
import { EngagementService } from 'app/entities/engagement/service/engagement.service';
import { IMandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';
import { MandateFormGroup, MandateFormService } from './mandate-form.service';

@Component({
  selector: 'jhi-mandate-update',
  templateUrl: './mandate-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MandateUpdateComponent implements OnInit {
  isSaving = false;
  mandate: IMandate | null = null;

  engagementsCollection: IEngagement[] = [];

  protected mandateService = inject(MandateService);
  protected mandateFormService = inject(MandateFormService);
  protected engagementService = inject(EngagementService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: MandateFormGroup = this.mandateFormService.createMandateFormGroup();

  compareEngagement = (o1: IEngagement | null, o2: IEngagement | null): boolean => this.engagementService.compareEngagement(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mandate }) => {
      this.mandate = mandate;
      if (mandate) {
        this.updateForm(mandate);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mandate = this.mandateFormService.getMandate(this.editForm);
    if (mandate.id !== null) {
      this.subscribeToSaveResponse(this.mandateService.update(mandate));
    } else {
      this.subscribeToSaveResponse(this.mandateService.create(mandate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMandate>>): void {
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

  protected updateForm(mandate: IMandate): void {
    this.mandate = mandate;
    this.mandateFormService.resetForm(this.editForm, mandate);

    this.engagementsCollection = this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(
      this.engagementsCollection,
      mandate.engagement,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.engagementService
      .query({ 'mandateId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEngagement[]>) => res.body ?? []))
      .pipe(
        map((engagements: IEngagement[]) =>
          this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(engagements, this.mandate?.engagement),
        ),
      )
      .subscribe((engagements: IEngagement[]) => (this.engagementsCollection = engagements));
  }
}
