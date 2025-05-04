import dayjs from 'dayjs/esm';

export interface IEngagement {
  id: number;
  engagementNumber?: string | null;
  engagementDate?: dayjs.Dayjs | null;
  objectOfExpense?: string | null;
  notifiedCredits?: string | null;
  creditCommitted?: string | null;
  creditsAvailable?: string | null;
  amountProposedCommitment?: string | null;
  headDaf?: string | null;
  financialController?: string | null;
  generalManager?: string | null;
}

export type NewEngagement = Omit<IEngagement, 'id'> & { id: null };
