export interface IFinancialYear {
  id: number;
  theYear?: number | null;
}

export type NewFinancialYear = Omit<IFinancialYear, 'id'> & { id: null };
