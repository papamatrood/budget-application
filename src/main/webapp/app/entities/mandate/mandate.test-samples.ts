import dayjs from 'dayjs/esm';

import { IMandate, NewMandate } from './mandate.model';

export const sampleWithRequiredData: IMandate = {
  id: 9497,
  mandateNumber: 'novice au-dessus de',
  mandateDate: dayjs('2025-05-04T01:52'),
};

export const sampleWithPartialData: IMandate = {
  id: 23845,
  mandateNumber: 'coupable',
  mandateDate: dayjs('2025-05-03T23:22'),
  issueSlipNumber: 'instituer',
};

export const sampleWithFullData: IMandate = {
  id: 11722,
  mandateNumber: 'alentour trop',
  mandateDate: dayjs('2025-05-04T09:51'),
  issueSlipNumber: 'tellement diététiste',
  monthAndYearOfIssue: 'contre',
  supportingDocuments: 'direction',
};

export const sampleWithNewData: NewMandate = {
  mandateNumber: 'très quasi immense',
  mandateDate: dayjs('2025-05-04T04:11'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
