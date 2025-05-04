import { IRecipe, NewRecipe } from './recipe.model';

export const sampleWithRequiredData: IRecipe = {
  id: 23791,
};

export const sampleWithPartialData: IRecipe = {
  id: 31080,
  newYearForecast: 22006,
};

export const sampleWithFullData: IRecipe = {
  id: 29984,
  achievementsInThePastYear: 3436,
  newYearForecast: 8867,
  category: 'INVESTMENT_EXPENSE',
};

export const sampleWithNewData: NewRecipe = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
