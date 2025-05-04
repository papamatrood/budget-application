import { IDecisionItem, NewDecisionItem } from './decision-item.model';

export const sampleWithRequiredData: IDecisionItem = {
  id: 28430,
  beneficiary: 'bzzz aussitôt que',
};

export const sampleWithPartialData: IDecisionItem = {
  id: 28105,
  beneficiary: 'de façon à ce que ménager tant que',
};

export const sampleWithFullData: IDecisionItem = {
  id: 24542,
  beneficiary: 'smack ramasser',
  amount: 30178,
  observation: 15858.02,
};

export const sampleWithNewData: NewDecisionItem = {
  beneficiary: 'guide simple',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
