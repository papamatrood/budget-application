import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IChapter } from '../chapter.model';
import { ChapterService } from '../service/chapter.service';

@Component({
  templateUrl: './chapter-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ChapterDeleteDialogComponent {
  chapter?: IChapter;

  protected chapterService = inject(ChapterService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.chapterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
