import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IChapter } from '../chapter.model';
import { ChapterService } from '../service/chapter.service';

const chapterResolve = (route: ActivatedRouteSnapshot): Observable<null | IChapter> => {
  const id = route.params.id;
  if (id) {
    return inject(ChapterService)
      .find(id)
      .pipe(
        mergeMap((chapter: HttpResponse<IChapter>) => {
          if (chapter.body) {
            return of(chapter.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default chapterResolve;
