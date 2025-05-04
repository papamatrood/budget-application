import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IRecipe, NewRecipe } from '../recipe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRecipe for edit and NewRecipeFormGroupInput for create.
 */
type RecipeFormGroupInput = IRecipe | PartialWithRequiredKeyOf<NewRecipe>;

type RecipeFormDefaults = Pick<NewRecipe, 'id' | 'articles'>;

type RecipeFormGroupContent = {
  id: FormControl<IRecipe['id'] | NewRecipe['id']>;
  achievementsInThePastYear: FormControl<IRecipe['achievementsInThePastYear']>;
  newYearForecast: FormControl<IRecipe['newYearForecast']>;
  category: FormControl<IRecipe['category']>;
  financialYear: FormControl<IRecipe['financialYear']>;
  articles: FormControl<IRecipe['articles']>;
};

export type RecipeFormGroup = FormGroup<RecipeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RecipeFormService {
  createRecipeFormGroup(recipe: RecipeFormGroupInput = { id: null }): RecipeFormGroup {
    const recipeRawValue = {
      ...this.getFormDefaults(),
      ...recipe,
    };
    return new FormGroup<RecipeFormGroupContent>({
      id: new FormControl(
        { value: recipeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      achievementsInThePastYear: new FormControl(recipeRawValue.achievementsInThePastYear),
      newYearForecast: new FormControl(recipeRawValue.newYearForecast),
      category: new FormControl(recipeRawValue.category),
      financialYear: new FormControl(recipeRawValue.financialYear),
      articles: new FormControl(recipeRawValue.articles ?? []),
    });
  }

  getRecipe(form: RecipeFormGroup): IRecipe | NewRecipe {
    return form.getRawValue() as IRecipe | NewRecipe;
  }

  resetForm(form: RecipeFormGroup, recipe: RecipeFormGroupInput): void {
    const recipeRawValue = { ...this.getFormDefaults(), ...recipe };
    form.reset(
      {
        ...recipeRawValue,
        id: { value: recipeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RecipeFormDefaults {
    return {
      id: null,
      articles: [],
    };
  }
}
