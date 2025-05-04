import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IDecisionItem } from '../decision-item.model';

@Component({
  selector: 'jhi-decision-item-detail',
  templateUrl: './decision-item-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class DecisionItemDetailComponent {
  decisionItem = input<IDecisionItem | null>(null);

  previousState(): void {
    window.history.back();
  }
}
