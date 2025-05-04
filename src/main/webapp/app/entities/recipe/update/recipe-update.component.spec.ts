import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/service/financial-year.service';
import { IArticle } from 'app/entities/article/article.model';
import { ArticleService } from 'app/entities/article/service/article.service';
import { IRecipe } from '../recipe.model';
import { RecipeService } from '../service/recipe.service';
import { RecipeFormService } from './recipe-form.service';

import { RecipeUpdateComponent } from './recipe-update.component';

describe('Recipe Management Update Component', () => {
  let comp: RecipeUpdateComponent;
  let fixture: ComponentFixture<RecipeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recipeFormService: RecipeFormService;
  let recipeService: RecipeService;
  let financialYearService: FinancialYearService;
  let articleService: ArticleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RecipeUpdateComponent],
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
      .overrideTemplate(RecipeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecipeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recipeFormService = TestBed.inject(RecipeFormService);
    recipeService = TestBed.inject(RecipeService);
    financialYearService = TestBed.inject(FinancialYearService);
    articleService = TestBed.inject(ArticleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call financialYear query and add missing value', () => {
      const recipe: IRecipe = { id: 32502 };
      const financialYear: IFinancialYear = { id: 14021 };
      recipe.financialYear = financialYear;

      const financialYearCollection: IFinancialYear[] = [{ id: 14021 }];
      jest.spyOn(financialYearService, 'query').mockReturnValue(of(new HttpResponse({ body: financialYearCollection })));
      const expectedCollection: IFinancialYear[] = [financialYear, ...financialYearCollection];
      jest.spyOn(financialYearService, 'addFinancialYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recipe });
      comp.ngOnInit();

      expect(financialYearService.query).toHaveBeenCalled();
      expect(financialYearService.addFinancialYearToCollectionIfMissing).toHaveBeenCalledWith(financialYearCollection, financialYear);
      expect(comp.financialYearsCollection).toEqual(expectedCollection);
    });

    it('should call Article query and add missing value', () => {
      const recipe: IRecipe = { id: 32502 };
      const articles: IArticle[] = [{ id: 24128 }];
      recipe.articles = articles;

      const articleCollection: IArticle[] = [{ id: 24128 }];
      jest.spyOn(articleService, 'query').mockReturnValue(of(new HttpResponse({ body: articleCollection })));
      const additionalArticles = [...articles];
      const expectedCollection: IArticle[] = [...additionalArticles, ...articleCollection];
      jest.spyOn(articleService, 'addArticleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recipe });
      comp.ngOnInit();

      expect(articleService.query).toHaveBeenCalled();
      expect(articleService.addArticleToCollectionIfMissing).toHaveBeenCalledWith(
        articleCollection,
        ...additionalArticles.map(expect.objectContaining),
      );
      expect(comp.articlesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const recipe: IRecipe = { id: 32502 };
      const financialYear: IFinancialYear = { id: 14021 };
      recipe.financialYear = financialYear;
      const article: IArticle = { id: 24128 };
      recipe.articles = [article];

      activatedRoute.data = of({ recipe });
      comp.ngOnInit();

      expect(comp.financialYearsCollection).toContainEqual(financialYear);
      expect(comp.articlesSharedCollection).toContainEqual(article);
      expect(comp.recipe).toEqual(recipe);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipe>>();
      const recipe = { id: 27805 };
      jest.spyOn(recipeFormService, 'getRecipe').mockReturnValue(recipe);
      jest.spyOn(recipeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recipe }));
      saveSubject.complete();

      // THEN
      expect(recipeFormService.getRecipe).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(recipeService.update).toHaveBeenCalledWith(expect.objectContaining(recipe));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipe>>();
      const recipe = { id: 27805 };
      jest.spyOn(recipeFormService, 'getRecipe').mockReturnValue({ id: null });
      jest.spyOn(recipeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recipe }));
      saveSubject.complete();

      // THEN
      expect(recipeFormService.getRecipe).toHaveBeenCalled();
      expect(recipeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRecipe>>();
      const recipe = { id: 27805 };
      jest.spyOn(recipeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recipe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recipeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareFinancialYear', () => {
      it('should forward to financialYearService', () => {
        const entity = { id: 14021 };
        const entity2 = { id: 23644 };
        jest.spyOn(financialYearService, 'compareFinancialYear');
        comp.compareFinancialYear(entity, entity2);
        expect(financialYearService.compareFinancialYear).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareArticle', () => {
      it('should forward to articleService', () => {
        const entity = { id: 24128 };
        const entity2 = { id: 30377 };
        jest.spyOn(articleService, 'compareArticle');
        comp.compareArticle(entity, entity2);
        expect(articleService.compareArticle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
