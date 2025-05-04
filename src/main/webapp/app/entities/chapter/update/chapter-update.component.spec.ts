import { ComponentFixture, TestBed } from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { FormBuilder } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { Subject, from, of } from "rxjs";

import { ISubTitle } from "app/entities/sub-title/sub-title.model";
import { SubTitleService } from "app/entities/sub-title/service/sub-title.service";
import { ChapterService } from "../service/chapter.service";
import { IChapter } from "../chapter.model";
import { ChapterFormService } from "./chapter-form.service";

import { ChapterUpdateComponent } from "./chapter-update.component";

describe("Chapter Management Update Component", () => {
  let comp: ChapterUpdateComponent;
  let fixture: ComponentFixture<ChapterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let chapterFormService: ChapterFormService;
  let chapterService: ChapterService;
  let subTitleService: SubTitleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChapterUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ChapterUpdateComponent, "")
      .compileComponents();

    fixture = TestBed.createComponent(ChapterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    chapterFormService = TestBed.inject(ChapterFormService);
    chapterService = TestBed.inject(ChapterService);
    subTitleService = TestBed.inject(SubTitleService);

    comp = fixture.componentInstance;
  });

  describe("ngOnInit", () => {
    it("should call SubTitle query and add missing value", () => {
      const chapter: IChapter = { id: 28081 };
      const subTitle: ISubTitle = { id: 2895 };
      chapter.subTitle = subTitle;

      const subTitleCollection: ISubTitle[] = [{ id: 2895 }];
      jest
        .spyOn(subTitleService, "query")
        .mockReturnValue(of(new HttpResponse({ body: subTitleCollection })));
      const additionalSubTitles = [subTitle];
      const expectedCollection: ISubTitle[] = [
        ...additionalSubTitles,
        ...subTitleCollection,
      ];
      jest
        .spyOn(subTitleService, "addSubTitleToCollectionIfMissing")
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ chapter });
      comp.ngOnInit();

      expect(subTitleService.query).toHaveBeenCalled();
      expect(
        subTitleService.addSubTitleToCollectionIfMissing,
      ).toHaveBeenCalledWith(
        subTitleCollection,
        ...additionalSubTitles.map(expect.objectContaining),
      );
      expect(comp.subTitlesSharedCollection).toEqual(expectedCollection);
    });

    it("should update editForm", () => {
      const chapter: IChapter = { id: 28081 };
      const subTitle: ISubTitle = { id: 2895 };
      chapter.subTitle = subTitle;

      activatedRoute.data = of({ chapter });
      comp.ngOnInit();

      expect(comp.subTitlesSharedCollection).toContainEqual(subTitle);
      expect(comp.chapter).toEqual(chapter);
    });
  });

  describe("save", () => {
    it("should call update service on save for existing entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChapter>>();
      const chapter = { id: 5578 };
      jest.spyOn(chapterFormService, "getChapter").mockReturnValue(chapter);
      jest.spyOn(chapterService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ chapter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chapter }));
      saveSubject.complete();

      // THEN
      expect(chapterFormService.getChapter).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(chapterService.update).toHaveBeenCalledWith(
        expect.objectContaining(chapter),
      );
      expect(comp.isSaving).toEqual(false);
    });

    it("should call create service on save for new entity", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChapter>>();
      const chapter = { id: 5578 };
      jest
        .spyOn(chapterFormService, "getChapter")
        .mockReturnValue({ id: null });
      jest.spyOn(chapterService, "create").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ chapter: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: chapter }));
      saveSubject.complete();

      // THEN
      expect(chapterFormService.getChapter).toHaveBeenCalled();
      expect(chapterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it("should set isSaving to false on error", () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IChapter>>();
      const chapter = { id: 5578 };
      jest.spyOn(chapterService, "update").mockReturnValue(saveSubject);
      jest.spyOn(comp, "previousState");
      activatedRoute.data = of({ chapter });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error("This is an error!");

      // THEN
      expect(chapterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe("Compare relationships", () => {
    describe("compareSubTitle", () => {
      it("should forward to subTitleService", () => {
        const entity = { id: 2895 };
        const entity2 = { id: 27234 };
        jest.spyOn(subTitleService, "compareSubTitle");
        comp.compareSubTitle(entity, entity2);
        expect(subTitleService.compareSubTitle).toHaveBeenCalledWith(
          entity,
          entity2,
        );
      });
    });
  });
});
