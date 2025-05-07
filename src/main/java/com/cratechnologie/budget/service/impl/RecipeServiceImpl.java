package com.cratechnologie.budget.service.impl;

import com.cratechnologie.budget.service.RecipeService;
import com.cratechnologie.budget.domain.Recipe;
import com.cratechnologie.budget.repository.RecipeRepository;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.cratechnologie.budget.domain.Recipe}.
 */
@Service
@Transactional
public class RecipeServiceImpl implements RecipeService {

    private static final Logger LOG = LoggerFactory.getLogger(RecipeServiceImpl.class);

    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Recipe save(Recipe recipe) {
        LOG.debug("Request to save Recipe : {}", recipe);
        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe update(Recipe recipe) {
        LOG.debug("Request to update Recipe : {}", recipe);
        return recipeRepository.save(recipe);
    }

    @Override
    public Optional<Recipe> partialUpdate(Recipe recipe) {
        LOG.debug("Request to partially update Recipe : {}", recipe);

        return recipeRepository
            .findById(recipe.getId())
            .map(existingRecipe -> {
                if (recipe.getAchievementsInThePastYear() != null) {
                    existingRecipe.setAchievementsInThePastYear(recipe.getAchievementsInThePastYear());
                }
                if (recipe.getNewYearForecast() != null) {
                    existingRecipe.setNewYearForecast(recipe.getNewYearForecast());
                }
                if (recipe.getCategory() != null) {
                    existingRecipe.setCategory(recipe.getCategory());
                }

                return existingRecipe;
            })
            .map(recipeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipe> findOne(Long id) {
        LOG.debug("Request to get Recipe : {}", id);
        return recipeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Recipe : {}", id);
        recipeRepository.deleteById(id);
    }
}
