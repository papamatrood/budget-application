import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideRouter, withComponentInputBinding } from "@angular/router";
import { RouterTestingHarness } from "@angular/router/testing";
import { of } from "rxjs";

import { RecipeDetailComponent } from "./recipe-detail.component";

describe("Recipe Management Detail Component", () => {
  let comp: RecipeDetailComponent;
  let fixture: ComponentFixture<RecipeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecipeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: "**",
              loadComponent: () =>
                import("./recipe-detail.component").then(
                  (m) => m.RecipeDetailComponent,
                ),
              resolve: { recipe: () => of({ id: 27805 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RecipeDetailComponent, "")
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecipeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("should load recipe on init", async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl("/", RecipeDetailComponent);

      // THEN
      expect(instance.recipe()).toEqual(expect.objectContaining({ id: 27805 }));
    });
  });

  describe("PreviousState", () => {
    it("should navigate to previous state", () => {
      jest.spyOn(window.history, "back");
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
