import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEngagement } from 'app/entities/engagement/engagement.model';
import { EngagementService } from 'app/entities/engagement/service/engagement.service';
import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';
import { AnnexDecisionService } from 'app/entities/annex-decision/service/annex-decision.service';
import { DecisionService } from '../service/decision.service';
import { IDecision } from '../decision.model';
import { DecisionFormGroup, DecisionFormService } from './decision-form.service';

@Component({
  selector: 'jhi-decision-update',
  templateUrl: './decision-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DecisionUpdateComponent implements OnInit {
  isSaving = false;
  decision: IDecision | null = null;

  engagementsCollection: IEngagement[] = [];
  annexDecisionsSharedCollection: IAnnexDecision[] = [];

  protected decisionService = inject(DecisionService);
  protected decisionFormService = inject(DecisionFormService);
  protected engagementService = inject(EngagementService);
  protected annexDecisionService = inject(AnnexDecisionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DecisionFormGroup = this.decisionFormService.createDecisionFormGroup();

  compareEngagement = (o1: IEngagement | null, o2: IEngagement | null): boolean => this.engagementService.compareEngagement(o1, o2);

  compareAnnexDecision = (o1: IAnnexDecision | null, o2: IAnnexDecision | null): boolean =>
    this.annexDecisionService.compareAnnexDecision(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decision }) => {
      this.decision = decision;
      if (decision) {
        this.updateForm(decision);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decision = this.decisionFormService.getDecision(this.editForm);
    if (decision.id !== null) {
      this.subscribeToSaveResponse(this.decisionService.update(decision));
    } else {
      this.subscribeToSaveResponse(this.decisionService.create(decision));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecision>>): void {
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

  protected updateForm(decision: IDecision): void {
    this.decision = decision;
    this.decisionFormService.resetForm(this.editForm, decision);

    this.engagementsCollection = this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(
      this.engagementsCollection,
      decision.engagement,
    );
    this.annexDecisionsSharedCollection = this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(
      this.annexDecisionsSharedCollection,
      decision.annexDecision,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.engagementService
      .query({ 'decisionId.specified': 'false' })
      .pipe(map((res: HttpResponse<IEngagement[]>) => res.body ?? []))
      .pipe(
        map((engagements: IEngagement[]) =>
          this.engagementService.addEngagementToCollectionIfMissing<IEngagement>(engagements, this.decision?.engagement),
        ),
      )
      .subscribe((engagements: IEngagement[]) => (this.engagementsCollection = engagements));

    this.annexDecisionService
      .query()
      .pipe(map((res: HttpResponse<IAnnexDecision[]>) => res.body ?? []))
      .pipe(
        map((annexDecisions: IAnnexDecision[]) =>
          this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(annexDecisions, this.decision?.annexDecision),
        ),
      )
      .subscribe((annexDecisions: IAnnexDecision[]) => (this.annexDecisionsSharedCollection = annexDecisions));
  }
}
