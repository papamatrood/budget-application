import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { IEngagement } from "app/entities/engagement/engagement.model";
import { EngagementService } from "app/entities/engagement/service/engagement.service";
import { IAnnexDecision } from "app/entities/annex-decision/annex-decision.model";
import { AnnexDecisionService } from "app/entities/annex-decision/service/annex-decision.service";
import { IDecision } from "../decision.model";
import { DecisionService } from "../service/decision.service";
import { DecisionFormService } from "./decision-form.service";

import { DecisionUpdateComponent } from "./decision-update.component";

describe("Decision Management Update Component", () => {
  let comp: DecisionUpdateComponent;
  let fixture: ComponentFixture<DecisionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let decisionFormService: DecisionFormService;
  let decisionService: DecisionService;
  let engagementService: EngagementService;
  let annexDecisionService: AnnexDecisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DecisionUpdateComponent],
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
      .overrideTemplate(DecisionUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(DecisionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    decisionFormService = TestBed.inject(DecisionFormService);
    decisionService = TestBed.inject(DecisionService);
    engagementService = TestBed.inject(EngagementService);
    annexDecisionService = TestBed.inject(AnnexDecisionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call engagement query and add missing value", () => {
      const decision: IDecision = { id: 4076 };
      const engagement: IEngagement = { id: 24171 };
      decision.engagement = engagement;

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

      activatedRoute.data = of({ decision });
      comp.ngOnInit();

      expect(engagementService.query).toHaveBeenCalled();
      expect(
        engagementService.addEngagementToCollectionIfMissing,
      ).toHaveBeenCalledWith(engagementCollection, engagement);
      expect(comp.engagementsCollection).toEqual(expectedCollection);
    });

    it("should call AnnexDecision query and add missing value", () => {
      const decision: IDecision = { id: 4076 };
      const annexDecision: IAnnexDecision = { id: 18859 };
      decision.annexDecision = annexDecision;

      const annexDecisionCollection: IAnnexDecision[] = [{ id: 18859 }];
      jest
        .spyOn(annexDecisionService, "query")
        .mockReturnValue(
          of(new HttpResponse({ body: annexDecisionCollection })),
        );
      const additionalAnnexDecisions = [annexDecision];
      const expectedCollection: IAnnexDecision[] = [
        ...additionalAnnexDecisions,
        ...annexDecisionCollection,
      ];
      jest
        .spyOn(annexDecisionService, "addAnnexDecisionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decision });
      comp.ngOnInit();

      expect(annexDecisionService.query).toHaveBeenCalled();
      expect(
        annexDecisionService.addAnnexDecisionToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        annexDecisionCollection,
        ...additionalAnnexDecisions.map(expect.objectContaining),
      );
      expect(comp.annexDecisionsSharedCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const decision: IDecision = { id: 4076 };
      const engagement: IEngagement = { id: 24171 };
      decision.engagement = engagement;
      const annexDecision: IAnnexDecision = { id: 18859 };
      decision.annexDecision = annexDecision;

      activatedRoute.data = of({ decision });
      comp.ngOnInit();

      expect(comp.engagementsCollection).toContainEqual(engagement);
      expect(comp.annexDecisionsSharedCollection).toContainEqual(annexDecision);
      expect(comp.decision).toEqual(decision);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecision>>();
      const decision = { id: 19132 };
      jest.spyOn(decisionFormService, "getDecision").mockReturnValue(decision);
      jest.spyOn(decisionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decision }));
      saveSubject.complete();

      // THEN
      expect(decisionFormService.getDecision).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(decisionService.update).toHaveBeenCalledWith(
        expect.objectContaining(decision),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecision>>();
      const decision = { id: 19132 };
      jest
        .spyOn(decisionFormService, "getDecision")
        .mockReturnValue({ id: null });
      jest.spyOn(decisionService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decision: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decision }));
      saveSubject.complete();

      // THEN
      expect(decisionFormService.getDecision).toHaveBeenCalled();
      expect(decisionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecision>>();
      const decision = { id: 19132 };
      jest.spyOn(decisionService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decision });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(decisionService.update).toHaveBeenCalled();
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
  });
});
