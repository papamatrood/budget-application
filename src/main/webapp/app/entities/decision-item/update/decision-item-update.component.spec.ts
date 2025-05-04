import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { IDecision } from "app/entities/decision/decision.model";
import { DecisionService } from "app/entities/decision/service/decision.service";
import { DecisionItemService } from "../service/decision-item.service";
import { IDecisionItem } from "../decision-item.model";
import { DecisionItemFormService } from "./decision-item-form.service";

import { DecisionItemUpdateComponent } from "./decision-item-update.component";

describe("DecisionItem Management Update Component", () => {
  let comp: DecisionItemUpdateComponent;
  let fixture: ComponentFixture<DecisionItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let decisionItemFormService: DecisionItemFormService;
  let decisionItemService: DecisionItemService;
  let decisionService: DecisionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DecisionItemUpdateComponent],
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
      .overrideTemplate(DecisionItemUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(DecisionItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    decisionItemFormService = TestBed.inject(DecisionItemFormService);
    decisionItemService = TestBed.inject(DecisionItemService);
    decisionService = TestBed.inject(DecisionService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call Decision query and add missing value", () => {
      const decisionItem: IDecisionItem = { id: 6825 };
      const decision: IDecision = { id: 19132 };
      decisionItem.decision = decision;

      const decisionCollection: IDecision[] = [{ id: 19132 }];
      jest
        .spyOn(decisionService, "query")
        .mockReturnValue(of(new HttpResponse({ body: decisionCollection })));
      const additionalDecisions = [decision];
      const expectedCollection: IDecision[] = [
        ...additionalDecisions,
        ...decisionCollection,
      ];
      jest
        .spyOn(decisionService, "addDecisionToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ decisionItem });
      comp.ngOnInit();

      expect(decisionService.query).toHaveBeenCalled();
      expect(
        decisionService.addDecisionToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        decisionCollection,
        ...additionalDecisions.map(expect.objectContaining),
      );
      expect(comp.decisionsSharedCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const decisionItem: IDecisionItem = { id: 6825 };
      const decision: IDecision = { id: 19132 };
      decisionItem.decision = decision;

      activatedRoute.data = of({ decisionItem });
      comp.ngOnInit();

      expect(comp.decisionsSharedCollection).toContainEqual(decision);
      expect(comp.decisionItem).toEqual(decisionItem);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecisionItem>>();
      const decisionItem = { id: 9886 };
      jest
        .spyOn(decisionItemFormService, "getDecisionItem")
        .mockReturnValue(decisionItem);
      jest.spyOn(decisionItemService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decisionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decisionItem }));
      saveSubject.complete();

      // THEN
      expect(decisionItemFormService.getDecisionItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(decisionItemService.update).toHaveBeenCalledWith(
        expect.objectContaining(decisionItem),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecisionItem>>();
      const decisionItem = { id: 9886 };
      jest
        .spyOn(decisionItemFormService, "getDecisionItem")
        .mockReturnValue({ id: null });
      jest.spyOn(decisionItemService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decisionItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: decisionItem }));
      saveSubject.complete();

      // THEN
      expect(decisionItemFormService.getDecisionItem).toHaveBeenCalled();
      expect(decisionItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDecisionItem>>();
      const decisionItem = { id: 9886 };
      jest.spyOn(decisionItemService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ decisionItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(decisionItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Compare relationships", () => {
    describe("compareDecision", () => {
      it("should forward to decisionService", () => {
        const entity = { id: 19132 };
        const entity2 = { id: 4076 };
        jest.spyOn(decisionService, "compareDecision");
        comp.compareDecision(entity, entity2);
        expect(decisionService.compareDecision).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });
  });
});
