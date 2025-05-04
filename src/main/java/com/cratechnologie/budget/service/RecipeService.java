package com.cratechnologie.budget.service;

import com.cratechnologie.budget.domain.Recipe;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.cratechnologie.budget.domain.Recipe}.
 */
public interface RecipeService {
    /**
     * Save a recipe.
     *
     * @param recipe the entity to save.
     * @return the persisted entity.
     */
    Recipe save(Recipe recipe);

    /**
     * Updates a recipe.
     *
     * @param recipe the entity to update.
     * @return the persisted entity.
     */
    Recipe update(Recipe recipe);

    /**
     * Partially updates a recipe.
     *
     * @param recipe the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Recipe> partialUpdate(Recipe recipe);

    /**
     * Get the "id" recipe.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Recipe> findOne(Long id);

    /**
     * Delete the "id" recipe.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
