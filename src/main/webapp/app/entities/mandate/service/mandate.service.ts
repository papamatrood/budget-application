import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMandate, NewMandate } from '../mandate.model';

export type PartialUpdateMandate = Partial<IMandate> & Pick<IMandate, 'id'>;

type RestOf<T extends IMandate | NewMandate> = Omit<T, 'mandateDate'> & {
  mandateDate?: string | null;
};

export type RestMandate = RestOf<IMandate>;

export type NewRestMandate = RestOf<NewMandate>;

export type PartialUpdateRestMandate = RestOf<PartialUpdateMandate>;

export type EntityResponseType = HttpResponse<IMandate>;
export type EntityArrayResponseType = HttpResponse<IMandate[]>;

@Injectable({ providedIn: 'root' })
export class MandateService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mandates');

  create(mandate: NewMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .post<RestMandate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(mandate: IMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .put<RestMandate>(`${this.resourceUrl}/${this.getMandateIdentifier(mandate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(mandate: PartialUpdateMandate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(mandate);
    return this.http
      .patch<RestMandate>(`${this.resourceUrl}/${this.getMandateIdentifier(mandate)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMandate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMandate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMandateIdentifier(mandate: Pick<IMandate, 'id'>): number {
    return mandate.id;
  }

  compareMandate(o1: Pick<IMandate, 'id'> | null, o2: Pick<IMandate, 'id'> | null): boolean {
    return o1 && o2 ? this.getMandateIdentifier(o1) === this.getMandateIdentifier(o2) : o1 === o2;
  }

  addMandateToCollectionIfMissing<Type extends Pick<IMandate, 'id'>>(
    mandateCollection: Type[],
    ...mandatesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mandates: Type[] = mandatesToCheck.filter(isPresent);
    if (mandates.length > 0) {
      const mandateCollectionIdentifiers = mandateCollection.map(mandateItem => this.getMandateIdentifier(mandateItem));
      const mandatesToAdd = mandates.filter(mandateItem => {
        const mandateIdentifier = this.getMandateIdentifier(mandateItem);
        if (mandateCollectionIdentifiers.includes(mandateIdentifier)) {
          return false;
        }
        mandateCollectionIdentifiers.push(mandateIdentifier);
        return true;
      });
      return [...mandatesToAdd, ...mandateCollection];
    }
    return mandateCollection;
  }

  protected convertDateFromClient<T extends IMandate | NewMandate | PartialUpdateMandate>(mandate: T): RestOf<T> {
    return {
      ...mandate,
      mandateDate: mandate.mandateDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restMandate: RestMandate): IMandate {
    return {
      ...restMandate,
      mandateDate: restMandate.mandateDate ? dayjs(restMandate.mandateDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMandate>): HttpResponse<IMandate> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMandate[]>): HttpResponse<IMandate[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
