import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IFinancialYear } from '../financial-year.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../financial-year.test-samples';

import { FinancialYearService } from './financial-year.service';

const requireRestSample: IFinancialYear = {
  ...sampleWithRequiredData,
};

describe('FinancialYear Service', () => {
  let service: FinancialYearService;
  let httpMock: HttpTestingController;
  let expectedResult: IFinancialYear | IFinancialYear[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(FinancialYearService);
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

    it('should create a FinancialYear', () => {
      const financialYear = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(financialYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FinancialYear', () => {
      const financialYear = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(financialYear).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FinancialYear', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FinancialYear', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a FinancialYear', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addFinancialYearToCollectionIfMissing', () => {
      it('should add a FinancialYear to an empty array', () => {
        const financialYear: IFinancialYear = sampleWithRequiredData;
        expectedResult = service.addFinancialYearToCollectionIfMissing([], financialYear);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialYear);
      });

      it('should not add a FinancialYear to an array that contains it', () => {
        const financialYear: IFinancialYear = sampleWithRequiredData;
        const financialYearCollection: IFinancialYear[] = [
          {
            ...financialYear,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addFinancialYearToCollectionIfMissing(financialYearCollection, financialYear);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FinancialYear to an array that doesn't contain it", () => {
        const financialYear: IFinancialYear = sampleWithRequiredData;
        const financialYearCollection: IFinancialYear[] = [sampleWithPartialData];
        expectedResult = service.addFinancialYearToCollectionIfMissing(financialYearCollection, financialYear);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialYear);
      });

      it('should add only unique FinancialYear to an array', () => {
        const financialYearArray: IFinancialYear[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const financialYearCollection: IFinancialYear[] = [sampleWithRequiredData];
        expectedResult = service.addFinancialYearToCollectionIfMissing(financialYearCollection, ...financialYearArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const financialYear: IFinancialYear = sampleWithRequiredData;
        const financialYear2: IFinancialYear = sampleWithPartialData;
        expectedResult = service.addFinancialYearToCollectionIfMissing([], financialYear, financialYear2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(financialYear);
        expect(expectedResult).toContain(financialYear2);
      });

      it('should accept null and undefined values', () => {
        const financialYear: IFinancialYear = sampleWithRequiredData;
        expectedResult = service.addFinancialYearToCollectionIfMissing([], null, financialYear, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(financialYear);
      });

      it('should return initial array if no FinancialYear is added', () => {
        const financialYearCollection: IFinancialYear[] = [sampleWithRequiredData];
        expectedResult = service.addFinancialYearToCollectionIfMissing(financialYearCollection, undefined, null);
        expect(expectedResult).toEqual(financialYearCollection);
      });
    });

    describe('compareFinancialYear', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareFinancialYear(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 14021 };
        const entity2 = null;

        const compareResult1 = service.compareFinancialYear(entity1, entity2);
        const compareResult2 = service.compareFinancialYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 14021 };
        const entity2 = { id: 23644 };

        const compareResult1 = service.compareFinancialYear(entity1, entity2);
        const compareResult2 = service.compareFinancialYear(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 14021 };
        const entity2 = { id: 14021 };

        const compareResult1 = service.compareFinancialYear(entity1, entity2);
        const compareResult2 = service.compareFinancialYear(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
