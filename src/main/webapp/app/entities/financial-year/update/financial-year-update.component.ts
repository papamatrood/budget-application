import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialYear } from '../financial-year.model';
import { FinancialYearService } from '../service/financial-year.service';
import { FinancialYearFormGroup, FinancialYearFormService } from './financial-year-form.service';

@Component({
  selector: 'jhi-financial-year-update',
  templateUrl: './financial-year-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FinancialYearUpdateComponent implements OnInit {
  isSaving = false;
  financialYear: IFinancialYear | null = null;

  protected financialYearService = inject(FinancialYearService);
  protected financialYearFormService = inject(FinancialYearFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FinancialYearFormGroup = this.financialYearFormService.createFinancialYearFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ financialYear }) => {
      this.financialYear = financialYear;
      if (financialYear) {
        this.updateForm(financialYear);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const financialYear = this.financialYearFormService.getFinancialYear(this.editForm);
    if (financialYear.id !== null) {
      this.subscribeToSaveResponse(this.financialYearService.update(financialYear));
    } else {
      this.subscribeToSaveResponse(this.financialYearService.create(financialYear));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinancialYear>>): void {
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

  protected updateForm(financialYear: IFinancialYear): void {
    this.financialYear = financialYear;
    this.financialYearFormService.resetForm(this.editForm, financialYear);
  }
}
