import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'budgetApplicationApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'app-user',
    data: { pageTitle: 'budgetApplicationApp.appUser.home.title' },
    loadChildren: () => import('./app-user/app-user.routes'),
  },
  {
    path: 'financial-year',
    data: { pageTitle: 'budgetApplicationApp.financialYear.home.title' },
    loadChildren: () => import('./financial-year/financial-year.routes'),
  },
  {
    path: 'sub-title',
    data: { pageTitle: 'budgetApplicationApp.subTitle.home.title' },
    loadChildren: () => import('./sub-title/sub-title.routes'),
  },
  {
    path: 'chapter',
    data: { pageTitle: 'budgetApplicationApp.chapter.home.title' },
    loadChildren: () => import('./chapter/chapter.routes'),
  },
  {
    path: 'article',
    data: { pageTitle: 'budgetApplicationApp.article.home.title' },
    loadChildren: () => import('./article/article.routes'),
  },
  {
    path: 'recipe',
    data: { pageTitle: 'budgetApplicationApp.recipe.home.title' },
    loadChildren: () => import('./recipe/recipe.routes'),
  },
  {
    path: 'expense',
    data: { pageTitle: 'budgetApplicationApp.expense.home.title' },
    loadChildren: () => import('./expense/expense.routes'),
  },
  {
    path: 'annex-decision',
    data: { pageTitle: 'budgetApplicationApp.annexDecision.home.title' },
    loadChildren: () => import('./annex-decision/annex-decision.routes'),
  },
  {
    path: 'supplier',
    data: { pageTitle: 'budgetApplicationApp.supplier.home.title' },
    loadChildren: () => import('./supplier/supplier.routes'),
  },
  {
    path: 'purchase-order',
    data: { pageTitle: 'budgetApplicationApp.purchaseOrder.home.title' },
    loadChildren: () => import('./purchase-order/purchase-order.routes'),
  },
  {
    path: 'purchase-order-item',
    data: { pageTitle: 'budgetApplicationApp.purchaseOrderItem.home.title' },
    loadChildren: () => import('./purchase-order-item/purchase-order-item.routes'),
  },
  {
    path: 'decision',
    data: { pageTitle: 'budgetApplicationApp.decision.home.title' },
    loadChildren: () => import('./decision/decision.routes'),
  },
  {
    path: 'decision-item',
    data: { pageTitle: 'budgetApplicationApp.decisionItem.home.title' },
    loadChildren: () => import('./decision-item/decision-item.routes'),
  },
  {
    path: 'engagement',
    data: { pageTitle: 'budgetApplicationApp.engagement.home.title' },
    loadChildren: () => import('./engagement/engagement.routes'),
  },
  {
    path: 'mandate',
    data: { pageTitle: 'budgetApplicationApp.mandate.home.title' },
    loadChildren: () => import('./mandate/mandate.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
