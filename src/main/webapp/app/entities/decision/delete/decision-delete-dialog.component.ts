import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDecision } from '../decision.model';
import { DecisionService } from '../service/decision.service';

@Component({
  templateUrl: './decision-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DecisionDeleteDialogComponent {
  decision?: IDecision;

  protected decisionService = inject(DecisionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.decisionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
