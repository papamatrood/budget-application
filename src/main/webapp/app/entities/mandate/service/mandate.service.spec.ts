import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IMandate } from '../mandate.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../mandate.test-samples';

import { MandateService, RestMandate } from './mandate.service';

const requireRestSample: RestMandate = {
  ...sampleWithRequiredData,
  mandateDate: sampleWithRequiredData.mandateDate?.toJSON(),
};

describe('Mandate Service', () => {
  let service: MandateService;
  let httpMock: HttpTestingController;
  let expectedResult: IMandate | IMandate[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(MandateService);
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

    it('should create a Mandate', () => {
      const mandate = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(mandate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Mandate', () => {
      const mandate = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(mandate).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Mandate', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Mandate', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Mandate', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMandateToCollectionIfMissing', () => {
      it('should add a Mandate to an empty array', () => {
        const mandate: IMandate = sampleWithRequiredData;
        expectedResult = service.addMandateToCollectionIfMissing([], mandate);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mandate);
      });

      it('should not add a Mandate to an array that contains it', () => {
        const mandate: IMandate = sampleWithRequiredData;
        const mandateCollection: IMandate[] = [
          {
            ...mandate,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, mandate);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Mandate to an array that doesn't contain it", () => {
        const mandate: IMandate = sampleWithRequiredData;
        const mandateCollection: IMandate[] = [sampleWithPartialData];
        expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, mandate);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mandate);
      });

      it('should add only unique Mandate to an array', () => {
        const mandateArray: IMandate[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const mandateCollection: IMandate[] = [sampleWithRequiredData];
        expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, ...mandateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const mandate: IMandate = sampleWithRequiredData;
        const mandate2: IMandate = sampleWithPartialData;
        expectedResult = service.addMandateToCollectionIfMissing([], mandate, mandate2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(mandate);
        expect(expectedResult).toContain(mandate2);
      });

      it('should accept null and undefined values', () => {
        const mandate: IMandate = sampleWithRequiredData;
        expectedResult = service.addMandateToCollectionIfMissing([], null, mandate, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(mandate);
      });

      it('should return initial array if no Mandate is added', () => {
        const mandateCollection: IMandate[] = [sampleWithRequiredData];
        expectedResult = service.addMandateToCollectionIfMissing(mandateCollection, undefined, null);
        expect(expectedResult).toEqual(mandateCollection);
      });
    });

    describe('compareMandate', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMandate(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5755 };
        const entity2 = null;

        const compareResult1 = service.compareMandate(entity1, entity2);
        const compareResult2 = service.compareMandate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5755 };
        const entity2 = { id: 18821 };

        const compareResult1 = service.compareMandate(entity1, entity2);
        const compareResult2 = service.compareMandate(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5755 };
        const entity2 = { id: 5755 };

        const compareResult1 = service.compareMandate(entity1, entity2);
        const compareResult2 = service.compareMandate(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
