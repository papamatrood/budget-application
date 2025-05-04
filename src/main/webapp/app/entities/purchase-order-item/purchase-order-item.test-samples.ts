import { IPurchaseOrderItem, NewPurchaseOrderItem } from './purchase-order-item.model';

export const sampleWithRequiredData: IPurchaseOrderItem = {
  id: 18166,
  productName: 'bien que étant donné que convenir',
  quantity: 6375,
  unitPrice: 5846.61,
  totalAmount: 22388.06,
};

export const sampleWithPartialData: IPurchaseOrderItem = {
  id: 26198,
  productName: 'depuis vraiment aménager',
  quantity: 31520,
  unitPrice: 28492.58,
  totalAmount: 21270.85,
};

export const sampleWithFullData: IPurchaseOrderItem = {
  id: 14657,
  productName: 'commissionnaire à la merci',
  quantity: 11101,
  unitPrice: 24822.6,
  totalAmount: 8152.95,
};

export const sampleWithNewData: NewPurchaseOrderItem = {
  productName: 'fourbe',
  quantity: 26435,
  unitPrice: 1281.98,
  totalAmount: 29730.84,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
