import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ChapterResolve from './route/chapter-routing-resolve.service';

const chapterRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/chapter.component').then(m => m.ChapterComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/chapter-detail.component').then(m => m.ChapterDetailComponent),
    resolve: {
      chapter: ChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/chapter-update.component').then(m => m.ChapterUpdateComponent),
    resolve: {
      chapter: ChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/chapter-update.component').then(m => m.ChapterUpdateComponent),
    resolve: {
      chapter: ChapterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default chapterRoute;
