import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IChapter } from 'app/entities/chapter/chapter.model';
import { ChapterService } from 'app/entities/chapter/service/chapter.service';
import { IRecipe } from 'app/entities/recipe/recipe.model';
import { RecipeService } from 'app/entities/recipe/service/recipe.service';
import { IExpense } from 'app/entities/expense/expense.model';
import { ExpenseService } from 'app/entities/expense/service/expense.service';
import { IArticle } from '../article.model';
import { ArticleService } from '../service/article.service';
import { ArticleFormService } from './article-form.service';

import { ArticleUpdateComponent } from './article-update.component';

describe('Article Management Update Component', () => {
  let comp: ArticleUpdateComponent;
  let fixture: ComponentFixture<ArticleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let articleFormService: ArticleFormService;
  let articleService: ArticleService;
  let chapterService: ChapterService;
  let recipeService: RecipeService;
  let expenseService: ExpenseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ArticleUpdateComponent],
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
      .overrideTemplate(ArticleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ArticleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    articleFormService = TestBed.inject(ArticleFormService);
    articleService = TestBed.inject(ArticleService);
    chapterService = TestBed.inject(ChapterService);
    recipeService = TestBed.inject(RecipeService);
    expenseService = TestBed.inject(ExpenseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Chapter query and add missing value', () => {
      const article: IArticle = { id: 30377 };
      const chapter: IChapter = { id: 5578 };
      article.chapter = chapter;

      const chapterCollection: IChapter[] = [{ id: 5578 }];
      jest.spyOn(chapterService, 'query').mockReturnValue(of(new HttpResponse({ body: chapterCollection })));
      const additionalChapters = [chapter];
      const expectedCollection: IChapter[] = [...additionalChapters, ...chapterCollection];
      jest.spyOn(chapterService, 'addChapterToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(chapterService.query).toHaveBeenCalled();
      expect(chapterService.addChapterToCollectionIfMissing).toHaveBeenCalledWith(
        chapterCollection,
        ...additionalChapters.map(expect.objectContaining),
      );
      expect(comp.chaptersSharedCollection).toEqual(expectedCollection);
    });

    it('should call Recipe query and add missing value', () => {
      const article: IArticle = { id: 30377 };
      const recipes: IRecipe[] = [{ id: 27805 }];
      article.recipes = recipes;

      const recipeCollection: IRecipe[] = [{ id: 27805 }];
      jest.spyOn(recipeService, 'query').mockReturnValue(of(new HttpResponse({ body: recipeCollection })));
      const additionalRecipes = [...recipes];
      const expectedCollection: IRecipe[] = [...additionalRecipes, ...recipeCollection];
      jest.spyOn(recipeService, 'addRecipeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(recipeService.query).toHaveBeenCalled();
      expect(recipeService.addRecipeToCollectionIfMissing).toHaveBeenCalledWith(
        recipeCollection,
        ...additionalRecipes.map(expect.objectContaining),
      );
      expect(comp.recipesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Expense query and add missing value', () => {
      const article: IArticle = { id: 30377 };
      const expenses: IExpense[] = [{ id: 17742 }];
      article.expenses = expenses;

      const expenseCollection: IExpense[] = [{ id: 17742 }];
      jest.spyOn(expenseService, 'query').mockReturnValue(of(new HttpResponse({ body: expenseCollection })));
      const additionalExpenses = [...expenses];
      const expectedCollection: IExpense[] = [...additionalExpenses, ...expenseCollection];
      jest.spyOn(expenseService, 'addExpenseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(expenseService.query).toHaveBeenCalled();
      expect(expenseService.addExpenseToCollectionIfMissing).toHaveBeenCalledWith(
        expenseCollection,
        ...additionalExpenses.map(expect.objectContaining),
      );
      expect(comp.expensesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const article: IArticle = { id: 30377 };
      const chapter: IChapter = { id: 5578 };
      article.chapter = chapter;
      const recipe: IRecipe = { id: 27805 };
      article.recipes = [recipe];
      const expense: IExpense = { id: 17742 };
      article.expenses = [expense];

      activatedRoute.data = of({ article });
      comp.ngOnInit();

      expect(comp.chaptersSharedCollection).toContainEqual(chapter);
      expect(comp.recipesSharedCollection).toContainEqual(recipe);
      expect(comp.expensesSharedCollection).toContainEqual(expense);
      expect(comp.article).toEqual(article);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 24128 };
      jest.spyOn(articleFormService, 'getArticle').mockReturnValue(article);
      jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: article }));
      saveSubject.complete();

      // THEN
      expect(articleFormService.getArticle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(articleService.update).toHaveBeenCalledWith(expect.objectContaining(article));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 24128 };
      jest.spyOn(articleFormService, 'getArticle').mockReturnValue({ id: null });
      jest.spyOn(articleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: article }));
      saveSubject.complete();

      // THEN
      expect(articleFormService.getArticle).toHaveBeenCalled();
      expect(articleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IArticle>>();
      const article = { id: 24128 };
      jest.spyOn(articleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ article });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(articleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareChapter', () => {
      it('should forward to chapterService', () => {
        const entity = { id: 5578 };
        const entity2 = { id: 28081 };
        jest.spyOn(chapterService, 'compareChapter');
        comp.compareChapter(entity, entity2);
        expect(chapterService.compareChapter).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRecipe', () => {
      it('should forward to recipeService', () => {
        const entity = { id: 27805 };
        const entity2 = { id: 32502 };
        jest.spyOn(recipeService, 'compareRecipe');
        comp.compareRecipe(entity, entity2);
        expect(recipeService.compareRecipe).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareExpense', () => {
      it('should forward to expenseService', () => {
        const entity = { id: 17742 };
        const entity2 = { id: 9220 };
        jest.spyOn(expenseService, 'compareExpense');
        comp.compareExpense(entity, entity2);
        expect(expenseService.compareExpense).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
