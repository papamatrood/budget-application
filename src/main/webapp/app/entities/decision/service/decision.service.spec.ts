import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDecision } from '../decision.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../decision.test-samples';

import { DecisionService, RestDecision } from './decision.service';

const requireRestSample: RestDecision = {
  ...sampleWithRequiredData,
  decisionDate: sampleWithRequiredData.decisionDate?.toJSON(),
};

describe('Decision Service', () => {
  let service: DecisionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDecision | IDecision[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DecisionService);
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

    it('should create a Decision', () => {
      const decision = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(decision).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Decision', () => {
      const decision = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(decision).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Decision', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Decision', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Decision', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDecisionToCollectionIfMissing', () => {
      it('should add a Decision to an empty array', () => {
        const decision: IDecision = sampleWithRequiredData;
        expectedResult = service.addDecisionToCollectionIfMissing([], decision);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decision);
      });

      it('should not add a Decision to an array that contains it', () => {
        const decision: IDecision = sampleWithRequiredData;
        const decisionCollection: IDecision[] = [
          {
            ...decision,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, decision);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Decision to an array that doesn't contain it", () => {
        const decision: IDecision = sampleWithRequiredData;
        const decisionCollection: IDecision[] = [sampleWithPartialData];
        expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, decision);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decision);
      });

      it('should add only unique Decision to an array', () => {
        const decisionArray: IDecision[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const decisionCollection: IDecision[] = [sampleWithRequiredData];
        expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, ...decisionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const decision: IDecision = sampleWithRequiredData;
        const decision2: IDecision = sampleWithPartialData;
        expectedResult = service.addDecisionToCollectionIfMissing([], decision, decision2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(decision);
        expect(expectedResult).toContain(decision2);
      });

      it('should accept null and undefined values', () => {
        const decision: IDecision = sampleWithRequiredData;
        expectedResult = service.addDecisionToCollectionIfMissing([], null, decision, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(decision);
      });

      it('should return initial array if no Decision is added', () => {
        const decisionCollection: IDecision[] = [sampleWithRequiredData];
        expectedResult = service.addDecisionToCollectionIfMissing(decisionCollection, undefined, null);
        expect(expectedResult).toEqual(decisionCollection);
      });
    });

    describe('compareDecision', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDecision(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 19132 };
        const entity2 = null;

        const compareResult1 = service.compareDecision(entity1, entity2);
        const compareResult2 = service.compareDecision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 19132 };
        const entity2 = { id: 4076 };

        const compareResult1 = service.compareDecision(entity1, entity2);
        const compareResult2 = service.compareDecision(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 19132 };
        const entity2 = { id: 19132 };

        const compareResult1 = service.compareDecision(entity1, entity2);
        const compareResult2 = service.compareDecision(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
