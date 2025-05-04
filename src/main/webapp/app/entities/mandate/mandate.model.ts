import dayjs from 'dayjs/esm';
import { IEngagement } from 'app/entities/engagement/engagement.model';

export interface IMandate {
  id: number;
  mandateNumber?: string | null;
  mandateDate?: dayjs.Dayjs | null;
  issueSlipNumber?: string | null;
  monthAndYearOfIssue?: string | null;
  supportingDocuments?: string | null;
  engagement?: IEngagement | null;
}

export type NewMandate = Omit<IMandate, 'id'> & { id: null };
