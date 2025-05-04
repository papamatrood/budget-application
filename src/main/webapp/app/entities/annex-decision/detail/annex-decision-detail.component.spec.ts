import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AnnexDecisionDetailComponent } from './annex-decision-detail.component';

describe('AnnexDecision Management Detail Component', () => {
  let comp: AnnexDecisionDetailComponent;
  let fixture: ComponentFixture<AnnexDecisionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnnexDecisionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./annex-decision-detail.component').then(m => m.AnnexDecisionDetailComponent),
              resolve: { annexDecision: () => of({ id: 18859 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AnnexDecisionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AnnexDecisionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load annexDecision on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AnnexDecisionDetailComponent);

      // THEN
      expect(instance.annexDecision()).toEqual(expect.objectContaining({ id: 18859 }));
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
