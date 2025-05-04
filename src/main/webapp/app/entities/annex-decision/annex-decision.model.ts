import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';

export interface IAnnexDecision {
  id: number;
  designation?: string | null;
  expenseAmount?: string | null;
  creditsAlreadyOpen?: string | null;
  creditsOpen?: string | null;
  financialYear?: IFinancialYear | null;
}

export type NewAnnexDecision = Omit<IAnnexDecision, 'id'> & { id: null };
