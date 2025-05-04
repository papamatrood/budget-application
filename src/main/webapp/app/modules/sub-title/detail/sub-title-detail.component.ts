import { Component, input } from "@angular/core";
import { RouterModule } from "@angular/router";
import { ISubTitle } from "app/entities/sub-title/sub-title.model";

import SharedModule from "app/shared/shared.module";

@Component({
  selector: "jhi-sub-title-detail",
  templateUrl: "./sub-title-detail.component.html",
  imports: [SharedModule, RouterModule],
})
export class SubTitleDetailComponent {
  subTitle = input<ISubTitle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
