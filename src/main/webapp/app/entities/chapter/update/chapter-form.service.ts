import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IChapter, NewChapter } from '../chapter.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IChapter for edit and NewChapterFormGroupInput for create.
 */
type ChapterFormGroupInput = IChapter | PartialWithRequiredKeyOf<NewChapter>;

type ChapterFormDefaults = Pick<NewChapter, 'id'>;

type ChapterFormGroupContent = {
  id: FormControl<IChapter['id'] | NewChapter['id']>;
  code: FormControl<IChapter['code']>;
  designation: FormControl<IChapter['designation']>;
  subTitle: FormControl<IChapter['subTitle']>;
};

export type ChapterFormGroup = FormGroup<ChapterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ChapterFormService {
  createChapterFormGroup(chapter: ChapterFormGroupInput = { id: null }): ChapterFormGroup {
    const chapterRawValue = {
      ...this.getFormDefaults(),
      ...chapter,
    };
    return new FormGroup<ChapterFormGroupContent>({
      id: new FormControl(
        { value: chapterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(chapterRawValue.code, {
        validators: [Validators.required],
      }),
      designation: new FormControl(chapterRawValue.designation, {
        validators: [Validators.required],
      }),
      subTitle: new FormControl(chapterRawValue.subTitle),
    });
  }

  getChapter(form: ChapterFormGroup): IChapter | NewChapter {
    return form.getRawValue() as IChapter | NewChapter;
  }

  resetForm(form: ChapterFormGroup, chapter: ChapterFormGroupInput): void {
    const chapterRawValue = { ...this.getFormDefaults(), ...chapter };
    form.reset(
      {
        ...chapterRawValue,
        id: { value: chapterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ChapterFormDefaults {
    return {
      id: null,
    };
  }
}
