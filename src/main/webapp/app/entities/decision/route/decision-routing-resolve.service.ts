import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecision } from '../decision.model';
import { DecisionService } from '../service/decision.service';

const decisionResolve = (route: ActivatedRouteSnapshot): Observable<null | IDecision> => {
  const id = route.params.id;
  if (id) {
    return inject(DecisionService)
      .find(id)
      .pipe(
        mergeMap((decision: HttpResponse<IDecision>) => {
          if (decision.body) {
            return of(decision.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default decisionResolve;
