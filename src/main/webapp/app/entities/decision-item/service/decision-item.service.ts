import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecisionItem, NewDecisionItem } from '../decision-item.model';

export type PartialUpdateDecisionItem = Partial<IDecisionItem> & Pick<IDecisionItem, 'id'>;

export type EntityResponseType = HttpResponse<IDecisionItem>;
export type EntityArrayResponseType = HttpResponse<IDecisionItem[]>;

@Injectable({ providedIn: 'root' })
export class DecisionItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decision-items');

  create(decisionItem: NewDecisionItem): Observable<EntityResponseType> {
    return this.http.post<IDecisionItem>(this.resourceUrl, decisionItem, { observe: 'response' });
  }

  update(decisionItem: IDecisionItem): Observable<EntityResponseType> {
    return this.http.put<IDecisionItem>(`${this.resourceUrl}/${this.getDecisionItemIdentifier(decisionItem)}`, decisionItem, {
      observe: 'response',
    });
  }

  partialUpdate(decisionItem: PartialUpdateDecisionItem): Observable<EntityResponseType> {
    return this.http.patch<IDecisionItem>(`${this.resourceUrl}/${this.getDecisionItemIdentifier(decisionItem)}`, decisionItem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDecisionItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDecisionItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDecisionItemIdentifier(decisionItem: Pick<IDecisionItem, 'id'>): number {
    return decisionItem.id;
  }

  compareDecisionItem(o1: Pick<IDecisionItem, 'id'> | null, o2: Pick<IDecisionItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getDecisionItemIdentifier(o1) === this.getDecisionItemIdentifier(o2) : o1 === o2;
  }

  addDecisionItemToCollectionIfMissing<Type extends Pick<IDecisionItem, 'id'>>(
    decisionItemCollection: Type[],
    ...decisionItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const decisionItems: Type[] = decisionItemsToCheck.filter(isPresent);
    if (decisionItems.length > 0) {
      const decisionItemCollectionIdentifiers = decisionItemCollection.map(decisionItemItem =>
        this.getDecisionItemIdentifier(decisionItemItem),
      );
      const decisionItemsToAdd = decisionItems.filter(decisionItemItem => {
        const decisionItemIdentifier = this.getDecisionItemIdentifier(decisionItemItem);
        if (decisionItemCollectionIdentifiers.includes(decisionItemIdentifier)) {
          return false;
        }
        decisionItemCollectionIdentifiers.push(decisionItemIdentifier);
        return true;
      });
      return [...decisionItemsToAdd, ...decisionItemCollection];
    }
    return decisionItemCollection;
  }
}
