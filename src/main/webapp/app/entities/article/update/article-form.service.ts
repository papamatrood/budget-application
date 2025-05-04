import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IArticle, NewArticle } from '../article.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IArticle for edit and NewArticleFormGroupInput for create.
 */
type ArticleFormGroupInput = IArticle | PartialWithRequiredKeyOf<NewArticle>;

type ArticleFormDefaults = Pick<NewArticle, 'id' | 'recipes' | 'expenses'>;

type ArticleFormGroupContent = {
  id: FormControl<IArticle['id'] | NewArticle['id']>;
  category: FormControl<IArticle['category']>;
  code: FormControl<IArticle['code']>;
  designation: FormControl<IArticle['designation']>;
  accountDiv: FormControl<IArticle['accountDiv']>;
  codeEnd: FormControl<IArticle['codeEnd']>;
  paragraph: FormControl<IArticle['paragraph']>;
  chapter: FormControl<IArticle['chapter']>;
  recipes: FormControl<IArticle['recipes']>;
  expenses: FormControl<IArticle['expenses']>;
};

export type ArticleFormGroup = FormGroup<ArticleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ArticleFormService {
  createArticleFormGroup(article: ArticleFormGroupInput = { id: null }): ArticleFormGroup {
    const articleRawValue = {
      ...this.getFormDefaults(),
      ...article,
    };
    return new FormGroup<ArticleFormGroupContent>({
      id: new FormControl(
        { value: articleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      category: new FormControl(articleRawValue.category),
      code: new FormControl(articleRawValue.code, {
        validators: [Validators.required],
      }),
      designation: new FormControl(articleRawValue.designation, {
        validators: [Validators.required],
      }),
      accountDiv: new FormControl(articleRawValue.accountDiv),
      codeEnd: new FormControl(articleRawValue.codeEnd),
      paragraph: new FormControl(articleRawValue.paragraph),
      chapter: new FormControl(articleRawValue.chapter),
      recipes: new FormControl(articleRawValue.recipes ?? []),
      expenses: new FormControl(articleRawValue.expenses ?? []),
    });
  }

  getArticle(form: ArticleFormGroup): IArticle | NewArticle {
    return form.getRawValue() as IArticle | NewArticle;
  }

  resetForm(form: ArticleFormGroup, article: ArticleFormGroupInput): void {
    const articleRawValue = { ...this.getFormDefaults(), ...article };
    form.reset(
      {
        ...articleRawValue,
        id: { value: articleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ArticleFormDefaults {
    return {
      id: null,
      recipes: [],
      expenses: [],
    };
  }
}
