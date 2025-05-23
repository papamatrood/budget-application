import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPurchaseOrder } from '../purchase-order.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../purchase-order.test-samples';

import { PurchaseOrderService, RestPurchaseOrder } from './purchase-order.service';

const requireRestSample: RestPurchaseOrder = {
  ...sampleWithRequiredData,
  orderDate: sampleWithRequiredData.orderDate?.toJSON(),
};

describe('PurchaseOrder Service', () => {
  let service: PurchaseOrderService;
  let httpMock: HttpTestingController;
  let expectedResult: IPurchaseOrder | IPurchaseOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PurchaseOrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PurchaseOrder', () => {
      const purchaseOrder = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(purchaseOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PurchaseOrder', () => {
      const purchaseOrder = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(purchaseOrder).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PurchaseOrder', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PurchaseOrder', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PurchaseOrder', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPurchaseOrderToCollectionIfMissing', () => {
      it('should add a PurchaseOrder to an empty array', () => {
        const purchaseOrder: IPurchaseOrder = sampleWithRequiredData;
        expectedResult = service.addPurchaseOrderToCollectionIfMissing([], purchaseOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrder);
      });

      it('should not add a PurchaseOrder to an array that contains it', () => {
        const purchaseOrder: IPurchaseOrder = sampleWithRequiredData;
        const purchaseOrderCollection: IPurchaseOrder[] = [
          {
            ...purchaseOrder,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPurchaseOrderToCollectionIfMissing(purchaseOrderCollection, purchaseOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchaseOrder to an array that doesn't contain it", () => {
        const purchaseOrder: IPurchaseOrder = sampleWithRequiredData;
        const purchaseOrderCollection: IPurchaseOrder[] = [sampleWithPartialData];
        expectedResult = service.addPurchaseOrderToCollectionIfMissing(purchaseOrderCollection, purchaseOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrder);
      });

      it('should add only unique PurchaseOrder to an array', () => {
        const purchaseOrderArray: IPurchaseOrder[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const purchaseOrderCollection: IPurchaseOrder[] = [sampleWithRequiredData];
        expectedResult = service.addPurchaseOrderToCollectionIfMissing(purchaseOrderCollection, ...purchaseOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const purchaseOrder: IPurchaseOrder = sampleWithRequiredData;
        const purchaseOrder2: IPurchaseOrder = sampleWithPartialData;
        expectedResult = service.addPurchaseOrderToCollectionIfMissing([], purchaseOrder, purchaseOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrder);
        expect(expectedResult).toContain(purchaseOrder2);
      });

      it('should accept null and undefined values', () => {
        const purchaseOrder: IPurchaseOrder = sampleWithRequiredData;
        expectedResult = service.addPurchaseOrderToCollectionIfMissing([], null, purchaseOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrder);
      });

      it('should return initial array if no PurchaseOrder is added', () => {
        const purchaseOrderCollection: IPurchaseOrder[] = [sampleWithRequiredData];
        expectedResult = service.addPurchaseOrderToCollectionIfMissing(purchaseOrderCollection, undefined, null);
        expect(expectedResult).toEqual(purchaseOrderCollection);
      });
    });

    describe('comparePurchaseOrder', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePurchaseOrder(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29828 };
        const entity2 = null;

        const compareResult1 = service.comparePurchaseOrder(entity1, entity2);
        const compareResult2 = service.comparePurchaseOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29828 };
        const entity2 = { id: 21921 };

        const compareResult1 = service.comparePurchaseOrder(entity1, entity2);
        const compareResult2 = service.comparePurchaseOrder(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29828 };
        const entity2 = { id: 29828 };

        const compareResult1 = service.comparePurchaseOrder(entity1, entity2);
        const compareResult2 = service.comparePurchaseOrder(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
