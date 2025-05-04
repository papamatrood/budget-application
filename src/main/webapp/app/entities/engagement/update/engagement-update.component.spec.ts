import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EngagementService } from '../service/engagement.service';
import { IEngagement } from '../engagement.model';
import { EngagementFormService } from './engagement-form.service';

import { EngagementUpdateComponent } from './engagement-update.component';

describe('Engagement Management Update Component', () => {
  let comp: EngagementUpdateComponent;
  let fixture: ComponentFixture<EngagementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let engagementFormService: EngagementFormService;
  let engagementService: EngagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EngagementUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EngagementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EngagementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    engagementFormService = TestBed.inject(EngagementFormService);
    engagementService = TestBed.inject(EngagementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const engagement: IEngagement = { id: 18750 };

      activatedRoute.data = of({ engagement });
      comp.ngOnInit();

      expect(comp.engagement).toEqual(engagement);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngagement>>();
      const engagement = { id: 24171 };
      jest.spyOn(engagementFormService, 'getEngagement').mockReturnValue(engagement);
      jest.spyOn(engagementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engagement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: engagement }));
      saveSubject.complete();

      // THEN
      expect(engagementFormService.getEngagement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(engagementService.update).toHaveBeenCalledWith(expect.objectContaining(engagement));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngagement>>();
      const engagement = { id: 24171 };
      jest.spyOn(engagementFormService, 'getEngagement').mockReturnValue({ id: null });
      jest.spyOn(engagementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engagement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: engagement }));
      saveSubject.complete();

      // THEN
      expect(engagementFormService.getEngagement).toHaveBeenCalled();
      expect(engagementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEngagement>>();
      const engagement = { id: 24171 };
      jest.spyOn(engagementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ engagement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(engagementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
