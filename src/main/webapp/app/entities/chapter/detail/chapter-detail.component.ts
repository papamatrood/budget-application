import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IChapter } from '../chapter.model';

@Component({
  selector: 'jhi-chapter-detail',
  templateUrl: './chapter-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ChapterDetailComponent {
  chapter = input<IChapter | null>(null);

  previousState(): void {
    window.history.back();
  }
}
