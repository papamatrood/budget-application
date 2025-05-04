import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IMandate } from '../mandate.model';

@Component({
  selector: 'jhi-mandate-detail',
  templateUrl: './mandate-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class MandateDetailComponent {
  mandate = input<IMandate | null>(null);

  previousState(): void {
    window.history.back();
  }
}
