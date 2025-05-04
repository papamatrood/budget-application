import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { DecisionDetailComponent } from './decision-detail.component';

describe('Decision Management Detail Component', () => {
  let comp: DecisionDetailComponent;
  let fixture: ComponentFixture<DecisionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DecisionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./decision-detail.component').then(m => m.DecisionDetailComponent),
              resolve: { decision: () => of({ id: 19132 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DecisionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load decision on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DecisionDetailComponent);

      // THEN
      expect(instance.decision()).toEqual(expect.objectContaining({ id: 19132 }));
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
