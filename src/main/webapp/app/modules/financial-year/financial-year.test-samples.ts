import {
  IFinancialYear,
  NewFinancialYear,
} from "app/entities/financial-year/financial-year.model";

export const sampleWithRequiredData: IFinancialYear = {
  id: 32636,
  year: 14173,
};

export const sampleWithPartialData: IFinancialYear = {
  id: 1083,
  year: 26879,
};

export const sampleWithFullData: IFinancialYear = {
  id: 31917,
  year: 5642,
};

export const sampleWithNewData: NewFinancialYear = {
  year: 2026,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
