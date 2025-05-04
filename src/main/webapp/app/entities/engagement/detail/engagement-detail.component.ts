import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IEngagement } from '../engagement.model';

@Component({
  selector: 'jhi-engagement-detail',
  templateUrl: './engagement-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class EngagementDetailComponent {
  engagement = input<IEngagement | null>(null);

  previousState(): void {
    window.history.back();
  }
}
