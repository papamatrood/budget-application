import dayjs from 'dayjs/esm';

import { IAppUser, NewAppUser } from './app-user.model';

export const sampleWithRequiredData: IAppUser = {
  id: 19407,
};

export const sampleWithPartialData: IAppUser = {
  id: 22065,
  accountStatus: false,
  dateCreated: dayjs('2025-05-03T15:43'),
  firstname: 'par suite de',
  birthDate: dayjs('2025-05-04T02:15'),
  position: 'bien manifester',
};

export const sampleWithFullData: IAppUser = {
  id: 1902,
  accountStatus: true,
  lastDateUpdate: dayjs('2025-05-04T10:46'),
  dateCreated: dayjs('2025-05-04T06:17'),
  firstname: 'freiner communiquer insipide',
  lastname: 'conduire conseil municipal',
  phoneNumber: 'solitaire incognito',
  birthDate: dayjs('2025-05-04T03:11'),
  birthPlace: 'songer drôlement errer',
  gender: 'FEMALE',
  familySituation: 'CELIBATAIRE',
  position: 'partenaire au-dedans de fidèle',
  address: 'bè clac',
};

export const sampleWithNewData: NewAppUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
