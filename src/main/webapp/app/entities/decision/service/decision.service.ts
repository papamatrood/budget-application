import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDecision, NewDecision } from '../decision.model';

export type PartialUpdateDecision = Partial<IDecision> & Pick<IDecision, 'id'>;

type RestOf<T extends IDecision | NewDecision> = Omit<T, 'decisionDate'> & {
  decisionDate?: string | null;
};

export type RestDecision = RestOf<IDecision>;

export type NewRestDecision = RestOf<NewDecision>;

export type PartialUpdateRestDecision = RestOf<PartialUpdateDecision>;

export type EntityResponseType = HttpResponse<IDecision>;
export type EntityArrayResponseType = HttpResponse<IDecision[]>;

@Injectable({ providedIn: 'root' })
export class DecisionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/decisions');

  create(decision: NewDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .post<RestDecision>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(decision: IDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .put<RestDecision>(`${this.resourceUrl}/${this.getDecisionIdentifier(decision)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(decision: PartialUpdateDecision): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(decision);
    return this.http
      .patch<RestDecision>(`${this.resourceUrl}/${this.getDecisionIdentifier(decision)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDecision>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDecision[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDecisionIdentifier(decision: Pick<IDecision, 'id'>): number {
    return decision.id;
  }

  compareDecision(o1: Pick<IDecision, 'id'> | null, o2: Pick<IDecision, 'id'> | null): boolean {
    return o1 && o2 ? this.getDecisionIdentifier(o1) === this.getDecisionIdentifier(o2) : o1 === o2;
  }

  addDecisionToCollectionIfMissing<Type extends Pick<IDecision, 'id'>>(
    decisionCollection: Type[],
    ...decisionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const decisions: Type[] = decisionsToCheck.filter(isPresent);
    if (decisions.length > 0) {
      const decisionCollectionIdentifiers = decisionCollection.map(decisionItem => this.getDecisionIdentifier(decisionItem));
      const decisionsToAdd = decisions.filter(decisionItem => {
        const decisionIdentifier = this.getDecisionIdentifier(decisionItem);
        if (decisionCollectionIdentifiers.includes(decisionIdentifier)) {
          return false;
        }
        decisionCollectionIdentifiers.push(decisionIdentifier);
        return true;
      });
      return [...decisionsToAdd, ...decisionCollection];
    }
    return decisionCollection;
  }

  protected convertDateFromClient<T extends IDecision | NewDecision | PartialUpdateDecision>(decision: T): RestOf<T> {
    return {
      ...decision,
      decisionDate: decision.decisionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDecision: RestDecision): IDecision {
    return {
      ...restDecision,
      decisionDate: restDecision.decisionDate ? dayjs(restDecision.decisionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDecision>): HttpResponse<IDecision> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDecision[]>): HttpResponse<IDecision[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
