import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { IFinancialYear } from "app/entities/financial-year/financial-year.model";
import { FinancialYearService } from "app/entities/financial-year/service/financial-year.service";
import { IAnnexDecision } from "app/entities/annex-decision/annex-decision.model";
import { AnnexDecisionService } from "app/entities/annex-decision/service/annex-decision.service";
import { IArticle } from "app/entities/article/article.model";
import { ArticleService } from "app/entities/article/service/article.service";
import { IExpense } from "../expense.model";
import { ExpenseService } from "../service/expense.service";
import { ExpenseFormService } from "./expense-form.service";

import { ExpenseUpdateComponent } from "./expense-update.component";

describe("Expense Management Update Component", () => {
  let comp: ExpenseUpdateComponent;
  let fixture: ComponentFixture<ExpenseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let expenseFormService: ExpenseFormService;
  let expenseService: ExpenseService;
  let financialYearService: FinancialYearService;
  let annexDecisionService: AnnexDecisionService;
  let articleService: ArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ExpenseUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ExpenseUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(ExpenseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    expenseFormService = TestBed.inject(ExpenseFormService);
    expenseService = TestBed.inject(ExpenseService);
    financialYearService = TestBed.inject(FinancialYearService);
    annexDecisionService = TestBed.inject(AnnexDecisionService);
    articleService = TestBed.inject(ArticleService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call financialYear query and add missing value", () => {
      const expense: IExpense = { id: 9220 };
      const financialYear: IFinancialYear = { id: 14021 };
      expense.financialYear = financialYear;

      const financialYearCollection: IFinancialYear[] = [{ id: 14021 }];
      jest
        .spyOn(financialYearService, "query")
        .mockReturnValue(
          of(new HttpResponse({ body: financialYearCollection })),
        );
      const expectedCollection: IFinancialYear[] = [
        financialYear,
        ...financialYearCollection,
      ];
      jest
        .spyOn(financialYearService, "addFinancialYearToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(financialYearService.query).toHaveBeenCalled();
      expect(
        financialYearService.addFinancialYearToCollectionIfMissing,
      ).toHaveBeenCalledWith(financialYearCollection, financialYear);
      expect(comp.financialYearsCollection).toEqual(expectedCollection);
    });

    it("should call annexDecision query and add missing value", () => {
      const expense: IExpense = { id: 9220 };
      const annexDecision: IAnnexDecision = { id: 18859 };
      expense.annexDecision = annexDecision;

      const annexDecisionCollection: IAnnexDecision[] = [{ id: 18859 }];
      jest
        .spyOn(annexDecisionService, "query")
        .mockReturnValue(
          of(new HttpResponse({ body: annexDecisionCollection })),
        );
      const expectedCollection: IAnnexDecision[] = [
        annexDecision,
        ...annexDecisionCollection,
      ];
      jest
        .spyOn(annexDecisionService, "addAnnexDecisionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(annexDecisionService.query).toHaveBeenCalled();
      expect(
        annexDecisionService.addAnnexDecisionToCollectionIfMissing,
      ).toHaveBeenCalledWith(annexDecisionCollection, annexDecision);
      expect(comp.annexDecisionsCollection).toEqual(expectedCollection);
    });

    it("should call Article query and add missing value", () => {
      const expense: IExpense = { id: 9220 };
      const articles: IArticle[] = [{ id: 24128 }];
      expense.articles = articles;

      const articleCollection: IArticle[] = [{ id: 24128 }];
      jest
        .spyOn(articleService, "query")
        .mockReturnValue(of(new HttpResponse({ body: articleCollection })));
      const additionalArticles = [...articles];
      const expectedCollection: IArticle[] = [
        ...additionalArticles,
        ...articleCollection,
      ];
      jest
        .spyOn(articleService, "addArticleToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(articleService.query).toHaveBeenCalled();
      expect(
        articleService.addArticleToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        articleCollection,
        ...additionalArticles.map(expect.objectContaining),
      );
      expect(comp.articlesSharedCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const expense: IExpense = { id: 9220 };
      const financialYear: IFinancialYear = { id: 14021 };
      expense.financialYear = financialYear;
      const annexDecision: IAnnexDecision = { id: 18859 };
      expense.annexDecision = annexDecision;
      const article: IArticle = { id: 24128 };
      expense.articles = [article];

      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      expect(comp.financialYearsCollection).toContainEqual(financialYear);
      expect(comp.annexDecisionsCollection).toContainEqual(annexDecision);
      expect(comp.articlesSharedCollection).toContainEqual(article);
      expect(comp.expense).toEqual(expense);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 17742 };
      jest.spyOn(expenseFormService, "getExpense").mockReturnValue(expense);
      jest.spyOn(expenseService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(expenseService.update).toHaveBeenCalledWith(
        expect.objectContaining(expense),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 17742 };
      jest
        .spyOn(expenseFormService, "getExpense")
        .mockReturnValue({ id: null });
      jest.spyOn(expenseService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ expense: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: expense }));
      saveSubject.complete();

      // THEN
      expect(expenseFormService.getExpense).toHaveBeenCalled();
      expect(expenseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IExpense>>();
      const expense = { id: 17742 };
      jest.spyOn(expenseService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ expense });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(expenseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Compare relationships", () => {
    describe("compareFinancialYear", () => {
      it("should forward to financialYearService", () => {
        const entity = { id: 14021 };
        const entity2 = { id: 23644 };
        jest.spyOn(financialYearService, "compareFinancialYear");
        comp.compareFinancialYear(entity, entity2);
        expect(financialYearService.compareFinancialYear).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });

    describe("compareAnnexDecision", () => {
      it("should forward to annexDecisionService", () => {
        const entity = { id: 18859 };
        const entity2 = { id: 13030 };
        jest.spyOn(annexDecisionService, "compareAnnexDecision");
        comp.compareAnnexDecision(entity, entity2);
        expect(annexDecisionService.compareAnnexDecision).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });

    describe("compareArticle", () => {
      it("should forward to articleService", () => {
        const entity = { id: 24128 };
        const entity2 = { id: 30377 };
        jest.spyOn(articleService, "compareArticle");
        comp.compareArticle(entity, entity2);
        expect(articleService.compareArticle).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });
  });
});
