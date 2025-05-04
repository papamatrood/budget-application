import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnexDecision, NewAnnexDecision } from '../annex-decision.model';

export type PartialUpdateAnnexDecision = Partial<IAnnexDecision> & Pick<IAnnexDecision, 'id'>;

export type EntityResponseType = HttpResponse<IAnnexDecision>;
export type EntityArrayResponseType = HttpResponse<IAnnexDecision[]>;

@Injectable({ providedIn: 'root' })
export class AnnexDecisionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annex-decisions');

  create(annexDecision: NewAnnexDecision): Observable<EntityResponseType> {
    return this.http.post<IAnnexDecision>(this.resourceUrl, annexDecision, { observe: 'response' });
  }

  update(annexDecision: IAnnexDecision): Observable<EntityResponseType> {
    return this.http.put<IAnnexDecision>(`${this.resourceUrl}/${this.getAnnexDecisionIdentifier(annexDecision)}`, annexDecision, {
      observe: 'response',
    });
  }

  partialUpdate(annexDecision: PartialUpdateAnnexDecision): Observable<EntityResponseType> {
    return this.http.patch<IAnnexDecision>(`${this.resourceUrl}/${this.getAnnexDecisionIdentifier(annexDecision)}`, annexDecision, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnnexDecision>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnnexDecision[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAnnexDecisionIdentifier(annexDecision: Pick<IAnnexDecision, 'id'>): number {
    return annexDecision.id;
  }

  compareAnnexDecision(o1: Pick<IAnnexDecision, 'id'> | null, o2: Pick<IAnnexDecision, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnnexDecisionIdentifier(o1) === this.getAnnexDecisionIdentifier(o2) : o1 === o2;
  }

  addAnnexDecisionToCollectionIfMissing<Type extends Pick<IAnnexDecision, 'id'>>(
    annexDecisionCollection: Type[],
    ...annexDecisionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const annexDecisions: Type[] = annexDecisionsToCheck.filter(isPresent);
    if (annexDecisions.length > 0) {
      const annexDecisionCollectionIdentifiers = annexDecisionCollection.map(annexDecisionItem =>
        this.getAnnexDecisionIdentifier(annexDecisionItem),
      );
      const annexDecisionsToAdd = annexDecisions.filter(annexDecisionItem => {
        const annexDecisionIdentifier = this.getAnnexDecisionIdentifier(annexDecisionItem);
        if (annexDecisionCollectionIdentifiers.includes(annexDecisionIdentifier)) {
          return false;
        }
        annexDecisionCollectionIdentifiers.push(annexDecisionIdentifier);
        return true;
      });
      return [...annexDecisionsToAdd, ...annexDecisionCollection];
    }
    return annexDecisionCollection;
  }
}
