import { TestBed } from "@angular/core/testing";
import {
  HttpTestingController,
  provideHttpClientTesting,
} from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";

import { IPurchaseOrderItem } from "../purchase-order-item.model";
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from "../purchase-order-item.test-samples";

import { PurchaseOrderItemService } from "./purchase-order-item.service";

const requireRestSample: IPurchaseOrderItem = {
  ...sampleWithRequiredData,
};

describe("PurchaseOrderItem Service", () => {
  let service: PurchaseOrderItemService;
  let httpMock: HttpTestingController;
  let expectedResult:
    | IPurchaseOrderItem
    | IPurchaseOrderItem[]
    | boolean
    | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PurchaseOrderItemService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe("Service methods", () => {
    it("should find an element", () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should create a PurchaseOrderItem", () => {
      const purchaseOrderItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .create(purchaseOrderItem)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a PurchaseOrderItem", () => {
      const purchaseOrderItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .update(purchaseOrderItem)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a PurchaseOrderItem", () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .partialUpdate(patchObject)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PATCH" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should return a list of PurchaseOrderItem", () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it("should delete a PurchaseOrderItem", () => {
      const expected = true;

      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe("addPurchaseOrderItemToCollectionIfMissing", () => {
      it("should add a PurchaseOrderItem to an empty array", () => {
        const purchaseOrderItem: IPurchaseOrderItem = sampleWithRequiredData;
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          [],
          purchaseOrderItem,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it("should not add a PurchaseOrderItem to an array that contains it", () => {
        const purchaseOrderItem: IPurchaseOrderItem = sampleWithRequiredData;
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [
          {
            ...purchaseOrderItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          purchaseOrderItemCollection,
          purchaseOrderItem,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PurchaseOrderItem to an array that doesn't contain it", () => {
        const purchaseOrderItem: IPurchaseOrderItem = sampleWithRequiredData;
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [
          sampleWithPartialData,
        ];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          purchaseOrderItemCollection,
          purchaseOrderItem,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it("should add only unique PurchaseOrderItem to an array", () => {
        const purchaseOrderItemArray: IPurchaseOrderItem[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          purchaseOrderItemCollection,
          ...purchaseOrderItemArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const purchaseOrderItem: IPurchaseOrderItem = sampleWithRequiredData;
        const purchaseOrderItem2: IPurchaseOrderItem = sampleWithPartialData;
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          [],
          purchaseOrderItem,
          purchaseOrderItem2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(purchaseOrderItem);
        expect(expectedResult).toContain(purchaseOrderItem2);
      });

      it("should accept null and undefined values", () => {
        const purchaseOrderItem: IPurchaseOrderItem = sampleWithRequiredData;
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          [],
          null,
          purchaseOrderItem,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(purchaseOrderItem);
      });

      it("should return initial array if no PurchaseOrderItem is added", () => {
        const purchaseOrderItemCollection: IPurchaseOrderItem[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addPurchaseOrderItemToCollectionIfMissing(
          purchaseOrderItemCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(purchaseOrderItemCollection);
      });
    });

    describe("comparePurchaseOrderItem", () => {
      it("should return true if both entities are null", () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePurchaseOrderItem(
          entity1,
          entity2,
        );

        expect(compareResult).toEqual(true);
      });

      it("should return false if one entity is null", () => {
        const entity1 = { id: 13347 };
        const entity2 = null;

        const compareResult1 = service.comparePurchaseOrderItem(
          entity1,
          entity2,
        );
        const compareResult2 = service.comparePurchaseOrderItem(
          entity2,
          entity1,
        );

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey differs", () => {
        const entity1 = { id: 13347 };
        const entity2 = { id: 27171 };

        const compareResult1 = service.comparePurchaseOrderItem(
          entity1,
          entity2,
        );
        const compareResult2 = service.comparePurchaseOrderItem(
          entity2,
          entity1,
        );

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey matches", () => {
        const entity1 = { id: 13347 };
        const entity2 = { id: 13347 };

        const compareResult1 = service.comparePurchaseOrderItem(
          entity1,
          entity2,
        );
        const compareResult2 = service.comparePurchaseOrderItem(
          entity2,
          entity1,
        );

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
