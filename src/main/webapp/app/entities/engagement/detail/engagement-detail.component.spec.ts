import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EngagementDetailComponent } from './engagement-detail.component';

describe('Engagement Management Detail Component', () => {
  let comp: EngagementDetailComponent;
  let fixture: ComponentFixture<EngagementDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EngagementDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./engagement-detail.component').then(m => m.EngagementDetailComponent),
              resolve: { engagement: () => of({ id: 24171 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EngagementDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EngagementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load engagement on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EngagementDetailComponent);

      // THEN
      expect(instance.engagement()).toEqual(expect.objectContaining({ id: 24171 }));
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
