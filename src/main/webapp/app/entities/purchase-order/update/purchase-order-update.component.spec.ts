import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { IAnnexDecision } from "app/entities/annex-decision/annex-decision.model";
import { AnnexDecisionService } from "app/entities/annex-decision/service/annex-decision.service";
import { ISupplier } from "app/entities/supplier/supplier.model";
import { SupplierService } from "app/entities/supplier/service/supplier.service";
import { IEngagement } from "app/entities/engagement/engagement.model";
import { EngagementService } from "app/entities/engagement/service/engagement.service";
import { IPurchaseOrder } from "../purchase-order.model";
import { PurchaseOrderService } from "../service/purchase-order.service";
import { PurchaseOrderFormService } from "./purchase-order-form.service";

import { PurchaseOrderUpdateComponent } from "./purchase-order-update.component";

describe("PurchaseOrder Management Update Component", () => {
  let comp: PurchaseOrderUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderFormService: PurchaseOrderFormService;
  let purchaseOrderService: PurchaseOrderService;
  let annexDecisionService: AnnexDecisionService;
  let supplierService: SupplierService;
  let engagementService: EngagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PurchaseOrderUpdateComponent],
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
      .overrideTemplate(PurchaseOrderUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderFormService = TestBed.inject(PurchaseOrderFormService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);
    annexDecisionService = TestBed.inject(AnnexDecisionService);
    supplierService = TestBed.inject(SupplierService);
    engagementService = TestBed.inject(EngagementService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call AnnexDecision query and add missing value", () => {
      const purchaseOrder: IPurchaseOrder = { id: 21921 };
      const annexDecision: IAnnexDecision = { id: 18859 };
      purchaseOrder.annexDecision = annexDecision;

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

      activatedRoute.data = of({ purchaseOrder });
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

    it("should call Supplier query and add missing value", () => {
      const purchaseOrder: IPurchaseOrder = { id: 21921 };
      const supplier: ISupplier = { id: 28889 };
      purchaseOrder.supplier = supplier;

      const supplierCollection: ISupplier[] = [{ id: 28889 }];
      jest
        .spyOn(supplierService, "query")
        .mockReturnValue(of(new HttpResponse({ body: supplierCollection })));
      const additionalSuppliers = [supplier];
      const expectedCollection: ISupplier[] = [
        ...additionalSuppliers,
        ...supplierCollection,
      ];
      jest
        .spyOn(supplierService, "addSupplierToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(supplierService.query).toHaveBeenCalled();
      expect(
        supplierService.addSupplierToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        supplierCollection,
        ...additionalSuppliers.map(expect.objectContaining),
      );
      expect(comp.suppliersSharedCollection).toEqual(expectedCollection);
    });

    it("should call Engagement query and add missing value", () => {
      const purchaseOrder: IPurchaseOrder = { id: 21921 };
      const engagement: IEngagement = { id: 24171 };
      purchaseOrder.engagement = engagement;

      const engagementCollection: IEngagement[] = [{ id: 24171 }];
      jest
        .spyOn(engagementService, "query")
        .mockReturnValue(of(new HttpResponse({ body: engagementCollection })));
      const additionalEngagements = [engagement];
      const expectedCollection: IEngagement[] = [
        ...additionalEngagements,
        ...engagementCollection,
      ];
      jest
        .spyOn(engagementService, "addEngagementToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(engagementService.query).toHaveBeenCalled();
      expect(
        engagementService.addEngagementToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        engagementCollection,
        ...additionalEngagements.map(expect.objectContaining),
      );
      expect(comp.engagementsSharedCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const purchaseOrder: IPurchaseOrder = { id: 21921 };
      const annexDecision: IAnnexDecision = { id: 18859 };
      purchaseOrder.annexDecision = annexDecision;
      const supplier: ISupplier = { id: 28889 };
      purchaseOrder.supplier = supplier;
      const engagement: IEngagement = { id: 24171 };
      purchaseOrder.engagement = engagement;

      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      expect(comp.annexDecisionsSharedCollection).toContainEqual(annexDecision);
      expect(comp.suppliersSharedCollection).toContainEqual(supplier);
      expect(comp.engagementsSharedCollection).toContainEqual(engagement);
      expect(comp.purchaseOrder).toEqual(purchaseOrder);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrder>>();
      const purchaseOrder = { id: 29828 };
      jest
        .spyOn(purchaseOrderFormService, "getPurchaseOrder")
        .mockReturnValue(purchaseOrder);
      jest.spyOn(purchaseOrderService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderFormService.getPurchaseOrder).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderService.update).toHaveBeenCalledWith(
        expect.objectContaining(purchaseOrder),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrder>>();
      const purchaseOrder = { id: 29828 };
      jest
        .spyOn(purchaseOrderFormService, "getPurchaseOrder")
        .mockReturnValue({ id: null });
      jest.spyOn(purchaseOrderService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ purchaseOrder: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrder }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderFormService.getPurchaseOrder).toHaveBeenCalled();
      expect(purchaseOrderService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrder>>();
      const purchaseOrder = { id: 29828 };
      jest.spyOn(purchaseOrderService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ purchaseOrder });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(purchaseOrderService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Compare relationships", () => {
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

    describe("compareSupplier", () => {
      it("should forward to supplierService", () => {
        const entity = { id: 28889 };
        const entity2 = { id: 5063 };
        jest.spyOn(supplierService, "compareSupplier");
        comp.compareSupplier(entity, entity2);
        expect(supplierService.compareSupplier).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });

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
