import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubTitle } from '../sub-title.model';
import { SubTitleService } from '../service/sub-title.service';

const subTitleResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubTitle> => {
  const id = route.params.id;
  if (id) {
    return inject(SubTitleService)
      .find(id)
      .pipe(
        mergeMap((subTitle: HttpResponse<ISubTitle>) => {
          if (subTitle.body) {
            return of(subTitle.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default subTitleResolve;
