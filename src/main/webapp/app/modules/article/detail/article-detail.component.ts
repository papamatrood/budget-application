import { Component, input } from "@angular/core";
import { RouterModule } from "@angular/router";
import { IArticle } from "app/entities/article/article.model";

import SharedModule from "app/shared/shared.module";

@Component({
  selector: "jhi-article-detail",
  templateUrl: "./article-detail.component.html",
  imports: [SharedModule, RouterModule],
})
export class ArticleDetailComponent {
  article = input<IArticle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
