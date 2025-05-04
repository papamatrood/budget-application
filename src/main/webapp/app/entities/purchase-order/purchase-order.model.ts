import dayjs from 'dayjs/esm';
import { IAnnexDecision } from 'app/entities/annex-decision/annex-decision.model';
import { ISupplier } from 'app/entities/supplier/supplier.model';
import { IEngagement } from 'app/entities/engagement/engagement.model';

export interface IPurchaseOrder {
  id: number;
  nameOfTheMinistry?: string | null;
  orderNumber?: string | null;
  orderDate?: dayjs.Dayjs | null;
  totalAmountWithoutTax?: number | null;
  taxRate?: number | null;
  totalTaxAmount?: number | null;
  prepaidTaxAmount?: number | null;
  totalAmountWithTax?: number | null;
  authExpenditureNumber?: string | null;
  allocatedCredits?: number | null;
  committedExpenditures?: number | null;
  availableBalance?: number | null;
  annexDecision?: IAnnexDecision | null;
  supplier?: ISupplier | null;
  engagement?: IEngagement | null;
}

export type NewPurchaseOrder = Omit<IPurchaseOrder, 'id'> & { id: null };
