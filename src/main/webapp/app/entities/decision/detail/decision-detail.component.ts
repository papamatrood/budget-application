import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IDecision } from '../decision.model';

@Component({
  selector: 'jhi-decision-detail',
  templateUrl: './decision-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class DecisionDetailComponent {
  decision = input<IDecision | null>(null);

  previousState(): void {
    window.history.back();
  }
}
