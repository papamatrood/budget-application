import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideRouter, withComponentInputBinding } from "@angular/router";
import { RouterTestingHarness } from "@angular/router/testing";
import { of } from "rxjs";

import { DecisionItemDetailComponent } from "./decision-item-detail.component";

describe("DecisionItem Management Detail Component", () => {
  let comp: DecisionItemDetailComponent;
  let fixture: ComponentFixture<DecisionItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DecisionItemDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: "**",
              loadComponent: () =>
                import("./decision-item-detail.component").then(
                  (m) => m.DecisionItemDetailComponent,
                ),
              resolve: { decisionItem: () => of({ id: 9886 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DecisionItemDetailComponent, "")
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("should load decisionItem on init", async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl(
        "/",
        DecisionItemDetailComponent,
      );

      // THEN
      expect(instance.decisionItem()).toEqual(
        expect.objectContaining({ id: 9886 }),
      );
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
