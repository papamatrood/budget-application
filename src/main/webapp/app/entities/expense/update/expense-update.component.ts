import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/service/financial-year.service';
import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';
import { AnnexDecisionService } from 'app/entities/annex-decision/service/annex-decision.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';
import { ExpenseService } from '../service/expense.service';
import { IExpense } from '../expense.model';
import { ExpenseFormGroup, ExpenseFormService } from './expense-form.service';

@Component({
  selector: 'jhi-expense-update',
  templateUrl: './expense-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ExpenseUpdateComponent implements OnInit {
  isSaving = false;
  expense: IExpense | null = null;
  financialCategoryEnumValues = Object.keys(FinancialCategoryEnum);

  financialYearsCollection: IFinancialYear[] = [];
  annexDecisionsCollection: IAnnexDecision[] = [];
  articlesSharedCollection: IArticle[] = [];

  protected expenseService = inject(ExpenseService);
  protected expenseFormService = inject(ExpenseFormService);
  protected financialYearService = inject(FinancialYearService);
  protected annexDecisionService = inject(AnnexDecisionService);
  protected articleService = inject(ArticleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ExpenseFormGroup = this.expenseFormService.createExpenseFormGroup();

  compareFinancialYear = (o1: IFinancialYear | null, o2: IFinancialYear | null): boolean =>
    this.financialYearService.compareFinancialYear(o1, o2);

  compareAnnexDecision = (o1: IAnnexDecision | null, o2: IAnnexDecision | null): boolean =>
    this.annexDecisionService.compareAnnexDecision(o1, o2);

  compareArticle = (o1: IArticle | null, o2: IArticle | null): boolean => this.articleService.compareArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expense }) => {
      this.expense = expense;
      if (expense) {
        this.updateForm(expense);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expense = this.expenseFormService.getExpense(this.editForm);
    if (expense.id !== null) {
      this.subscribeToSaveResponse(this.expenseService.update(expense));
    } else {
      this.subscribeToSaveResponse(this.expenseService.create(expense));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpense>>): void {
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

  protected updateForm(expense: IExpense): void {
    this.expense = expense;
    this.expenseFormService.resetForm(this.editForm, expense);

    this.financialYearsCollection = this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(
      this.financialYearsCollection,
      expense.financialYear,
    );
    this.annexDecisionsCollection = this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(
      this.annexDecisionsCollection,
      expense.annexDecision,
    );
    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing<IArticle>(
      this.articlesSharedCollection,
      ...(expense.articles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.financialYearService
      .query({ 'expenseId.specified': 'false' })
      .pipe(map((res: HttpResponse<IFinancialYear[]>) => res.body ?? []))
      .pipe(
        map((financialYears: IFinancialYear[]) =>
          this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(financialYears, this.expense?.financialYear),
        ),
      )
      .subscribe((financialYears: IFinancialYear[]) => (this.financialYearsCollection = financialYears));

    this.annexDecisionService
      .query({ 'expenseId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAnnexDecision[]>) => res.body ?? []))
      .pipe(
        map((annexDecisions: IAnnexDecision[]) =>
          this.annexDecisionService.addAnnexDecisionToCollectionIfMissing<IAnnexDecision>(annexDecisions, this.expense?.annexDecision),
        ),
      )
      .subscribe((annexDecisions: IAnnexDecision[]) => (this.annexDecisionsCollection = annexDecisions));

    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(
        map((articles: IArticle[]) =>
          this.articleService.addArticleToCollectionIfMissing<IArticle>(articles, ...(this.expense?.articles ?? [])),
        ),
      )
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }
}
