import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IChapter } from '../chapter.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../chapter.test-samples';

import { ChapterService } from './chapter.service';

const requireRestSample: IChapter = {
  ...sampleWithRequiredData,
};

describe('Chapter Service', () => {
  let service: ChapterService;
  let httpMock: HttpTestingController;
  let expectedResult: IChapter | IChapter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ChapterService);
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

    it('should create a Chapter', () => {
      const chapter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(chapter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Chapter', () => {
      const chapter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(chapter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Chapter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Chapter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Chapter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addChapterToCollectionIfMissing', () => {
      it('should add a Chapter to an empty array', () => {
        const chapter: IChapter = sampleWithRequiredData;
        expectedResult = service.addChapterToCollectionIfMissing([], chapter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chapter);
      });

      it('should not add a Chapter to an array that contains it', () => {
        const chapter: IChapter = sampleWithRequiredData;
        const chapterCollection: IChapter[] = [
          {
            ...chapter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addChapterToCollectionIfMissing(chapterCollection, chapter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Chapter to an array that doesn't contain it", () => {
        const chapter: IChapter = sampleWithRequiredData;
        const chapterCollection: IChapter[] = [sampleWithPartialData];
        expectedResult = service.addChapterToCollectionIfMissing(chapterCollection, chapter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chapter);
      });

      it('should add only unique Chapter to an array', () => {
        const chapterArray: IChapter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const chapterCollection: IChapter[] = [sampleWithRequiredData];
        expectedResult = service.addChapterToCollectionIfMissing(chapterCollection, ...chapterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const chapter: IChapter = sampleWithRequiredData;
        const chapter2: IChapter = sampleWithPartialData;
        expectedResult = service.addChapterToCollectionIfMissing([], chapter, chapter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(chapter);
        expect(expectedResult).toContain(chapter2);
      });

      it('should accept null and undefined values', () => {
        const chapter: IChapter = sampleWithRequiredData;
        expectedResult = service.addChapterToCollectionIfMissing([], null, chapter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(chapter);
      });

      it('should return initial array if no Chapter is added', () => {
        const chapterCollection: IChapter[] = [sampleWithRequiredData];
        expectedResult = service.addChapterToCollectionIfMissing(chapterCollection, undefined, null);
        expect(expectedResult).toEqual(chapterCollection);
      });
    });

    describe('compareChapter', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareChapter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5578 };
        const entity2 = null;

        const compareResult1 = service.compareChapter(entity1, entity2);
        const compareResult2 = service.compareChapter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5578 };
        const entity2 = { id: 28081 };

        const compareResult1 = service.compareChapter(entity1, entity2);
        const compareResult2 = service.compareChapter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5578 };
        const entity2 = { id: 5578 };

        const compareResult1 = service.compareChapter(entity1, entity2);
        const compareResult2 = service.compareChapter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
