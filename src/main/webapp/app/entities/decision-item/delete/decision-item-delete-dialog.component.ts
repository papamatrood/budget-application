import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDecisionItem } from '../decision-item.model';
import { DecisionItemService } from '../service/decision-item.service';

@Component({
  templateUrl: './decision-item-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DecisionItemDeleteDialogComponent {
  decisionItem?: IDecisionItem;

  protected decisionItemService = inject(DecisionItemService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decisionItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
