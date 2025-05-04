import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IExpense, NewExpense } from '../expense.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IExpense for edit and NewExpenseFormGroupInput for create.
 */
type ExpenseFormGroupInput = IExpense | PartialWithRequiredKeyOf<NewExpense>;

type ExpenseFormDefaults = Pick<NewExpense, 'id' | 'articles'>;

type ExpenseFormGroupContent = {
  id: FormControl<IExpense['id'] | NewExpense['id']>;
  achievementsInThePastYear: FormControl<IExpense['achievementsInThePastYear']>;
  newYearForecast: FormControl<IExpense['newYearForecast']>;
  category: FormControl<IExpense['category']>;
  financialYear: FormControl<IExpense['financialYear']>;
  annexDecision: FormControl<IExpense['annexDecision']>;
  articles: FormControl<IExpense['articles']>;
};

export type ExpenseFormGroup = FormGroup<ExpenseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ExpenseFormService {
  createExpenseFormGroup(expense: ExpenseFormGroupInput = { id: null }): ExpenseFormGroup {
    const expenseRawValue = {
      ...this.getFormDefaults(),
      ...expense,
    };
    return new FormGroup<ExpenseFormGroupContent>({
      id: new FormControl(
        { value: expenseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      achievementsInThePastYear: new FormControl(expenseRawValue.achievementsInThePastYear),
      newYearForecast: new FormControl(expenseRawValue.newYearForecast),
      category: new FormControl(expenseRawValue.category),
      financialYear: new FormControl(expenseRawValue.financialYear),
      annexDecision: new FormControl(expenseRawValue.annexDecision),
      articles: new FormControl(expenseRawValue.articles ?? []),
    });
  }

  getExpense(form: ExpenseFormGroup): IExpense | NewExpense {
    return form.getRawValue() as IExpense | NewExpense;
  }

  resetForm(form: ExpenseFormGroup, expense: ExpenseFormGroupInput): void {
    const expenseRawValue = { ...this.getFormDefaults(), ...expense };
    form.reset(
      {
        ...expenseRawValue,
        id: { value: expenseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ExpenseFormDefaults {
    return {
      id: null,
      articles: [],
    };
  }
}
