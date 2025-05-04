import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IAnnexDecision } from '../annex-decision.model';

@Component({
  selector: 'jhi-annex-decision-detail',
  templateUrl: './annex-decision-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class AnnexDecisionDetailComponent {
  annexDecision = input<IAnnexDecision | null>(null);

  previousState(): void {
    window.history.back();
  }
}
