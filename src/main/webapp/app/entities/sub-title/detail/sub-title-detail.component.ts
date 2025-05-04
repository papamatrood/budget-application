import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISubTitle } from '../sub-title.model';

@Component({
  selector: 'jhi-sub-title-detail',
  templateUrl: './sub-title-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SubTitleDetailComponent {
  subTitle = input<ISubTitle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
