import { TestBed } from "@angular/core/testing";
import {
  HttpTestingController,
  provideHttpClientTesting,
} from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";

import { IEngagement } from "../engagement.model";
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from "../engagement.test-samples";

import { EngagementService, RestEngagement } from "./engagement.service";

const requireRestSample: RestEngagement = {
  ...sampleWithRequiredData,
  engagementDate: sampleWithRequiredData.engagementDate?.toJSON(),
};

describe("Engagement Service", () => {
  let service: EngagementService;
  let httpMock: HttpTestingController;
  let expectedResult: IEngagement | IEngagement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EngagementService);
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

    it("should create a Engagement", () => {
      const engagement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .create(engagement)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a Engagement", () => {
      const engagement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .update(engagement)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a Engagement", () => {
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

    it("should return a list of Engagement", () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it("should delete a Engagement", () => {
      const expected = true;

      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe("addEngagementToCollectionIfMissing", () => {
      it("should add a Engagement to an empty array", () => {
        const engagement: IEngagement = sampleWithRequiredData;
        expectedResult = service.addEngagementToCollectionIfMissing(
          [],
          engagement,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(engagement);
      });

      it("should not add a Engagement to an array that contains it", () => {
        const engagement: IEngagement = sampleWithRequiredData;
        const engagementCollection: IEngagement[] = [
          {
            ...engagement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEngagementToCollectionIfMissing(
          engagementCollection,
          engagement,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Engagement to an array that doesn't contain it", () => {
        const engagement: IEngagement = sampleWithRequiredData;
        const engagementCollection: IEngagement[] = [sampleWithPartialData];
        expectedResult = service.addEngagementToCollectionIfMissing(
          engagementCollection,
          engagement,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(engagement);
      });

      it("should add only unique Engagement to an array", () => {
        const engagementArray: IEngagement[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const engagementCollection: IEngagement[] = [sampleWithRequiredData];
        expectedResult = service.addEngagementToCollectionIfMissing(
          engagementCollection,
          ...engagementArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const engagement: IEngagement = sampleWithRequiredData;
        const engagement2: IEngagement = sampleWithPartialData;
        expectedResult = service.addEngagementToCollectionIfMissing(
          [],
          engagement,
          engagement2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(engagement);
        expect(expectedResult).toContain(engagement2);
      });

      it("should accept null and undefined values", () => {
        const engagement: IEngagement = sampleWithRequiredData;
        expectedResult = service.addEngagementToCollectionIfMissing(
          [],
          null,
          engagement,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(engagement);
      });

      it("should return initial array if no Engagement is added", () => {
        const engagementCollection: IEngagement[] = [sampleWithRequiredData];
        expectedResult = service.addEngagementToCollectionIfMissing(
          engagementCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(engagementCollection);
      });
    });

    describe("compareEngagement", () => {
      it("should return true if both entities are null", () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEngagement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it("should return false if one entity is null", () => {
        const entity1 = { id: 24171 };
        const entity2 = null;

        const compareResult1 = service.compareEngagement(entity1, entity2);
        const compareResult2 = service.compareEngagement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey differs", () => {
        const entity1 = { id: 24171 };
        const entity2 = { id: 18750 };

        const compareResult1 = service.compareEngagement(entity1, entity2);
        const compareResult2 = service.compareEngagement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey matches", () => {
        const entity1 = { id: 24171 };
        const entity2 = { id: 24171 };

        const compareResult1 = service.compareEngagement(entity1, entity2);
        const compareResult2 = service.compareEngagement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
