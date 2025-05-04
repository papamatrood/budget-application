import { IExpense, NewExpense } from './expense.model';

export const sampleWithRequiredData: IExpense = {
  id: 5007,
};

export const sampleWithPartialData: IExpense = {
  id: 31796,
};

export const sampleWithFullData: IExpense = {
  id: 30901,
  achievementsInThePastYear: 26398,
  newYearForecast: 3779,
  category: 'OPERATING_RECIPE',
};

export const sampleWithNewData: NewExpense = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
