import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IFinancialYear } from '../financial-year.model';

@Component({
  selector: 'jhi-financial-year-detail',
  templateUrl: './financial-year-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class FinancialYearDetailComponent {
  financialYear = input<IFinancialYear | null>(null);

  previousState(): void {
    window.history.back();
  }
}
