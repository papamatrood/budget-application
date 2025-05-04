import { ComponentFixture, TestBed } from "@angular/core/testing";
import { provideRouter, withComponentInputBinding } from "@angular/router";
import { RouterTestingHarness } from "@angular/router/testing";
import { of } from "rxjs";

import { PurchaseOrderItemDetailComponent } from "./purchase-order-item-detail.component";

describe("PurchaseOrderItem Management Detail Component", () => {
  let comp: PurchaseOrderItemDetailComponent;
  let fixture: ComponentFixture<PurchaseOrderItemDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PurchaseOrderItemDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: "**",
              loadComponent: () =>
                import("./purchase-order-item-detail.component").then(
                  (m) => m.PurchaseOrderItemDetailComponent,
                ),
              resolve: { purchaseOrderItem: () => of({ id: 13347 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PurchaseOrderItemDetailComponent, "")
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PurchaseOrderItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe("OnInit", () => {
    it("should load purchaseOrderItem on init", async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl(
        "/",
        PurchaseOrderItemDetailComponent,
      );

      // THEN
      expect(instance.purchaseOrderItem()).toEqual(
        expect.objectContaining({ id: 13347 }),
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
