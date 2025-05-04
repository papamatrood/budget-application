import { ISubTitle, NewSubTitle } from './sub-title.model';

export const sampleWithRequiredData: ISubTitle = {
  id: 15202,
  code: 'porte-parole',
  designation: 'après',
};

export const sampleWithPartialData: ISubTitle = {
  id: 29970,
  code: 'lors de',
  designation: 'gigantesque',
};

export const sampleWithFullData: ISubTitle = {
  id: 23787,
  code: 'affronter jamais direction',
  designation: 'aigre',
};

export const sampleWithNewData: NewSubTitle = {
  code: 'oui mal',
  designation: 'franco',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
