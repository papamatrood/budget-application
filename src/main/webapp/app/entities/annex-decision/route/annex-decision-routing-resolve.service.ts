import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnexDecision } from '../annex-decision.model';
import { AnnexDecisionService } from '../service/annex-decision.service';

const annexDecisionResolve = (route: ActivatedRouteSnapshot): Observable<null | IAnnexDecision> => {
  const id = route.params.id;
  if (id) {
    return inject(AnnexDecisionService)
      .find(id)
      .pipe(
        mergeMap((annexDecision: HttpResponse<IAnnexDecision>) => {
          if (annexDecision.body) {
            return of(annexDecision.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default annexDecisionResolve;
