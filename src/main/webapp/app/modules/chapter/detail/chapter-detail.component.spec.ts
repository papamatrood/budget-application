import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideRouter, withComponentInputBinding } from "@angular/router";
import { RouterTestingHarness } from "@angular/router/testing";
import { of } from "rxjs";

import { ChapterDetailComponent } from "./chapter-detail.component";

describe("Chapter Management Detail Component", () => {
  let comp: ChapterDetailComponent;
  let fixture: ComponentFixture<ChapterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ChapterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: "**",
              loadComponent: () =>
                import("./chapter-detail.component").then(
                  (m) => m.ChapterDetailComponent,
                ),
              resolve: { chapter: () => of({ id: 5578 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ChapterDetailComponent, "")
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ChapterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("Should load chapter on init", async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl("/", ChapterDetailComponent);

      // THEN
      expect(instance.chapter()).toEqual(expect.objectContaining({ id: 5578 }));
    });
  });

  describe("PreviousState", () => {
    it("Should navigate to previous state", () => {
      jest.spyOn(window.history, "back");
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
