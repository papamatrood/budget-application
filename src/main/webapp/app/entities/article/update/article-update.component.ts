import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IChapter } from 'app/entities/chapter/chapter.model';
import { ChapterService } from 'app/entities/chapter/service/chapter.service';
import { IRecipe } from 'app/entities/recipe/recipe.model';
import { RecipeService } from 'app/entities/recipe/service/recipe.service';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';
import { ArticleService } from '../service/article.service';
import { IArticle } from '../article.model';
import { ArticleFormGroup, ArticleFormService } from './article-form.service';

@Component({
  selector: 'jhi-article-update',
  templateUrl: './article-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ArticleUpdateComponent implements OnInit {
  isSaving = false;
  article: IArticle | null = null;
  financialCategoryEnumValues = Object.keys(FinancialCategoryEnum);

  chaptersSharedCollection: IChapter[] = [];
  recipesSharedCollection: IRecipe[] = [];
  expensesSharedCollection: IExpense[] = [];

  protected articleService = inject(ArticleService);
  protected articleFormService = inject(ArticleFormService);
  protected chapterService = inject(ChapterService);
  protected recipeService = inject(RecipeService);
  protected expenseService = inject(ExpenseService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ArticleFormGroup = this.articleFormService.createArticleFormGroup();

  compareChapter = (o1: IChapter | null, o2: IChapter | null): boolean => this.chapterService.compareChapter(o1, o2);

  compareRecipe = (o1: IRecipe | null, o2: IRecipe | null): boolean => this.recipeService.compareRecipe(o1, o2);

  compareExpense = (o1: IExpense | null, o2: IExpense | null): boolean => this.expenseService.compareExpense(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ article }) => {
      this.article = article;
      if (article) {
        this.updateForm(article);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const article = this.articleFormService.getArticle(this.editForm);
    if (article.id !== null) {
      this.subscribeToSaveResponse(this.articleService.update(article));
    } else {
      this.subscribeToSaveResponse(this.articleService.create(article));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IArticle>>): void {
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

  protected updateForm(article: IArticle): void {
    this.article = article;
    this.articleFormService.resetForm(this.editForm, article);

    this.chaptersSharedCollection = this.chapterService.addChapterToCollectionIfMissing<IChapter>(
      this.chaptersSharedCollection,
      article.chapter,
    );
    this.recipesSharedCollection = this.recipeService.addRecipeToCollectionIfMissing<IRecipe>(
      this.recipesSharedCollection,
      ...(article.recipes ?? []),
    );
    this.expensesSharedCollection = this.expenseService.addExpenseToCollectionIfMissing<IExpense>(
      this.expensesSharedCollection,
      ...(article.expenses ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.chapterService
      .query()
      .pipe(map((res: HttpResponse<IChapter[]>) => res.body ?? []))
      .pipe(map((chapters: IChapter[]) => this.chapterService.addChapterToCollectionIfMissing<IChapter>(chapters, this.article?.chapter)))
      .subscribe((chapters: IChapter[]) => (this.chaptersSharedCollection = chapters));

    this.recipeService
      .query()
      .pipe(map((res: HttpResponse<IRecipe[]>) => res.body ?? []))
      .pipe(
        map((recipes: IRecipe[]) => this.recipeService.addRecipeToCollectionIfMissing<IRecipe>(recipes, ...(this.article?.recipes ?? []))),
      )
      .subscribe((recipes: IRecipe[]) => (this.recipesSharedCollection = recipes));

    this.expenseService
      .query()
      .pipe(map((res: HttpResponse<IExpense[]>) => res.body ?? []))
      .pipe(
        map((expenses: IExpense[]) =>
          this.expenseService.addExpenseToCollectionIfMissing<IExpense>(expenses, ...(this.article?.expenses ?? [])),
        ),
      )
      .subscribe((expenses: IExpense[]) => (this.expensesSharedCollection = expenses));
  }
}
