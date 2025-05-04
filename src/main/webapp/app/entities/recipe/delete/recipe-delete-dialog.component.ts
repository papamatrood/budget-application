import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRecipe } from '../recipe.model';
import { RecipeService } from '../service/recipe.service';

@Component({
  templateUrl: './recipe-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RecipeDeleteDialogComponent {
  recipe?: IRecipe;

  protected recipeService = inject(RecipeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recipeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
