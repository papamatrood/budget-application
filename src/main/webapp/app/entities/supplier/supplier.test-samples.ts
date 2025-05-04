import { ISupplier, NewSupplier } from './supplier.model';

export const sampleWithRequiredData: ISupplier = {
  id: 22717,
};

export const sampleWithPartialData: ISupplier = {
  id: 21091,
  phone: '+33 149605929',
  commercialRegister: 'longtemps drelin tantôt',
  bankAccount: 'en plus de peut-être',
  email: 'Clementine_Laine3@yahoo.fr',
  website: 'prou en dedans de étonner',
  description: 'psitt',
};

export const sampleWithFullData: ISupplier = {
  id: 3840,
  companyName: 'mieux au-dessous de',
  address: 'électorat vlan communauté étudiante',
  phone: '0471864744',
  nifNumber: 'recta proposer',
  commercialRegister: 'diététiste de crainte que efficace',
  bankAccount: 'en decà de étant donné que modifier',
  mandatingEstablishment: 'boum',
  email: 'Amiel.Chevalier55@gmail.com',
  website: 'dispenser après',
  description: 'toc-toc bon glouglou',
  contactFirstname: 'boum presque',
  contactlastname: 'ci commis de cuisine',
};

export const sampleWithNewData: NewSupplier = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
