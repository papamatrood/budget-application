import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SubTitleService } from '../service/sub-title.service';
import { ISubTitle } from '../sub-title.model';
import { SubTitleFormService } from './sub-title-form.service';

import { SubTitleUpdateComponent } from './sub-title-update.component';

describe('SubTitle Management Update Component', () => {
  let comp: SubTitleUpdateComponent;
  let fixture: ComponentFixture<SubTitleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let subTitleFormService: SubTitleFormService;
  let subTitleService: SubTitleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubTitleUpdateComponent],
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
      .overrideTemplate(SubTitleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubTitleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    subTitleFormService = TestBed.inject(SubTitleFormService);
    subTitleService = TestBed.inject(SubTitleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const subTitle: ISubTitle = { id: 27234 };

      activatedRoute.data = of({ subTitle });
      comp.ngOnInit();

      expect(comp.subTitle).toEqual(subTitle);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubTitle>>();
      const subTitle = { id: 2895 };
      jest.spyOn(subTitleFormService, 'getSubTitle').mockReturnValue(subTitle);
      jest.spyOn(subTitleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subTitle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subTitle }));
      saveSubject.complete();

      // THEN
      expect(subTitleFormService.getSubTitle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(subTitleService.update).toHaveBeenCalledWith(expect.objectContaining(subTitle));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubTitle>>();
      const subTitle = { id: 2895 };
      jest.spyOn(subTitleFormService, 'getSubTitle').mockReturnValue({ id: null });
      jest.spyOn(subTitleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subTitle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: subTitle }));
      saveSubject.complete();

      // THEN
      expect(subTitleFormService.getSubTitle).toHaveBeenCalled();
      expect(subTitleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubTitle>>();
      const subTitle = { id: 2895 };
      jest.spyOn(subTitleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ subTitle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(subTitleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
