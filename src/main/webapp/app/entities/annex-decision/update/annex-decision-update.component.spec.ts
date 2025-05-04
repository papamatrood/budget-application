import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IFinancialYear } from 'app/entities/financial-year/financial-year.model';
import { FinancialYearService } from 'app/entities/financial-year/service/financial-year.service';
import { AnnexDecisionService } from '../service/annex-decision.service';
import { IAnnexDecision } from '../annex-decision.model';
import { AnnexDecisionFormService } from './annex-decision-form.service';

import { AnnexDecisionUpdateComponent } from './annex-decision-update.component';

describe('AnnexDecision Management Update Component', () => {
  let comp: AnnexDecisionUpdateComponent;
  let fixture: ComponentFixture<AnnexDecisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let annexDecisionFormService: AnnexDecisionFormService;
  let annexDecisionService: AnnexDecisionService;
  let financialYearService: FinancialYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AnnexDecisionUpdateComponent],
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
      .overrideTemplate(AnnexDecisionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnnexDecisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    annexDecisionFormService = TestBed.inject(AnnexDecisionFormService);
    annexDecisionService = TestBed.inject(AnnexDecisionService);
    financialYearService = TestBed.inject(FinancialYearService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call financialYear query and add missing value', () => {
      const annexDecision: IAnnexDecision = { id: 13030 };
      const financialYear: IFinancialYear = { id: 14021 };
      annexDecision.financialYear = financialYear;

      const financialYearCollection: IFinancialYear[] = [{ id: 14021 }];
      jest.spyOn(financialYearService, 'query').mockReturnValue(of(new HttpResponse({ body: financialYearCollection })));
      const expectedCollection: IFinancialYear[] = [financialYear, ...financialYearCollection];
      jest.spyOn(financialYearService, 'addFinancialYearToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annexDecision });
      comp.ngOnInit();

      expect(financialYearService.query).toHaveBeenCalled();
      expect(financialYearService.addFinancialYearToCollectionIfMissing).toHaveBeenCalledWith(financialYearCollection, financialYear);
      expect(comp.financialYearsCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const annexDecision: IAnnexDecision = { id: 13030 };
      const financialYear: IFinancialYear = { id: 14021 };
      annexDecision.financialYear = financialYear;

      activatedRoute.data = of({ annexDecision });
      comp.ngOnInit();

      expect(comp.financialYearsCollection).toContainEqual(financialYear);
      expect(comp.annexDecision).toEqual(annexDecision);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnexDecision>>();
      const annexDecision = { id: 18859 };
      jest.spyOn(annexDecisionFormService, 'getAnnexDecision').mockReturnValue(annexDecision);
      jest.spyOn(annexDecisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annexDecision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annexDecision }));
      saveSubject.complete();

      // THEN
      expect(annexDecisionFormService.getAnnexDecision).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(annexDecisionService.update).toHaveBeenCalledWith(expect.objectContaining(annexDecision));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnexDecision>>();
      const annexDecision = { id: 18859 };
      jest.spyOn(annexDecisionFormService, 'getAnnexDecision').mockReturnValue({ id: null });
      jest.spyOn(annexDecisionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annexDecision: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annexDecision }));
      saveSubject.complete();

      // THEN
      expect(annexDecisionFormService.getAnnexDecision).toHaveBeenCalled();
      expect(annexDecisionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnexDecision>>();
      const annexDecision = { id: 18859 };
      jest.spyOn(annexDecisionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annexDecision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(annexDecisionService.update).toHaveBeenCalled();
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
  });
});
