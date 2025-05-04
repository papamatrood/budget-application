import { IDecision } from 'app/entities/decision/decision.model';

export interface IDecisionItem {
  id: number;
  beneficiary?: string | null;
  amount?: number | null;
  observation?: number | null;
  decision?: IDecision | null;
}

export type NewDecisionItem = Omit<IDecisionItem, 'id'> & { id: null };
