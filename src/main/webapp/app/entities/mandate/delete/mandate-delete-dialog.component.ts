import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMandate } from '../mandate.model';
import { MandateService } from '../service/mandate.service';

@Component({
  templateUrl: './mandate-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MandateDeleteDialogComponent {
  mandate?: IMandate;

  protected mandateService = inject(MandateService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mandateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
