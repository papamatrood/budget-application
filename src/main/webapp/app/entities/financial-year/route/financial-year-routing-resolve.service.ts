import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFinancialYear } from '../financial-year.model';
import { FinancialYearService } from '../service/financial-year.service';

const financialYearResolve = (route: ActivatedRouteSnapshot): Observable<null | IFinancialYear> => {
  const id = route.params.id;
  if (id) {
    return inject(FinancialYearService)
      .find(id)
      .pipe(
        mergeMap((financialYear: HttpResponse<IFinancialYear>) => {
          if (financialYear.body) {
            return of(financialYear.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default financialYearResolve;
