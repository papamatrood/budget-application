import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideRouter, withComponentInputBinding } from "@angular/router";
import { RouterTestingHarness } from "@angular/router/testing";
import { of } from "rxjs";

import { SubTitleDetailComponent } from "./sub-title-detail.component";

describe("SubTitle Management Detail Component", () => {
  let comp: SubTitleDetailComponent;
  let fixture: ComponentFixture<SubTitleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubTitleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: "**",
              loadComponent: () =>
                import("./sub-title-detail.component").then(
                  (m) => m.SubTitleDetailComponent,
                ),
              resolve: { subTitle: () => of({ id: 2895 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubTitleDetailComponent, "")
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubTitleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("Should load subTitle on init", async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl(
        "/",
        SubTitleDetailComponent,
      );

      // THEN
      expect(instance.subTitle()).toEqual(
        expect.objectContaining({ id: 2895 }),
      );
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
