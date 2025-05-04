import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { FinancialYearService } from "../service/financial-year.service";
import { IFinancialYear } from "../financial-year.model";
import { FinancialYearFormService } from "./financial-year-form.service";

import { FinancialYearUpdateComponent } from "./financial-year-update.component";

describe("FinancialYear Management Update Component", () => {
  let comp: FinancialYearUpdateComponent;
  let fixture: ComponentFixture<FinancialYearUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let financialYearFormService: FinancialYearFormService;
  let financialYearService: FinancialYearService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [FinancialYearUpdateComponent],
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
      .overrideTemplate(FinancialYearUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(FinancialYearUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    financialYearFormService = TestBed.inject(FinancialYearFormService);
    financialYearService = TestBed.inject(FinancialYearService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should update editForm", () => {
      const financialYear: IFinancialYear = { id: 23644 };

      activatedRoute.data = of({ financialYear });
      comp.ngOnInit();

      expect(comp.financialYear).toEqual(financialYear);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialYear>>();
      const financialYear = { id: 14021 };
      jest
        .spyOn(financialYearFormService, "getFinancialYear")
        .mockReturnValue(financialYear);
      jest.spyOn(financialYearService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ financialYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialYear }));
      saveSubject.complete();

      // THEN
      expect(financialYearFormService.getFinancialYear).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(financialYearService.update).toHaveBeenCalledWith(
        expect.objectContaining(financialYear),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialYear>>();
      const financialYear = { id: 14021 };
      jest
        .spyOn(financialYearFormService, "getFinancialYear")
        .mockReturnValue({ id: null });
      jest.spyOn(financialYearService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ financialYear: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: financialYear }));
      saveSubject.complete();

      // THEN
      expect(financialYearFormService.getFinancialYear).toHaveBeenCalled();
      expect(financialYearService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFinancialYear>>();
      const financialYear = { id: 14021 };
      jest.spyOn(financialYearService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ financialYear });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(financialYearService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
