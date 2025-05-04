package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.RecipeAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Article;
import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.domain.Recipe;
import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import com.cratechnologie.budget.repository.RecipeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RecipeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RecipeResourceIT {

    private static final Integer DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR = 1;
    private static final Integer UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR = 2;
    private static final Integer SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR = 1 - 1;

    private static final Integer DEFAULT_NEW_YEAR_FORECAST = 1;
    private static final Integer UPDATED_NEW_YEAR_FORECAST = 2;
    private static final Integer SMALLER_NEW_YEAR_FORECAST = 1 - 1;

    private static final FinancialCategoryEnum DEFAULT_CATEGORY = FinancialCategoryEnum.OPERATING_RECIPE;
    private static final FinancialCategoryEnum UPDATED_CATEGORY = FinancialCategoryEnum.INVESTMENT_RECIPE;

    private static final String ENTITY_API_URL = "/api/recipes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    private Recipe insertedRecipe;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createEntity() {
        return new Recipe()
            .achievementsInThePastYear(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(DEFAULT_NEW_YEAR_FORECAST)
            .category(DEFAULT_CATEGORY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Recipe createUpdatedEntity() {
        return new Recipe()
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);
    }

    @BeforeEach
    void initTest() {
        recipe = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRecipe != null) {
            recipeRepository.delete(insertedRecipe);
            insertedRecipe = null;
        }
    }

    @Test
    @Transactional
    void createRecipe() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Recipe
        var returnedRecipe = om.readValue(
            restRecipeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipe)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Recipe.class
        );

        // Validate the Recipe in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertRecipeUpdatableFieldsEquals(returnedRecipe, getPersistedRecipe(returnedRecipe));

        insertedRecipe = returnedRecipe;
    }

    @Test
    @Transactional
    void createRecipeWithExistingId() throws Exception {
        // Create the Recipe with an existing ID
        recipe.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecipeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipe)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecipes() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievementsInThePastYear").value(hasItem(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR)))
            .andExpect(jsonPath("$.[*].newYearForecast").value(hasItem(DEFAULT_NEW_YEAR_FORECAST)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    void getRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get the recipe
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL_ID, recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recipe.getId().intValue()))
            .andExpect(jsonPath("$.achievementsInThePastYear").value(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR))
            .andExpect(jsonPath("$.newYearForecast").value(DEFAULT_NEW_YEAR_FORECAST))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    void getRecipesByIdFiltering() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        Long id = recipe.getId();

        defaultRecipeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRecipeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRecipeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear equals to
        defaultRecipeFiltering(
            "achievementsInThePastYear.equals=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.equals=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear in
        defaultRecipeFiltering(
            "achievementsInThePastYear.in=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR + "," + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.in=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear is not null
        defaultRecipeFiltering("achievementsInThePastYear.specified=true", "achievementsInThePastYear.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear is greater than or equal to
        defaultRecipeFiltering(
            "achievementsInThePastYear.greaterThanOrEqual=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.greaterThanOrEqual=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear is less than or equal to
        defaultRecipeFiltering(
            "achievementsInThePastYear.lessThanOrEqual=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.lessThanOrEqual=" + SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear is less than
        defaultRecipeFiltering(
            "achievementsInThePastYear.lessThan=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.lessThan=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByAchievementsInThePastYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where achievementsInThePastYear is greater than
        defaultRecipeFiltering(
            "achievementsInThePastYear.greaterThan=" + SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.greaterThan=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast equals to
        defaultRecipeFiltering(
            "newYearForecast.equals=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.equals=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast in
        defaultRecipeFiltering(
            "newYearForecast.in=" + DEFAULT_NEW_YEAR_FORECAST + "," + UPDATED_NEW_YEAR_FORECAST,
            "newYearForecast.in=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast is not null
        defaultRecipeFiltering("newYearForecast.specified=true", "newYearForecast.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast is greater than or equal to
        defaultRecipeFiltering(
            "newYearForecast.greaterThanOrEqual=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.greaterThanOrEqual=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast is less than or equal to
        defaultRecipeFiltering(
            "newYearForecast.lessThanOrEqual=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.lessThanOrEqual=" + SMALLER_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast is less than
        defaultRecipeFiltering(
            "newYearForecast.lessThan=" + UPDATED_NEW_YEAR_FORECAST,
            "newYearForecast.lessThan=" + DEFAULT_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByNewYearForecastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where newYearForecast is greater than
        defaultRecipeFiltering(
            "newYearForecast.greaterThan=" + SMALLER_NEW_YEAR_FORECAST,
            "newYearForecast.greaterThan=" + DEFAULT_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllRecipesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where category equals to
        defaultRecipeFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllRecipesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where category in
        defaultRecipeFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllRecipesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        // Get all the recipeList where category is not null
        defaultRecipeFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllRecipesByFinancialYearIsEqualToSomething() throws Exception {
        FinancialYear financialYear;
        if (TestUtil.findAll(em, FinancialYear.class).isEmpty()) {
            recipeRepository.saveAndFlush(recipe);
            financialYear = FinancialYearResourceIT.createEntity();
        } else {
            financialYear = TestUtil.findAll(em, FinancialYear.class).get(0);
        }
        em.persist(financialYear);
        em.flush();
        recipe.setFinancialYear(financialYear);
        recipeRepository.saveAndFlush(recipe);
        Long financialYearId = financialYear.getId();
        // Get all the recipeList where financialYear equals to financialYearId
        defaultRecipeShouldBeFound("financialYearId.equals=" + financialYearId);

        // Get all the recipeList where financialYear equals to (financialYearId + 1)
        defaultRecipeShouldNotBeFound("financialYearId.equals=" + (financialYearId + 1));
    }

    @Test
    @Transactional
    void getAllRecipesByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            recipeRepository.saveAndFlush(recipe);
            article = ArticleResourceIT.createEntity();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        recipe.addArticle(article);
        recipeRepository.saveAndFlush(recipe);
        Long articleId = article.getId();
        // Get all the recipeList where article equals to articleId
        defaultRecipeShouldBeFound("articleId.equals=" + articleId);

        // Get all the recipeList where article equals to (articleId + 1)
        defaultRecipeShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    private void defaultRecipeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRecipeShouldBeFound(shouldBeFound);
        defaultRecipeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRecipeShouldBeFound(String filter) throws Exception {
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievementsInThePastYear").value(hasItem(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR)))
            .andExpect(jsonPath("$.[*].newYearForecast").value(hasItem(DEFAULT_NEW_YEAR_FORECAST)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));

        // Check, that the count call also returns 1
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRecipeShouldNotBeFound(String filter) throws Exception {
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRecipeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe
        Recipe updatedRecipe = recipeRepository.findById(recipe.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRecipe are not directly saved in db
        em.detach(updatedRecipe);
        updatedRecipe
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);

        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecipe.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRecipeToMatchAllProperties(updatedRecipe);
    }

    @Test
    @Transactional
    void putNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL_ID, recipe.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipe)))
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(recipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(recipe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedRecipe, recipe), getPersistedRecipe(recipe));
    }

    @Test
    @Transactional
    void fullUpdateRecipeWithPatch() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the recipe using partial update
        Recipe partialUpdatedRecipe = new Recipe();
        partialUpdatedRecipe.setId(recipe.getId());

        partialUpdatedRecipe
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);

        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecipe.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRecipe))
            )
            .andExpect(status().isOk());

        // Validate the Recipe in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRecipeUpdatableFieldsEquals(partialUpdatedRecipe, getPersistedRecipe(partialUpdatedRecipe));
    }

    @Test
    @Transactional
    void patchNonExistingRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recipe.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(recipe))
            )
            .andExpect(status().isBadRequest());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecipe() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        recipe.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecipeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(recipe)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Recipe in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecipe() throws Exception {
        // Initialize the database
        insertedRecipe = recipeRepository.saveAndFlush(recipe);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the recipe
        restRecipeMockMvc
            .perform(delete(ENTITY_API_URL_ID, recipe.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return recipeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Recipe getPersistedRecipe(Recipe recipe) {
        return recipeRepository.findById(recipe.getId()).orElseThrow();
    }

    protected void assertPersistedRecipeToMatchAllProperties(Recipe expectedRecipe) {
        assertRecipeAllPropertiesEquals(expectedRecipe, getPersistedRecipe(expectedRecipe));
    }

    protected void assertPersistedRecipeToMatchUpdatableProperties(Recipe expectedRecipe) {
        assertRecipeAllUpdatablePropertiesEquals(expectedRecipe, getPersistedRecipe(expectedRecipe));
    }
}
