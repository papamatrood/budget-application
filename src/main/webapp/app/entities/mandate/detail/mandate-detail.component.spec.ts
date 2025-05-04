import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { MandateDetailComponent } from './mandate-detail.component';

describe('Mandate Management Detail Component', () => {
  let comp: MandateDetailComponent;
  let fixture: ComponentFixture<MandateDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MandateDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./mandate-detail.component').then(m => m.MandateDetailComponent),
              resolve: { mandate: () => of({ id: 5755 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MandateDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(MandateDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load mandate on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MandateDetailComponent);

      // THEN
      expect(instance.mandate()).toEqual(expect.objectContaining({ id: 5755 }));
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
