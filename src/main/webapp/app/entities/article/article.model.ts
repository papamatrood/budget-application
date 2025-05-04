import { IChapter } from 'app/entities/chapter/chapter.model';
import { IRecipe } from 'app/entities/recipe/recipe.model';
import { IExpense } from 'app/entities/expense/expense.model';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';

export interface IArticle {
  id: number;
  category?: keyof typeof FinancialCategoryEnum | null;
  code?: string | null;
  designation?: string | null;
  accountDiv?: string | null;
  codeEnd?: string | null;
  paragraph?: string | null;
  chapter?: IChapter | null;
  recipes?: IRecipe[] | null;
  expenses?: IExpense[] | null;
}

export type NewArticle = Omit<IArticle, 'id'> & { id: null };
