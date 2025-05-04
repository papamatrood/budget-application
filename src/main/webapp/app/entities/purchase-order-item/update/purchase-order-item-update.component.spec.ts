import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPurchaseOrder } from 'app/entities/purchase-order/purchase-order.model';
import { PurchaseOrderService } from 'app/entities/purchase-order/service/purchase-order.service';
import { PurchaseOrderItemService } from '../service/purchase-order-item.service';
import { IPurchaseOrderItem } from '../purchase-order-item.model';
import { PurchaseOrderItemFormService } from './purchase-order-item-form.service';

import { PurchaseOrderItemUpdateComponent } from './purchase-order-item-update.component';

describe('PurchaseOrderItem Management Update Component', () => {
  let comp: PurchaseOrderItemUpdateComponent;
  let fixture: ComponentFixture<PurchaseOrderItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let purchaseOrderItemFormService: PurchaseOrderItemFormService;
  let purchaseOrderItemService: PurchaseOrderItemService;
  let purchaseOrderService: PurchaseOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PurchaseOrderItemUpdateComponent],
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
      .overrideTemplate(PurchaseOrderItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PurchaseOrderItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    purchaseOrderItemFormService = TestBed.inject(PurchaseOrderItemFormService);
    purchaseOrderItemService = TestBed.inject(PurchaseOrderItemService);
    purchaseOrderService = TestBed.inject(PurchaseOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call PurchaseOrder query and add missing value', () => {
      const purchaseOrderItem: IPurchaseOrderItem = { id: 27171 };
      const purchaseOrder: IPurchaseOrder = { id: 29828 };
      purchaseOrderItem.purchaseOrder = purchaseOrder;

      const purchaseOrderCollection: IPurchaseOrder[] = [{ id: 29828 }];
      jest.spyOn(purchaseOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: purchaseOrderCollection })));
      const additionalPurchaseOrders = [purchaseOrder];
      const expectedCollection: IPurchaseOrder[] = [...additionalPurchaseOrders, ...purchaseOrderCollection];
      jest.spyOn(purchaseOrderService, 'addPurchaseOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      expect(purchaseOrderService.query).toHaveBeenCalled();
      expect(purchaseOrderService.addPurchaseOrderToCollectionIfMissing).toHaveBeenCalledWith(
        purchaseOrderCollection,
        ...additionalPurchaseOrders.map(expect.objectContaining),
      );
      expect(comp.purchaseOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const purchaseOrderItem: IPurchaseOrderItem = { id: 27171 };
      const purchaseOrder: IPurchaseOrder = { id: 29828 };
      purchaseOrderItem.purchaseOrder = purchaseOrder;

      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      expect(comp.purchaseOrdersSharedCollection).toContainEqual(purchaseOrder);
      expect(comp.purchaseOrderItem).toEqual(purchaseOrderItem);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrderItem>>();
      const purchaseOrderItem = { id: 13347 };
      jest.spyOn(purchaseOrderItemFormService, 'getPurchaseOrderItem').mockReturnValue(purchaseOrderItem);
      jest.spyOn(purchaseOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderItem }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderItemFormService.getPurchaseOrderItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(purchaseOrderItemService.update).toHaveBeenCalledWith(expect.objectContaining(purchaseOrderItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrderItem>>();
      const purchaseOrderItem = { id: 13347 };
      jest.spyOn(purchaseOrderItemFormService, 'getPurchaseOrderItem').mockReturnValue({ id: null });
      jest.spyOn(purchaseOrderItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: purchaseOrderItem }));
      saveSubject.complete();

      // THEN
      expect(purchaseOrderItemFormService.getPurchaseOrderItem).toHaveBeenCalled();
      expect(purchaseOrderItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPurchaseOrderItem>>();
      const purchaseOrderItem = { id: 13347 };
      jest.spyOn(purchaseOrderItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ purchaseOrderItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(purchaseOrderItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePurchaseOrder', () => {
      it('should forward to purchaseOrderService', () => {
        const entity = { id: 29828 };
        const entity2 = { id: 21921 };
        jest.spyOn(purchaseOrderService, 'comparePurchaseOrder');
        comp.comparePurchaseOrder(entity, entity2);
        expect(purchaseOrderService.comparePurchaseOrder).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
