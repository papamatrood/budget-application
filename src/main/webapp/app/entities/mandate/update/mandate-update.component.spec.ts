import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { IEngagement } from "app/entities/engagement/engagement.model";
import { EngagementService } from "app/entities/engagement/service/engagement.service";
import { MandateService } from "../service/mandate.service";
import { IMandate } from "../mandate.model";
import { MandateFormService } from "./mandate-form.service";

import { MandateUpdateComponent } from "./mandate-update.component";

describe("Mandate Management Update Component", () => {
  let comp: MandateUpdateComponent;
  let fixture: ComponentFixture<MandateUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mandateFormService: MandateFormService;
  let mandateService: MandateService;
  let engagementService: EngagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MandateUpdateComponent],
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
      .overrideTemplate(MandateUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(MandateUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mandateFormService = TestBed.inject(MandateFormService);
    mandateService = TestBed.inject(MandateService);
    engagementService = TestBed.inject(EngagementService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call engagement query and add missing value", () => {
      const mandate: IMandate = { id: 18821 };
      const engagement: IEngagement = { id: 24171 };
      mandate.engagement = engagement;

      const engagementCollection: IEngagement[] = [{ id: 24171 }];
      jest
        .spyOn(engagementService, "query")
        .mockReturnValue(of(new HttpResponse({ body: engagementCollection })));
      const expectedCollection: IEngagement[] = [
        engagement,
        ...engagementCollection,
      ];
      jest
        .spyOn(engagementService, "addEngagementToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mandate });
      comp.ngOnInit();

      expect(engagementService.query).toHaveBeenCalled();
      expect(
        engagementService.addEngagementToCollectionIfMissing,
      ).toHaveBeenCalledWith(engagementCollection, engagement);
      expect(comp.engagementsCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const mandate: IMandate = { id: 18821 };
      const engagement: IEngagement = { id: 24171 };
      mandate.engagement = engagement;

      activatedRoute.data = of({ mandate });
      comp.ngOnInit();

      expect(comp.engagementsCollection).toContainEqual(engagement);
      expect(comp.mandate).toEqual(mandate);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMandate>>();
      const mandate = { id: 5755 };
      jest.spyOn(mandateFormService, "getMandate").mockReturnValue(mandate);
      jest.spyOn(mandateService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ mandate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mandate }));
      saveSubject.complete();

      // THEN
      expect(mandateFormService.getMandate).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mandateService.update).toHaveBeenCalledWith(
        expect.objectContaining(mandate),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMandate>>();
      const mandate = { id: 5755 };
      jest
        .spyOn(mandateFormService, "getMandate")
        .mockReturnValue({ id: null });
      jest.spyOn(mandateService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ mandate: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mandate }));
      saveSubject.complete();

      // THEN
      expect(mandateFormService.getMandate).toHaveBeenCalled();
      expect(mandateService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMandate>>();
      const mandate = { id: 5755 };
      jest.spyOn(mandateService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ mandate });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(mandateService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Compare relationships", () => {
    describe("compareEngagement", () => {
      it("should forward to engagementService", () => {
        const entity = { id: 24171 };
        const entity2 = { id: 18750 };
        jest.spyOn(engagementService, "compareEngagement");
        comp.compareEngagement(entity, entity2);
        expect(engagementService.compareEngagement).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });
  });
});
