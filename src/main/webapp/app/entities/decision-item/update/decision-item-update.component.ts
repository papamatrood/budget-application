import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDecision } from 'app/entities/decision/decision.model';
import { DecisionService } from 'app/entities/decision/service/decision.service';
import { IDecisionItem } from '../decision-item.model';
import { DecisionItemService } from '../service/decision-item.service';
import { DecisionItemFormGroup, DecisionItemFormService } from './decision-item-form.service';

@Component({
  selector: 'jhi-decision-item-update',
  templateUrl: './decision-item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DecisionItemUpdateComponent implements OnInit {
  isSaving = false;
  decisionItem: IDecisionItem | null = null;

  decisionsSharedCollection: IDecision[] = [];

  protected decisionItemService = inject(DecisionItemService);
  protected decisionItemFormService = inject(DecisionItemFormService);
  protected decisionService = inject(DecisionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DecisionItemFormGroup = this.decisionItemFormService.createDecisionItemFormGroup();

  compareDecision = (o1: IDecision | null, o2: IDecision | null): boolean => this.decisionService.compareDecision(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ decisionItem }) => {
      this.decisionItem = decisionItem;
      if (decisionItem) {
        this.updateForm(decisionItem);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const decisionItem = this.decisionItemFormService.getDecisionItem(this.editForm);
    if (decisionItem.id !== null) {
      this.subscribeToSaveResponse(this.decisionItemService.update(decisionItem));
    } else {
      this.subscribeToSaveResponse(this.decisionItemService.create(decisionItem));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDecisionItem>>): void {
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

  protected updateForm(decisionItem: IDecisionItem): void {
    this.decisionItem = decisionItem;
    this.decisionItemFormService.resetForm(this.editForm, decisionItem);

    this.decisionsSharedCollection = this.decisionService.addDecisionToCollectionIfMissing<IDecision>(
      this.decisionsSharedCollection,
      decisionItem.decision,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.decisionService
      .query()
      .pipe(map((res: HttpResponse<IDecision[]>) => res.body ?? []))
      .pipe(
        map((decisions: IDecision[]) =>
          this.decisionService.addDecisionToCollectionIfMissing<IDecision>(decisions, this.decisionItem?.decision),
        ),
      )
      .subscribe((decisions: IDecision[]) => (this.decisionsSharedCollection = decisions));
  }
}
