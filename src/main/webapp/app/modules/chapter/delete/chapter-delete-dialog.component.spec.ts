jest.mock("@ng-bootstrap/ng-bootstrap");

import {
  ComponentFixture,
  TestBed,
  fakeAsync,
  inject,
  tick,
} from "@angular/core/testing";
import { HttpResponse, provideHttpClient } from "@angular/common/http";
import { of } from "rxjs";
import { NgbActiveModal } from "@ng-bootstrap/ng-bootstrap";

import { ChapterDeleteDialogComponent } from "./chapter-delete-dialog.component";
import { ChapterService } from "app/entities/chapter/service/chapter.service";

describe("Chapter Management Delete Component", () => {
  let comp: ChapterDeleteDialogComponent;
  let fixture: ComponentFixture<ChapterDeleteDialogComponent>;
  let service: ChapterService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ChapterDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(ChapterDeleteDialogComponent, "")
      .compileComponents();
    fixture = TestBed.createComponent(ChapterDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ChapterService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe("confirmDelete", () => {
    it("Should call delete service on confirmDelete", inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest
          .spyOn(service, "delete")
          .mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith("deleted");
      }),
    ));

    it("Should not call delete service on clear", () => {
      // GIVEN
      jest.spyOn(service, "delete");

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
