import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFinancialYear, NewFinancialYear } from '../financial-year.model';

export type PartialUpdateFinancialYear = Partial<IFinancialYear> & Pick<IFinancialYear, 'id'>;

export type EntityResponseType = HttpResponse<IFinancialYear>;
export type EntityArrayResponseType = HttpResponse<IFinancialYear[]>;

@Injectable({ providedIn: 'root' })
export class FinancialYearService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/financial-years');

  create(financialYear: NewFinancialYear): Observable<EntityResponseType> {
    return this.http.post<IFinancialYear>(this.resourceUrl, financialYear, { observe: 'response' });
  }

  update(financialYear: IFinancialYear): Observable<EntityResponseType> {
    return this.http.put<IFinancialYear>(`${this.resourceUrl}/${this.getFinancialYearIdentifier(financialYear)}`, financialYear, {
      observe: 'response',
    });
  }

  partialUpdate(financialYear: PartialUpdateFinancialYear): Observable<EntityResponseType> {
    return this.http.patch<IFinancialYear>(`${this.resourceUrl}/${this.getFinancialYearIdentifier(financialYear)}`, financialYear, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFinancialYear>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFinancialYear[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFinancialYearIdentifier(financialYear: Pick<IFinancialYear, 'id'>): number {
    return financialYear.id;
  }

  compareFinancialYear(o1: Pick<IFinancialYear, 'id'> | null, o2: Pick<IFinancialYear, 'id'> | null): boolean {
    return o1 && o2 ? this.getFinancialYearIdentifier(o1) === this.getFinancialYearIdentifier(o2) : o1 === o2;
  }

  addFinancialYearToCollectionIfMissing<Type extends Pick<IFinancialYear, 'id'>>(
    financialYearCollection: Type[],
    ...financialYearsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const financialYears: Type[] = financialYearsToCheck.filter(isPresent);
    if (financialYears.length > 0) {
      const financialYearCollectionIdentifiers = financialYearCollection.map(financialYearItem =>
        this.getFinancialYearIdentifier(financialYearItem),
      );
      const financialYearsToAdd = financialYears.filter(financialYearItem => {
        const financialYearIdentifier = this.getFinancialYearIdentifier(financialYearItem);
        if (financialYearCollectionIdentifiers.includes(financialYearIdentifier)) {
          return false;
        }
        financialYearCollectionIdentifiers.push(financialYearIdentifier);
        return true;
      });
      return [...financialYearsToAdd, ...financialYearCollection];
    }
    return financialYearCollection;
  }
}
