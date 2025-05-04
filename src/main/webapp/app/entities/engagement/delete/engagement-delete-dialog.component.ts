import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEngagement } from '../engagement.model';
import { EngagementService } from '../service/engagement.service';

@Component({
  templateUrl: './engagement-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EngagementDeleteDialogComponent {
  engagement?: IEngagement;

  protected engagementService = inject(EngagementService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.engagementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
