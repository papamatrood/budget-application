package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.ExpenseAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.domain.Article;
import com.cratechnologie.budget.domain.Expense;
import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import com.cratechnologie.budget.repository.ExpenseRepository;
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
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExpenseResourceIT {

    private static final Integer DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR = 1;
    private static final Integer UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR = 2;
    private static final Integer SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR = 1 - 1;

    private static final Integer DEFAULT_NEW_YEAR_FORECAST = 1;
    private static final Integer UPDATED_NEW_YEAR_FORECAST = 2;
    private static final Integer SMALLER_NEW_YEAR_FORECAST = 1 - 1;

    private static final FinancialCategoryEnum DEFAULT_CATEGORY = FinancialCategoryEnum.OPERATING_RECIPE;
    private static final FinancialCategoryEnum UPDATED_CATEGORY = FinancialCategoryEnum.INVESTMENT_RECIPE;

    private static final String ENTITY_API_URL = "/api/expenses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    private Expense insertedExpense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity() {
        return new Expense()
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
    public static Expense createUpdatedEntity() {
        return new Expense()
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);
    }

    @BeforeEach
    void initTest() {
        expense = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExpense != null) {
            expenseRepository.delete(insertedExpense);
            insertedExpense = null;
        }
    }

    @Test
    @Transactional
    void createExpense() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Expense
        var returnedExpense = om.readValue(
            restExpenseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expense)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Expense.class
        );

        // Validate the Expense in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertExpenseUpdatableFieldsEquals(returnedExpense, getPersistedExpense(returnedExpense));

        insertedExpense = returnedExpense;
    }

    @Test
    @Transactional
    void createExpenseWithExistingId() throws Exception {
        // Create the Expense with an existing ID
        expense.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllExpenses() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievementsInThePastYear").value(hasItem(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR)))
            .andExpect(jsonPath("$.[*].newYearForecast").value(hasItem(DEFAULT_NEW_YEAR_FORECAST)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    void getExpense() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL_ID, expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.achievementsInThePastYear").value(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR))
            .andExpect(jsonPath("$.newYearForecast").value(DEFAULT_NEW_YEAR_FORECAST))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    void getExpensesByIdFiltering() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        Long id = expense.getId();

        defaultExpenseFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExpenseFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExpenseFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear equals to
        defaultExpenseFiltering(
            "achievementsInThePastYear.equals=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.equals=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear in
        defaultExpenseFiltering(
            "achievementsInThePastYear.in=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR + "," + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.in=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear is not null
        defaultExpenseFiltering("achievementsInThePastYear.specified=true", "achievementsInThePastYear.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear is greater than or equal to
        defaultExpenseFiltering(
            "achievementsInThePastYear.greaterThanOrEqual=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.greaterThanOrEqual=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear is less than or equal to
        defaultExpenseFiltering(
            "achievementsInThePastYear.lessThanOrEqual=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.lessThanOrEqual=" + SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear is less than
        defaultExpenseFiltering(
            "achievementsInThePastYear.lessThan=" + UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.lessThan=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByAchievementsInThePastYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where achievementsInThePastYear is greater than
        defaultExpenseFiltering(
            "achievementsInThePastYear.greaterThan=" + SMALLER_ACHIEVEMENTS_IN_THE_PAST_YEAR,
            "achievementsInThePastYear.greaterThan=" + DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast equals to
        defaultExpenseFiltering(
            "newYearForecast.equals=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.equals=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast in
        defaultExpenseFiltering(
            "newYearForecast.in=" + DEFAULT_NEW_YEAR_FORECAST + "," + UPDATED_NEW_YEAR_FORECAST,
            "newYearForecast.in=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast is not null
        defaultExpenseFiltering("newYearForecast.specified=true", "newYearForecast.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast is greater than or equal to
        defaultExpenseFiltering(
            "newYearForecast.greaterThanOrEqual=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.greaterThanOrEqual=" + UPDATED_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast is less than or equal to
        defaultExpenseFiltering(
            "newYearForecast.lessThanOrEqual=" + DEFAULT_NEW_YEAR_FORECAST,
            "newYearForecast.lessThanOrEqual=" + SMALLER_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast is less than
        defaultExpenseFiltering(
            "newYearForecast.lessThan=" + UPDATED_NEW_YEAR_FORECAST,
            "newYearForecast.lessThan=" + DEFAULT_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByNewYearForecastIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where newYearForecast is greater than
        defaultExpenseFiltering(
            "newYearForecast.greaterThan=" + SMALLER_NEW_YEAR_FORECAST,
            "newYearForecast.greaterThan=" + DEFAULT_NEW_YEAR_FORECAST
        );
    }

    @Test
    @Transactional
    void getAllExpensesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where category equals to
        defaultExpenseFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllExpensesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where category in
        defaultExpenseFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllExpensesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        // Get all the expenseList where category is not null
        defaultExpenseFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllExpensesByAnnexDecisionIsEqualToSomething() throws Exception {
        AnnexDecision annexDecision;
        if (TestUtil.findAll(em, AnnexDecision.class).isEmpty()) {
            expenseRepository.saveAndFlush(expense);
            annexDecision = AnnexDecisionResourceIT.createEntity();
        } else {
            annexDecision = TestUtil.findAll(em, AnnexDecision.class).get(0);
        }
        em.persist(annexDecision);
        em.flush();
        expense.setAnnexDecision(annexDecision);
        expenseRepository.saveAndFlush(expense);
        Long annexDecisionId = annexDecision.getId();
        // Get all the expenseList where annexDecision equals to annexDecisionId
        defaultExpenseShouldBeFound("annexDecisionId.equals=" + annexDecisionId);

        // Get all the expenseList where annexDecision equals to (annexDecisionId + 1)
        defaultExpenseShouldNotBeFound("annexDecisionId.equals=" + (annexDecisionId + 1));
    }

    @Test
    @Transactional
    void getAllExpensesByFinancialYearIsEqualToSomething() throws Exception {
        FinancialYear financialYear;
        if (TestUtil.findAll(em, FinancialYear.class).isEmpty()) {
            expenseRepository.saveAndFlush(expense);
            financialYear = FinancialYearResourceIT.createEntity();
        } else {
            financialYear = TestUtil.findAll(em, FinancialYear.class).get(0);
        }
        em.persist(financialYear);
        em.flush();
        expense.setFinancialYear(financialYear);
        expenseRepository.saveAndFlush(expense);
        Long financialYearId = financialYear.getId();
        // Get all the expenseList where financialYear equals to financialYearId
        defaultExpenseShouldBeFound("financialYearId.equals=" + financialYearId);

        // Get all the expenseList where financialYear equals to (financialYearId + 1)
        defaultExpenseShouldNotBeFound("financialYearId.equals=" + (financialYearId + 1));
    }

    @Test
    @Transactional
    void getAllExpensesByArticleIsEqualToSomething() throws Exception {
        Article article;
        if (TestUtil.findAll(em, Article.class).isEmpty()) {
            expenseRepository.saveAndFlush(expense);
            article = ArticleResourceIT.createEntity();
        } else {
            article = TestUtil.findAll(em, Article.class).get(0);
        }
        em.persist(article);
        em.flush();
        expense.addArticle(article);
        expenseRepository.saveAndFlush(expense);
        Long articleId = article.getId();
        // Get all the expenseList where article equals to articleId
        defaultExpenseShouldBeFound("articleId.equals=" + articleId);

        // Get all the expenseList where article equals to (articleId + 1)
        defaultExpenseShouldNotBeFound("articleId.equals=" + (articleId + 1));
    }

    private void defaultExpenseFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExpenseShouldBeFound(shouldBeFound);
        defaultExpenseShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExpenseShouldBeFound(String filter) throws Exception {
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].achievementsInThePastYear").value(hasItem(DEFAULT_ACHIEVEMENTS_IN_THE_PAST_YEAR)))
            .andExpect(jsonPath("$.[*].newYearForecast").value(hasItem(DEFAULT_NEW_YEAR_FORECAST)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));

        // Check, that the count call also returns 1
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExpenseShouldNotBeFound(String filter) throws Exception {
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExpenseMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExpense() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);

        restExpenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedExpense.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExpenseToMatchAllProperties(updatedExpense);
    }

    @Test
    @Transactional
    void putNonExistingExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(put(ENTITY_API_URL_ID, expense.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(expense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExpenseWithPatch() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expense using partial update
        Expense partialUpdatedExpense = new Expense();
        partialUpdatedExpense.setId(expense.getId());

        partialUpdatedExpense.achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR);

        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpense.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedExpense, expense), getPersistedExpense(expense));
    }

    @Test
    @Transactional
    void fullUpdateExpenseWithPatch() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the expense using partial update
        Expense partialUpdatedExpense = new Expense();
        partialUpdatedExpense.setId(expense.getId());

        partialUpdatedExpense
            .achievementsInThePastYear(UPDATED_ACHIEVEMENTS_IN_THE_PAST_YEAR)
            .newYearForecast(UPDATED_NEW_YEAR_FORECAST)
            .category(UPDATED_CATEGORY);

        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExpense.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExpense))
            )
            .andExpect(status().isOk());

        // Validate the Expense in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExpenseUpdatableFieldsEquals(partialUpdatedExpense, getPersistedExpense(partialUpdatedExpense));
    }

    @Test
    @Transactional
    void patchNonExistingExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, expense.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(expense))
            )
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExpense() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        expense.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExpenseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(expense)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Expense in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExpense() throws Exception {
        // Initialize the database
        insertedExpense = expenseRepository.saveAndFlush(expense);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the expense
        restExpenseMockMvc
            .perform(delete(ENTITY_API_URL_ID, expense.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return expenseRepository.count();
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

    protected Expense getPersistedExpense(Expense expense) {
        return expenseRepository.findById(expense.getId()).orElseThrow();
    }

    protected void assertPersistedExpenseToMatchAllProperties(Expense expectedExpense) {
        assertExpenseAllPropertiesEquals(expectedExpense, getPersistedExpense(expectedExpense));
    }

    protected void assertPersistedExpenseToMatchUpdatableProperties(Expense expectedExpense) {
        assertExpenseAllUpdatablePropertiesEquals(expectedExpense, getPersistedExpense(expectedExpense));
    }
}
