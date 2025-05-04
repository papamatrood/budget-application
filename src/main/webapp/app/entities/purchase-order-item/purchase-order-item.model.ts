import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';

export interface IPurchaseOrderItem {
  id: number;
  productName?: string | null;
  quantity?: number | null;
  unitPrice?: number | null;
  totalAmount?: number | null;
  purchaseOrder?: IPurchaseOrder | null;
}

export type NewPurchaseOrderItem = Omit<IPurchaseOrderItem, 'id'> & { id: null };
