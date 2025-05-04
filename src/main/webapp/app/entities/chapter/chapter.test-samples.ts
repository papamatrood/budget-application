import { IChapter, NewChapter } from './chapter.model';

export const sampleWithRequiredData: IChapter = {
  id: 2926,
  code: 'hier',
  designation: 'tant ouin smack',
};

export const sampleWithPartialData: IChapter = {
  id: 18093,
  code: 'espiègle super',
  designation: 'gens pleurer',
};

export const sampleWithFullData: IChapter = {
  id: 4093,
  code: 'personnel lâche',
  designation: 'briller pourvu que',
};

export const sampleWithNewData: NewChapter = {
  code: 'conquérir',
  designation: 'faire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
