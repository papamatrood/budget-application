import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { IArticle } from 'app/entities/article/article.model';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';

export interface IRecipe {
  id: number;
  achievementsInThePastYear?: number | null;
  newYearForecast?: number | null;
  category?: keyof typeof FinancialCategoryEnum | null;
  financialYear?: IFinancialYear | null;
  articles?: IArticle[] | null;
}

export type NewRecipe = Omit<IRecipe, 'id'> & { id: null };
