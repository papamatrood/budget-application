import dayjs from 'dayjs/esm';
import { IEngagement } from 'app/entities/engagement/engagement.model';
import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';

export interface IDecision {
  id: number;
  decisionNumber?: string | null;
  decisionDate?: dayjs.Dayjs | null;
  engagement?: IEngagement | null;
  annexDecision?: IAnnexDecision | null;
}

export type NewDecision = Omit<IDecision, 'id'> & { id: null };
