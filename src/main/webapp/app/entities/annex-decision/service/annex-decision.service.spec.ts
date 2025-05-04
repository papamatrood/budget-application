import { TestBed } from "@angular/core/testing";
import {
  HttpTestingController,
  provideHttpClientTesting,
} from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";

import { IAnnexDecision } from "../annex-decision.model";
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from "../annex-decision.test-samples";

import { AnnexDecisionService } from "./annex-decision.service";

const requireRestSample: IAnnexDecision = {
  ...sampleWithRequiredData,
};

describe("AnnexDecision Service", () => {
  let service: AnnexDecisionService;
  let httpMock: HttpTestingController;
  let expectedResult: IAnnexDecision | IAnnexDecision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AnnexDecisionService);
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

    it("should create a AnnexDecision", () => {
      const annexDecision = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .create(annexDecision)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a AnnexDecision", () => {
      const annexDecision = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .update(annexDecision)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a AnnexDecision", () => {
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

    it("should return a list of AnnexDecision", () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it("should delete a AnnexDecision", () => {
      const expected = true;

      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe("addAnnexDecisionToCollectionIfMissing", () => {
      it("should add a AnnexDecision to an empty array", () => {
        const annexDecision: IAnnexDecision = sampleWithRequiredData;
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          [],
          annexDecision,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annexDecision);
      });

      it("should not add a AnnexDecision to an array that contains it", () => {
        const annexDecision: IAnnexDecision = sampleWithRequiredData;
        const annexDecisionCollection: IAnnexDecision[] = [
          {
            ...annexDecision,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          annexDecisionCollection,
          annexDecision,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AnnexDecision to an array that doesn't contain it", () => {
        const annexDecision: IAnnexDecision = sampleWithRequiredData;
        const annexDecisionCollection: IAnnexDecision[] = [
          sampleWithPartialData,
        ];
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          annexDecisionCollection,
          annexDecision,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annexDecision);
      });

      it("should add only unique AnnexDecision to an array", () => {
        const annexDecisionArray: IAnnexDecision[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const annexDecisionCollection: IAnnexDecision[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          annexDecisionCollection,
          ...annexDecisionArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const annexDecision: IAnnexDecision = sampleWithRequiredData;
        const annexDecision2: IAnnexDecision = sampleWithPartialData;
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          [],
          annexDecision,
          annexDecision2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annexDecision);
        expect(expectedResult).toContain(annexDecision2);
      });

      it("should accept null and undefined values", () => {
        const annexDecision: IAnnexDecision = sampleWithRequiredData;
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          [],
          null,
          annexDecision,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annexDecision);
      });

      it("should return initial array if no AnnexDecision is added", () => {
        const annexDecisionCollection: IAnnexDecision[] = [
          sampleWithRequiredData,
        ];
        expectedResult = service.addAnnexDecisionToCollectionIfMissing(
          annexDecisionCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(annexDecisionCollection);
      });
    });

    describe("compareAnnexDecision", () => {
      it("should return true if both entities are null", () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAnnexDecision(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it("should return false if one entity is null", () => {
        const entity1 = { id: 18859 };
        const entity2 = null;

        const compareResult1 = service.compareAnnexDecision(entity1, entity2);
        const compareResult2 = service.compareAnnexDecision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey differs", () => {
        const entity1 = { id: 18859 };
        const entity2 = { id: 13030 };

        const compareResult1 = service.compareAnnexDecision(entity1, entity2);
        const compareResult2 = service.compareAnnexDecision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey matches", () => {
        const entity1 = { id: 18859 };
        const entity2 = { id: 18859 };

        const compareResult1 = service.compareAnnexDecision(entity1, entity2);
        const compareResult2 = service.compareAnnexDecision(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
