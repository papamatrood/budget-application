import { Routes } from "@angular/router";

const routes: Routes = [
  // {
  //   path: 'authority',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.adminAuthority.home.title' },
  //   loadChildren: () => import('./admin/authority/authority.routes'),
  // },
  {
    path: "user",
    data: { pageTitle: "jhipsterFinancialManagementApp.appUser.home.title" },
    loadChildren: () => import("./app-user/app-user.routes"),
  },
  {
    path: "parametres",
    loadComponent: () => import("./setting/setting.component"),
    title: "login.title",
  },
  {
    path: "financial-year",
    data: {
      pageTitle: "jhipsterFinancialManagementApp.financialYear.home.title",
    },
    loadChildren: () => import("./financial-year/financial-year.routes"),
  },
  {
    path: "sub-title",
    data: { pageTitle: "jhipsterFinancialManagementApp.subTitle.home.title" },
    loadChildren: () => import("./sub-title/sub-title.routes"),
  },
  {
    path: "chapitre",
    data: { pageTitle: "jhipsterFinancialManagementApp.chapter.home.title" },
    loadChildren: () => import("./chapter/chapter.routes"),
  },
  {
    path: "article",
    data: { pageTitle: "jhipsterFinancialManagementApp.article.home.title" },
    loadChildren: () => import("./article/article.routes"),
  },
  {
    path: "recipe",
    data: { pageTitle: "budgetApplicationApp.recipe.home.title" },
    loadChildren: () => import("./recipe/recipe.routes"),
  },
  // {
  //   path: 'chapter',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.chapter.home.title' },
  //   loadChildren: () => import('./chapter/chapter.routes'),
  // },
  // {
  //   path: 'article',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.article.home.title' },
  //   loadChildren: () => import('./article/article.routes'),
  // },
  // {
  //   path: 'recipe',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.recipe.home.title' },
  //   loadChildren: () => import('./recipe/recipe.routes'),
  // },
  // {
  //   path: 'expense',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.expense.home.title' },
  //   loadChildren: () => import('./expense/expense.routes'),
  // },
  // {
  //   path: 'annex-decision',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.annexDecision.home.title' },
  //   loadChildren: () => import('./annex-decision/annex-decision.routes'),
  // },
  // {
  //   path: 'supplier',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.supplier.home.title' },
  //   loadChildren: () => import('./supplier/supplier.routes'),
  // },
  // {
  //   path: 'purchase-order',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.purchaseOrder.home.title' },
  //   loadChildren: () => import('./purchase-order/purchase-order.routes'),
  // },
  // {
  //   path: 'purchase-order-item',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.purchaseOrderItem.home.title' },
  //   loadChildren: () => import('./purchase-order-item/purchase-order-item.routes'),
  // },
  // {
  //   path: 'decision',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.decision.home.title' },
  //   loadChildren: () => import('./decision/decision.routes'),
  // },
  // {
  //   path: 'decision-item',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.decisionItem.home.title' },
  //   loadChildren: () => import('./decision-item/decision-item.routes'),
  // },
  // {
  //   path: 'engagement',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.engagement.home.title' },
  //   loadChildren: () => import('./engagement/engagement.routes'),
  // },
  // {
  //   path: 'mandate',
  //   data: { pageTitle: 'jhipsterFinancialManagementApp.mandate.home.title' },
  //   loadChildren: () => import('./mandate/mandate.routes'),
  // },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
