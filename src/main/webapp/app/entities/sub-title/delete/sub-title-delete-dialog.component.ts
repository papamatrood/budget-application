import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubTitle } from '../sub-title.model';
import { SubTitleService } from '../service/sub-title.service';

@Component({
  templateUrl: './sub-title-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubTitleDeleteDialogComponent {
  subTitle?: ISubTitle;

  protected subTitleService = inject(SubTitleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subTitleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
