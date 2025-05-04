import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFinancialYear } from '../financial-year.model';
import { FinancialYearService } from '../service/financial-year.service';

@Component({
  templateUrl: './financial-year-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FinancialYearDeleteDialogComponent {
  financialYear?: IFinancialYear;

  protected financialYearService = inject(FinancialYearService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.financialYearService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
