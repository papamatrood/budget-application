import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/service/financial-year.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';
import { RecipeService } from '../service/recipe.service';
import { IRecipe } from '../recipe.model';
import { RecipeFormGroup, RecipeFormService } from './recipe-form.service';

@Component({
  selector: 'jhi-recipe-update',
  templateUrl: './recipe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RecipeUpdateComponent implements OnInit {
  isSaving = false;
  recipe: IRecipe | null = null;
  financialCategoryEnumValues = Object.keys(FinancialCategoryEnum);

  financialYearsCollection: IFinancialYear[] = [];
  articlesSharedCollection: IArticle[] = [];

  protected recipeService = inject(RecipeService);
  protected recipeFormService = inject(RecipeFormService);
  protected financialYearService = inject(FinancialYearService);
  protected articleService = inject(ArticleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RecipeFormGroup = this.recipeFormService.createRecipeFormGroup();

  compareFinancialYear = (o1: IFinancialYear | null, o2: IFinancialYear | null): boolean =>
    this.financialYearService.compareFinancialYear(o1, o2);

  compareArticle = (o1: IArticle | null, o2: IArticle | null): boolean => this.articleService.compareArticle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recipe }) => {
      this.recipe = recipe;
      if (recipe) {
        this.updateForm(recipe);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recipe = this.recipeFormService.getRecipe(this.editForm);
    if (recipe.id !== null) {
      this.subscribeToSaveResponse(this.recipeService.update(recipe));
    } else {
      this.subscribeToSaveResponse(this.recipeService.create(recipe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecipe>>): void {
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

  protected updateForm(recipe: IRecipe): void {
    this.recipe = recipe;
    this.recipeFormService.resetForm(this.editForm, recipe);

    this.financialYearsCollection = this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(
      this.financialYearsCollection,
      recipe.financialYear,
    );
    this.articlesSharedCollection = this.articleService.addArticleToCollectionIfMissing<IArticle>(
      this.articlesSharedCollection,
      ...(recipe.articles ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.financialYearService
      .query({ 'recipeId.specified': 'false' })
      .pipe(map((res: HttpResponse<IFinancialYear[]>) => res.body ?? []))
      .pipe(
        map((financialYears: IFinancialYear[]) =>
          this.financialYearService.addFinancialYearToCollectionIfMissing<IFinancialYear>(financialYears, this.recipe?.financialYear),
        ),
      )
      .subscribe((financialYears: IFinancialYear[]) => (this.financialYearsCollection = financialYears));

    this.articleService
      .query()
      .pipe(map((res: HttpResponse<IArticle[]>) => res.body ?? []))
      .pipe(
        map((articles: IArticle[]) =>
          this.articleService.addArticleToCollectionIfMissing<IArticle>(articles, ...(this.recipe?.articles ?? [])),
        ),
      )
      .subscribe((articles: IArticle[]) => (this.articlesSharedCollection = articles));
  }
}
