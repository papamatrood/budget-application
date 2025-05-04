import { IAnnexDecision, NewAnnexDecision } from './annex-decision.model';

export const sampleWithRequiredData: IAnnexDecision = {
  id: 9377,
};

export const sampleWithPartialData: IAnnexDecision = {
  id: 22527,
  designation: 'moderne désormais',
  creditsAlreadyOpen: 'ouah',
};

export const sampleWithFullData: IAnnexDecision = {
  id: 24619,
  designation: 'adepte',
  expenseAmount: 'alors que',
  creditsAlreadyOpen: "d'avec invoquer toc-toc",
  creditsOpen: 'du fait que triangulaire',
};

export const sampleWithNewData: NewAnnexDecision = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
