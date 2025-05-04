import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDecisionItem } from '../decision-item.model';
import { DecisionItemService } from '../service/decision-item.service';

const decisionItemResolve = (route: ActivatedRouteSnapshot): Observable<null | IDecisionItem> => {
  const id = route.params.id;
  if (id) {
    return inject(DecisionItemService)
      .find(id)
      .pipe(
        mergeMap((decisionItem: HttpResponse<IDecisionItem>) => {
          if (decisionItem.body) {
            return of(decisionItem.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default decisionItemResolve;
