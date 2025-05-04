import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubTitle, NewSubTitle } from '../sub-title.model';

export type PartialUpdateSubTitle = Partial<ISubTitle> & Pick<ISubTitle, 'id'>;

export type EntityResponseType = HttpResponse<ISubTitle>;
export type EntityArrayResponseType = HttpResponse<ISubTitle[]>;

@Injectable({ providedIn: 'root' })
export class SubTitleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sub-titles');

  create(subTitle: NewSubTitle): Observable<EntityResponseType> {
    return this.http.post<ISubTitle>(this.resourceUrl, subTitle, { observe: 'response' });
  }

  update(subTitle: ISubTitle): Observable<EntityResponseType> {
    return this.http.put<ISubTitle>(`${this.resourceUrl}/${this.getSubTitleIdentifier(subTitle)}`, subTitle, { observe: 'response' });
  }

  partialUpdate(subTitle: PartialUpdateSubTitle): Observable<EntityResponseType> {
    return this.http.patch<ISubTitle>(`${this.resourceUrl}/${this.getSubTitleIdentifier(subTitle)}`, subTitle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubTitle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubTitle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubTitleIdentifier(subTitle: Pick<ISubTitle, 'id'>): number {
    return subTitle.id;
  }

  compareSubTitle(o1: Pick<ISubTitle, 'id'> | null, o2: Pick<ISubTitle, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubTitleIdentifier(o1) === this.getSubTitleIdentifier(o2) : o1 === o2;
  }

  addSubTitleToCollectionIfMissing<Type extends Pick<ISubTitle, 'id'>>(
    subTitleCollection: Type[],
    ...subTitlesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const subTitles: Type[] = subTitlesToCheck.filter(isPresent);
    if (subTitles.length > 0) {
      const subTitleCollectionIdentifiers = subTitleCollection.map(subTitleItem => this.getSubTitleIdentifier(subTitleItem));
      const subTitlesToAdd = subTitles.filter(subTitleItem => {
        const subTitleIdentifier = this.getSubTitleIdentifier(subTitleItem);
        if (subTitleCollectionIdentifiers.includes(subTitleIdentifier)) {
          return false;
        }
        subTitleCollectionIdentifiers.push(subTitleIdentifier);
        return true;
      });
      return [...subTitlesToAdd, ...subTitleCollection];
    }
    return subTitleCollection;
  }
}
