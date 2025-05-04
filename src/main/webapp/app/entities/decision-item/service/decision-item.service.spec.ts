import { TestBed } from "@angular/core/testing";
import {
  HttpTestingController,
  provideHttpClientTesting,
} from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";

import { IDecisionItem } from "../decision-item.model";
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from "../decision-item.test-samples";

import { DecisionItemService } from "./decision-item.service";

const requireRestSample: IDecisionItem = {
  ...sampleWithRequiredData,
};

describe("DecisionItem Service", () => {
  let service: DecisionItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IDecisionItem | IDecisionItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DecisionItemService);
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

    it("should create a DecisionItem", () => {
      const decisionItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .create(decisionItem)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a DecisionItem", () => {
      const decisionItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .update(decisionItem)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a DecisionItem", () => {
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

    it("should return a list of DecisionItem", () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it("should delete a DecisionItem", () => {
      const expected = true;

      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe("addDecisionItemToCollectionIfMissing", () => {
      it("should add a DecisionItem to an empty array", () => {
        const decisionItem: IDecisionItem = sampleWithRequiredData;
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          [],
          decisionItem,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decisionItem);
      });

      it("should not add a DecisionItem to an array that contains it", () => {
        const decisionItem: IDecisionItem = sampleWithRequiredData;
        const decisionItemCollection: IDecisionItem[] = [
          {
            ...decisionItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          decisionItemCollection,
          decisionItem,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DecisionItem to an array that doesn't contain it", () => {
        const decisionItem: IDecisionItem = sampleWithRequiredData;
        const decisionItemCollection: IDecisionItem[] = [sampleWithPartialData];
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          decisionItemCollection,
          decisionItem,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decisionItem);
      });

      it("should add only unique DecisionItem to an array", () => {
        const decisionItemArray: IDecisionItem[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const decisionItemCollection: IDecisionItem[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          decisionItemCollection,
          ...decisionItemArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const decisionItem: IDecisionItem = sampleWithRequiredData;
        const decisionItem2: IDecisionItem = sampleWithPartialData;
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          [],
          decisionItem,
          decisionItem2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decisionItem);
        expect(expectedResult).toContain(decisionItem2);
      });

      it("should accept null and undefined values", () => {
        const decisionItem: IDecisionItem = sampleWithRequiredData;
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          [],
          null,
          decisionItem,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decisionItem);
      });

      it("should return initial array if no DecisionItem is added", () => {
        const decisionItemCollection: IDecisionItem[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addDecisionItemToCollectionIfMissing(
          decisionItemCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(decisionItemCollection);
      });
    });

    describe("compareDecisionItem", () => {
      it("should return true if both entities are null", () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDecisionItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it("should return false if one entity is null", () => {
        const entity1 = { id: 9886 };
        const entity2 = null;

        const compareResult1 = service.compareDecisionItem(entity1, entity2);
        const compareResult2 = service.compareDecisionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey differs", () => {
        const entity1 = { id: 9886 };
        const entity2 = { id: 6825 };

        const compareResult1 = service.compareDecisionItem(entity1, entity2);
        const compareResult2 = service.compareDecisionItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey matches", () => {
        const entity1 = { id: 9886 };
        const entity2 = { id: 9886 };

        const compareResult1 = service.compareDecisionItem(entity1, entity2);
        const compareResult2 = service.compareDecisionItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
