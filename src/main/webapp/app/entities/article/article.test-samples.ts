import { IArticle, NewArticle } from './article.model';

export const sampleWithRequiredData: IArticle = {
  id: 17774,
  code: 'étant donné que disposer épouser',
  designation: 'continuer concurrence mélancolique',
};

export const sampleWithPartialData: IArticle = {
  id: 29334,
  code: 'ressembler',
  designation: 'en plus de population du Québec',
  accountDiv: 'certes',
  codeEnd: 'sembler en decà de pff',
};

export const sampleWithFullData: IArticle = {
  id: 28493,
  category: 'OPERATING_RECIPE',
  code: 'étrangler',
  designation: 'sans que électorat préférer',
  accountDiv: 'patientèle insipide',
  codeEnd: 'turquoise',
  paragraph: 'gai au cas où',
};

export const sampleWithNewData: NewArticle = {
  code: 'cuicui',
  designation: 'équipe',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
