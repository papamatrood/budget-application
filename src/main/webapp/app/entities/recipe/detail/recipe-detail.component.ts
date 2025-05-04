import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IRecipe } from '../recipe.model';

@Component({
  selector: 'jhi-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class RecipeDetailComponent {
  recipe = input<IRecipe | null>(null);

  previousState(): void {
    window.history.back();
  }
}
