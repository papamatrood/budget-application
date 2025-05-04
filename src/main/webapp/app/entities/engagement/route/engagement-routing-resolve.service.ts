import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEngagement } from '../engagement.model';
import { EngagementService } from '../service/engagement.service';

const engagementResolve = (route: ActivatedRouteSnapshot): Observable<null | IEngagement> => {
  const id = route.params.id;
  if (id) {
    return inject(EngagementService)
      .find(id)
      .pipe(
        mergeMap((engagement: HttpResponse<IEngagement>) => {
          if (engagement.body) {
            return of(engagement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default engagementResolve;
