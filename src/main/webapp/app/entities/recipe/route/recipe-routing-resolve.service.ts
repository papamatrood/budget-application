import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecipe } from '../recipe.model';
import { RecipeService } from '../service/recipe.service';

const recipeResolve = (route: ActivatedRouteSnapshot): Observable<null | IRecipe> => {
  const id = route.params.id;
  if (id) {
    return inject(RecipeService)
      .find(id)
      .pipe(
        mergeMap((recipe: HttpResponse<IRecipe>) => {
          if (recipe.body) {
            return of(recipe.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default recipeResolve;
