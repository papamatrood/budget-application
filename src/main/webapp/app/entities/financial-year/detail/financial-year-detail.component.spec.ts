import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { FinancialYearDetailComponent } from './financial-year-detail.component';

describe('FinancialYear Management Detail Component', () => {
  let comp: FinancialYearDetailComponent;
  let fixture: ComponentFixture<FinancialYearDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FinancialYearDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./financial-year-detail.component').then(m => m.FinancialYearDetailComponent),
              resolve: { financialYear: () => of({ id: 14021 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FinancialYearDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinancialYearDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load financialYear on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FinancialYearDetailComponent);

      // THEN
      expect(instance.financialYear()).toEqual(expect.objectContaining({ id: 14021 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
