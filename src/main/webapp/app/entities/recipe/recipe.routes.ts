import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import RecipeResolve from './route/recipe-routing-resolve.service';

const recipeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/recipe.component').then(m => m.RecipeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/recipe-detail.component').then(m => m.RecipeDetailComponent),
    resolve: {
      recipe: RecipeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/recipe-update.component').then(m => m.RecipeUpdateComponent),
    resolve: {
      recipe: RecipeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/recipe-update.component').then(m => m.RecipeUpdateComponent),
    resolve: {
      recipe: RecipeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default recipeRoute;
