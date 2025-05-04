import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/service/financial-year.service';
import { IAnnexDecision } from '../annex-decision.model';
import { AnnexDecisionService } from '../service/annex-decision.service';
import { AnnexDecisionFormGroup, AnnexDecisionFormService } from './annex-decision-form.service';

@Component({
  selector: 'jhi-annex-decision-update',
  templateUrl: './annex-decision-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AnnexDecisionUpdateComponent implements OnInit {
  isSaving = false;
  annexDecision: IAnnexDecision | null = null;

  financialYearsCollection: IFinancialYear[] = [];

  protected annexDecisionService = inject(AnnexDecisionService);
  protected annexDecisionFormService = inject(AnnexDecisionFormService);
  protected financialYearService = inject(FinancialYearService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AnnexDecisionFormGroup = this.annexDecisionFormService.createAnnexDecisionFormGroup();

  compareFinancialYear = (o1: IFinancialYear | null, o2: IFinancialYear | null): boolean =>
    this.financialYearService.compareFinancialYear(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annexDecision }) => {
      this.annexDecision = annexDecision;
      if (annexDecision) {
        this.updateForm(annexDecision);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annexDecision = this.annexDecisionFormService.getAnnexDecision(this.editForm);
    if (annexDecision.id !== null) {
      this.subscribeToSaveResponse(this.annexDecisionService.update(annexDecision));
    } else {
      this.subscribeToSaveResponse(this.annexDecisionService.create(annexDecision));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnexDecision>>): void {
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

  protected updateForm(annexDecision: IAnnexDecision): void {
    this.annexDecision = annexDecision;
    this.annexDecisionFormService.resetForm(this.editForm, annexDecision);

    this.financialYearsCollection = this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(
      this.financialYearsCollection,
      annexDecision.financialYear,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.financialYearService
      .query({ 'annexDecisionId.specified': 'false' })
      .pipe(map((res: HttpResponse<IFinancialYear[]>) => res.body ?? []))
      .pipe(
        map((financialYears: IFinancialYear[]) =>
          this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(
            financialYears,
            this.annexDecision?.financialYear,
          ),
        ),
      )
      .subscribe((financialYears: IFinancialYear[]) => (this.financialYearsCollection = financialYears));
  }
}
