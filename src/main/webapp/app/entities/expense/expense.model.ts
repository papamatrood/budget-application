import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';
import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { IArticle } from 'app/entities/article/article.model';
import { FinancialCategoryEnum } from 'app/entities/enumerations/financial-category-enum.model';

export interface IExpense {
  id: number;
  achievementsInThePastYear?: number | null;
  newYearForecast?: number | null;
  category?: keyof typeof FinancialCategoryEnum | null;
  annexDecision?: IAnnexDecision | null;
  financialYear?: IFinancialYear | null;
  articles?: IArticle[] | null;
}

export type NewExpense = Omit<IExpense, 'id'> & { id: null };
