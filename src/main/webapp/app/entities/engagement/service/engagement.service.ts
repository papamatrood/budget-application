import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEngagement, NewEngagement } from '../engagement.model';

export type PartialUpdateEngagement = Partial<IEngagement> & Pick<IEngagement, 'id'>;

type RestOf<T extends IEngagement | NewEngagement> = Omit<T, 'engagementDate'> & {
  engagementDate?: string | null;
};

export type RestEngagement = RestOf<IEngagement>;

export type NewRestEngagement = RestOf<NewEngagement>;

export type PartialUpdateRestEngagement = RestOf<PartialUpdateEngagement>;

export type EntityResponseType = HttpResponse<IEngagement>;
export type EntityArrayResponseType = HttpResponse<IEngagement[]>;

@Injectable({ providedIn: 'root' })
export class EngagementService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/engagements');

  create(engagement: NewEngagement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(engagement);
    return this.http
      .post<RestEngagement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(engagement: IEngagement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(engagement);
    return this.http
      .put<RestEngagement>(`${this.resourceUrl}/${this.getEngagementIdentifier(engagement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(engagement: PartialUpdateEngagement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(engagement);
    return this.http
      .patch<RestEngagement>(`${this.resourceUrl}/${this.getEngagementIdentifier(engagement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEngagement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEngagement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEngagementIdentifier(engagement: Pick<IEngagement, 'id'>): number {
    return engagement.id;
  }

  compareEngagement(o1: Pick<IEngagement, 'id'> | null, o2: Pick<IEngagement, 'id'> | null): boolean {
    return o1 && o2 ? this.getEngagementIdentifier(o1) === this.getEngagementIdentifier(o2) : o1 === o2;
  }

  addEngagementToCollectionIfMissing<Type extends Pick<IEngagement, 'id'>>(
    engagementCollection: Type[],
    ...engagementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const engagements: Type[] = engagementsToCheck.filter(isPresent);
    if (engagements.length > 0) {
      const engagementCollectionIdentifiers = engagementCollection.map(engagementItem => this.getEngagementIdentifier(engagementItem));
      const engagementsToAdd = engagements.filter(engagementItem => {
        const engagementIdentifier = this.getEngagementIdentifier(engagementItem);
        if (engagementCollectionIdentifiers.includes(engagementIdentifier)) {
          return false;
        }
        engagementCollectionIdentifiers.push(engagementIdentifier);
        return true;
      });
      return [...engagementsToAdd, ...engagementCollection];
    }
    return engagementCollection;
  }

  protected convertDateFromClient<T extends IEngagement | NewEngagement | PartialUpdateEngagement>(engagement: T): RestOf<T> {
    return {
      ...engagement,
      engagementDate: engagement.engagementDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEngagement: RestEngagement): IEngagement {
    return {
      ...restEngagement,
      engagementDate: restEngagement.engagementDate ? dayjs(restEngagement.engagementDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEngagement>): HttpResponse<IEngagement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEngagement[]>): HttpResponse<IEngagement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
