import { TestBed } from "@angular/core/testing";
import {
  HttpTestingController,
  provideHttpClientTesting,
} from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";

import { ISubTitle } from "../sub-title.model";
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from "../sub-title.test-samples";

import { SubTitleService } from "./sub-title.service";

const requireRestSample: ISubTitle = {
  ...sampleWithRequiredData,
};

describe("SubTitle Service", () => {
  let service: SubTitleService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubTitle | ISubTitle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SubTitleService);
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

    it("should create a SubTitle", () => {
      const subTitle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .create(subTitle)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "POST" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should update a SubTitle", () => {
      const subTitle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service
        .update(subTitle)
        .subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "PUT" });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it("should partial update a SubTitle", () => {
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

    it("should return a list of SubTitle", () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe((resp) => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: "GET" });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it("should delete a SubTitle", () => {
      const expected = true;

      service.delete(123).subscribe((resp) => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: "DELETE" });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe("addSubTitleToCollectionIfMissing", () => {
      it("should add a SubTitle to an empty array", () => {
        const subTitle: ISubTitle = sampleWithRequiredData;
        expectedResult = service.addSubTitleToCollectionIfMissing([], subTitle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subTitle);
      });

      it("should not add a SubTitle to an array that contains it", () => {
        const subTitle: ISubTitle = sampleWithRequiredData;
        const subTitleCollection: ISubTitle[] = [
          {
            ...subTitle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubTitleToCollectionIfMissing(
          subTitleCollection,
          subTitle,
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubTitle to an array that doesn't contain it", () => {
        const subTitle: ISubTitle = sampleWithRequiredData;
        const subTitleCollection: ISubTitle[] = [sampleWithPartialData];
        expectedResult = service.addSubTitleToCollectionIfMissing(
          subTitleCollection,
          subTitle,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subTitle);
      });

      it("should add only unique SubTitle to an array", () => {
        const subTitleArray: ISubTitle[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const subTitleCollection: ISubTitle[] = [sampleWithRequiredData];
        expectedResult = service.addSubTitleToCollectionIfMissing(
          subTitleCollection,
          ...subTitleArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it("should accept varargs", () => {
        const subTitle: ISubTitle = sampleWithRequiredData;
        const subTitle2: ISubTitle = sampleWithPartialData;
        expectedResult = service.addSubTitleToCollectionIfMissing(
          [],
          subTitle,
          subTitle2,
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(subTitle);
        expect(expectedResult).toContain(subTitle2);
      });

      it("should accept null and undefined values", () => {
        const subTitle: ISubTitle = sampleWithRequiredData;
        expectedResult = service.addSubTitleToCollectionIfMissing(
          [],
          null,
          subTitle,
          undefined,
        );
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(subTitle);
      });

      it("should return initial array if no SubTitle is added", () => {
        const subTitleCollection: ISubTitle[] = [sampleWithRequiredData];
        expectedResult = service.addSubTitleToCollectionIfMissing(
          subTitleCollection,
          undefined,
          null,
        );
        expect(expectedResult).toEqual(subTitleCollection);
      });
    });

    describe("compareSubTitle", () => {
      it("should return true if both entities are null", () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubTitle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it("should return false if one entity is null", () => {
        const entity1 = { id: 2895 };
        const entity2 = null;

        const compareResult1 = service.compareSubTitle(entity1, entity2);
        const compareResult2 = service.compareSubTitle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey differs", () => {
        const entity1 = { id: 2895 };
        const entity2 = { id: 27234 };

        const compareResult1 = service.compareSubTitle(entity1, entity2);
        const compareResult2 = service.compareSubTitle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it("should return false if primaryKey matches", () => {
        const entity1 = { id: 2895 };
        const entity2 = { id: 2895 };

        const compareResult1 = service.compareSubTitle(entity1, entity2);
        const compareResult2 = service.compareSubTitle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
