import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPurchaseOrderItem, NewPurchaseOrderItem } from '../purchase-order-item.model';

export type PartialUpdatePurchaseOrderItem = Partial<IPurchaseOrderItem> & Pick<IPurchaseOrderItem, 'id'>;

export type EntityResponseType = HttpResponse<IPurchaseOrderItem>;
export type EntityArrayResponseType = HttpResponse<IPurchaseOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class PurchaseOrderItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/purchase-order-items');

  create(purchaseOrderItem: NewPurchaseOrderItem): Observable<EntityResponseType> {
    return this.http.post<IPurchaseOrderItem>(this.resourceUrl, purchaseOrderItem, { observe: 'response' });
  }

  update(purchaseOrderItem: IPurchaseOrderItem): Observable<EntityResponseType> {
    return this.http.put<IPurchaseOrderItem>(
      `${this.resourceUrl}/${this.getPurchaseOrderItemIdentifier(purchaseOrderItem)}`,
      purchaseOrderItem,
      { observe: 'response' },
    );
  }

  partialUpdate(purchaseOrderItem: PartialUpdatePurchaseOrderItem): Observable<EntityResponseType> {
    return this.http.patch<IPurchaseOrderItem>(
      `${this.resourceUrl}/${this.getPurchaseOrderItemIdentifier(purchaseOrderItem)}`,
      purchaseOrderItem,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPurchaseOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPurchaseOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPurchaseOrderItemIdentifier(purchaseOrderItem: Pick<IPurchaseOrderItem, 'id'>): number {
    return purchaseOrderItem.id;
  }

  comparePurchaseOrderItem(o1: Pick<IPurchaseOrderItem, 'id'> | null, o2: Pick<IPurchaseOrderItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getPurchaseOrderItemIdentifier(o1) === this.getPurchaseOrderItemIdentifier(o2) : o1 === o2;
  }

  addPurchaseOrderItemToCollectionIfMissing<Type extends Pick<IPurchaseOrderItem, 'id'>>(
    purchaseOrderItemCollection: Type[],
    ...purchaseOrderItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const purchaseOrderItems: Type[] = purchaseOrderItemsToCheck.filter(isPresent);
    if (purchaseOrderItems.length > 0) {
      const purchaseOrderItemCollectionIdentifiers = purchaseOrderItemCollection.map(purchaseOrderItemItem =>
        this.getPurchaseOrderItemIdentifier(purchaseOrderItemItem),
      );
      const purchaseOrderItemsToAdd = purchaseOrderItems.filter(purchaseOrderItemItem => {
        const purchaseOrderItemIdentifier = this.getPurchaseOrderItemIdentifier(purchaseOrderItemItem);
        if (purchaseOrderItemCollectionIdentifiers.includes(purchaseOrderItemIdentifier)) {
          return false;
        }
        purchaseOrderItemCollectionIdentifiers.push(purchaseOrderItemIdentifier);
        return true;
      });
      return [...purchaseOrderItemsToAdd, ...purchaseOrderItemCollection];
    }
    return purchaseOrderItemCollection;
  }
}
