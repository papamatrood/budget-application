import { IFinancialYear, NewFinancialYear } from './financial-year.model';

export const sampleWithRequiredData: IFinancialYear = {
  id: 32636,
  theYear: 14173,
};

export const sampleWithPartialData: IFinancialYear = {
  id: 1083,
  theYear: 26879,
};

export const sampleWithFullData: IFinancialYear = {
  id: 31917,
  theYear: 5642,
};

export const sampleWithNewData: NewFinancialYear = {
  theYear: 2026,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
