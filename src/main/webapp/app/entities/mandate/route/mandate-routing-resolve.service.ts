import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';

const mandateResolve = (route: ActivatedRouteSnapshot): Observable<null | IMandate> => {
  const id = route.params.id;
  if (id) {
    return inject(MandateService)
      .find(id)
      .pipe(
        mergeMap((mandate: HttpResponse<IMandate>) => {
          if (mandate.body) {
            return of(mandate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default mandateResolve;
