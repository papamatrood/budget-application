package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.ArticleAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.service.ArticleService;
import com.cratechnologie.budget.domain.Article;
import com.cratechnologie.budget.domain.Chapter;
import com.cratechnologie.budget.domain.Expense;
import com.cratechnologie.budget.domain.Recipe;
import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import com.cratechnologie.budget.repository.ArticleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ArticleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ArticleResourceIT {

    private static final FinancialCategoryEnum DEFAULT_CATEGORY = FinancialCategoryEnum.OPERATING_RECIPE;
    private static final FinancialCategoryEnum UPDATED_CATEGORY = FinancialCategoryEnum.INVESTMENT_RECIPE;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_DIV = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_DIV = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_END = "AAAAAAAAAA";
    private static final String UPDATED_CODE_END = "BBBBBBBBBB";

    private static final String DEFAULT_PARAGRAPH = "AAAAAAAAAA";
    private static final String UPDATED_PARAGRAPH = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/articles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArticleRepository articleRepository;

    @Mock
    private ArticleRepository articleRepositoryMock;

    @Mock
    private ArticleService articleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArticleMockMvc;

    private Article article;

    private Article insertedArticle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createEntity() {
        return new Article()
            .category(DEFAULT_CATEGORY)
            .code(DEFAULT_CODE)
            .designation(DEFAULT_DESIGNATION)
            .accountDiv(DEFAULT_ACCOUNT_DIV)
            .codeEnd(DEFAULT_CODE_END)
            .paragraph(DEFAULT_PARAGRAPH);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Article createUpdatedEntity() {
        return new Article()
            .category(UPDATED_CATEGORY)
            .code(UPDATED_CODE)
            .designation(UPDATED_DESIGNATION)
            .accountDiv(UPDATED_ACCOUNT_DIV)
            .codeEnd(UPDATED_CODE_END)
            .paragraph(UPDATED_PARAGRAPH);
    }

    @BeforeEach
    void initTest() {
        article = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedArticle != null) {
            articleRepository.delete(insertedArticle);
            insertedArticle = null;
        }
    }

    @Test
    @Transactional
    void createArticle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Article
        var returnedArticle = om.readValue(
            restArticleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Article.class
        );

        // Validate the Article in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertArticleUpdatableFieldsEquals(returnedArticle, getPersistedArticle(returnedArticle));

        insertedArticle = returnedArticle;
    }

    @Test
    @Transactional
    void createArticleWithExistingId() throws Exception {
        // Create the Article with an existing ID
        article.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        article.setCode(null);

        // Create the Article, which fails.

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        article.setDesignation(null);

        // Create the Article, which fails.

        restArticleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArticles() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].accountDiv").value(hasItem(DEFAULT_ACCOUNT_DIV)))
            .andExpect(jsonPath("$.[*].codeEnd").value(hasItem(DEFAULT_CODE_END)))
            .andExpect(jsonPath("$.[*].paragraph").value(hasItem(DEFAULT_PARAGRAPH)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArticlesWithEagerRelationshipsIsEnabled() throws Exception {
        when(articleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(articleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArticlesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(articleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArticleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(articleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get the article
        restArticleMockMvc
            .perform(get(ENTITY_API_URL_ID, article.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(article.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.accountDiv").value(DEFAULT_ACCOUNT_DIV))
            .andExpect(jsonPath("$.codeEnd").value(DEFAULT_CODE_END))
            .andExpect(jsonPath("$.paragraph").value(DEFAULT_PARAGRAPH));
    }

    @Test
    @Transactional
    void getArticlesByIdFiltering() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        Long id = article.getId();

        defaultArticleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArticleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArticleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArticlesByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where category equals to
        defaultArticleFiltering("category.equals=" + DEFAULT_CATEGORY, "category.equals=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllArticlesByCategoryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where category in
        defaultArticleFiltering("category.in=" + DEFAULT_CATEGORY + "," + UPDATED_CATEGORY, "category.in=" + UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    void getAllArticlesByCategoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where category is not null
        defaultArticleFiltering("category.specified=true", "category.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where code equals to
        defaultArticleFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where code in
        defaultArticleFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where code is not null
        defaultArticleFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where code contains
        defaultArticleFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where code does not contain
        defaultArticleFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllArticlesByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where designation equals to
        defaultArticleFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where designation in
        defaultArticleFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllArticlesByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where designation is not null
        defaultArticleFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByDesignationContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where designation contains
        defaultArticleFiltering("designation.contains=" + DEFAULT_DESIGNATION, "designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllArticlesByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where designation does not contain
        defaultArticleFiltering("designation.doesNotContain=" + UPDATED_DESIGNATION, "designation.doesNotContain=" + DEFAULT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllArticlesByAccountDivIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where accountDiv equals to
        defaultArticleFiltering("accountDiv.equals=" + DEFAULT_ACCOUNT_DIV, "accountDiv.equals=" + UPDATED_ACCOUNT_DIV);
    }

    @Test
    @Transactional
    void getAllArticlesByAccountDivIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where accountDiv in
        defaultArticleFiltering("accountDiv.in=" + DEFAULT_ACCOUNT_DIV + "," + UPDATED_ACCOUNT_DIV, "accountDiv.in=" + UPDATED_ACCOUNT_DIV);
    }

    @Test
    @Transactional
    void getAllArticlesByAccountDivIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where accountDiv is not null
        defaultArticleFiltering("accountDiv.specified=true", "accountDiv.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByAccountDivContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where accountDiv contains
        defaultArticleFiltering("accountDiv.contains=" + DEFAULT_ACCOUNT_DIV, "accountDiv.contains=" + UPDATED_ACCOUNT_DIV);
    }

    @Test
    @Transactional
    void getAllArticlesByAccountDivNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where accountDiv does not contain
        defaultArticleFiltering("accountDiv.doesNotContain=" + UPDATED_ACCOUNT_DIV, "accountDiv.doesNotContain=" + DEFAULT_ACCOUNT_DIV);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeEndIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where codeEnd equals to
        defaultArticleFiltering("codeEnd.equals=" + DEFAULT_CODE_END, "codeEnd.equals=" + UPDATED_CODE_END);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeEndIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where codeEnd in
        defaultArticleFiltering("codeEnd.in=" + DEFAULT_CODE_END + "," + UPDATED_CODE_END, "codeEnd.in=" + UPDATED_CODE_END);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where codeEnd is not null
        defaultArticleFiltering("codeEnd.specified=true", "codeEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByCodeEndContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where codeEnd contains
        defaultArticleFiltering("codeEnd.contains=" + DEFAULT_CODE_END, "codeEnd.contains=" + UPDATED_CODE_END);
    }

    @Test
    @Transactional
    void getAllArticlesByCodeEndNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where codeEnd does not contain
        defaultArticleFiltering("codeEnd.doesNotContain=" + UPDATED_CODE_END, "codeEnd.doesNotContain=" + DEFAULT_CODE_END);
    }

    @Test
    @Transactional
    void getAllArticlesByParagraphIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where paragraph equals to
        defaultArticleFiltering("paragraph.equals=" + DEFAULT_PARAGRAPH, "paragraph.equals=" + UPDATED_PARAGRAPH);
    }

    @Test
    @Transactional
    void getAllArticlesByParagraphIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where paragraph in
        defaultArticleFiltering("paragraph.in=" + DEFAULT_PARAGRAPH + "," + UPDATED_PARAGRAPH, "paragraph.in=" + UPDATED_PARAGRAPH);
    }

    @Test
    @Transactional
    void getAllArticlesByParagraphIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where paragraph is not null
        defaultArticleFiltering("paragraph.specified=true", "paragraph.specified=false");
    }

    @Test
    @Transactional
    void getAllArticlesByParagraphContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where paragraph contains
        defaultArticleFiltering("paragraph.contains=" + DEFAULT_PARAGRAPH, "paragraph.contains=" + UPDATED_PARAGRAPH);
    }

    @Test
    @Transactional
    void getAllArticlesByParagraphNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        // Get all the articleList where paragraph does not contain
        defaultArticleFiltering("paragraph.doesNotContain=" + UPDATED_PARAGRAPH, "paragraph.doesNotContain=" + DEFAULT_PARAGRAPH);
    }

    @Test
    @Transactional
    void getAllArticlesByChapterIsEqualToSomething() throws Exception {
        Chapter chapter;
        if (TestUtil.findAll(em, Chapter.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            chapter = ChapterResourceIT.createEntity();
        } else {
            chapter = TestUtil.findAll(em, Chapter.class).get(0);
        }
        em.persist(chapter);
        em.flush();
        article.setChapter(chapter);
        articleRepository.saveAndFlush(article);
        Long chapterId = chapter.getId();
        // Get all the articleList where chapter equals to chapterId
        defaultArticleShouldBeFound("chapterId.equals=" + chapterId);

        // Get all the articleList where chapter equals to (chapterId + 1)
        defaultArticleShouldNotBeFound("chapterId.equals=" + (chapterId + 1));
    }

    @Test
    @Transactional
    void getAllArticlesByRecipeIsEqualToSomething() throws Exception {
        Recipe recipe;
        if (TestUtil.findAll(em, Recipe.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            recipe = RecipeResourceIT.createEntity();
        } else {
            recipe = TestUtil.findAll(em, Recipe.class).get(0);
        }
        em.persist(recipe);
        em.flush();
        article.addRecipe(recipe);
        articleRepository.saveAndFlush(article);
        Long recipeId = recipe.getId();
        // Get all the articleList where recipe equals to recipeId
        defaultArticleShouldBeFound("recipeId.equals=" + recipeId);

        // Get all the articleList where recipe equals to (recipeId + 1)
        defaultArticleShouldNotBeFound("recipeId.equals=" + (recipeId + 1));
    }

    @Test
    @Transactional
    void getAllArticlesByExpenseIsEqualToSomething() throws Exception {
        Expense expense;
        if (TestUtil.findAll(em, Expense.class).isEmpty()) {
            articleRepository.saveAndFlush(article);
            expense = ExpenseResourceIT.createEntity();
        } else {
            expense = TestUtil.findAll(em, Expense.class).get(0);
        }
        em.persist(expense);
        em.flush();
        article.addExpense(expense);
        articleRepository.saveAndFlush(article);
        Long expenseId = expense.getId();
        // Get all the articleList where expense equals to expenseId
        defaultArticleShouldBeFound("expenseId.equals=" + expenseId);

        // Get all the articleList where expense equals to (expenseId + 1)
        defaultArticleShouldNotBeFound("expenseId.equals=" + (expenseId + 1));
    }

    private void defaultArticleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArticleShouldBeFound(shouldBeFound);
        defaultArticleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArticleShouldBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(article.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].accountDiv").value(hasItem(DEFAULT_ACCOUNT_DIV)))
            .andExpect(jsonPath("$.[*].codeEnd").value(hasItem(DEFAULT_CODE_END)))
            .andExpect(jsonPath("$.[*].paragraph").value(hasItem(DEFAULT_PARAGRAPH)));

        // Check, that the count call also returns 1
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArticleShouldNotBeFound(String filter) throws Exception {
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArticleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArticle() throws Exception {
        // Get the article
        restArticleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article
        Article updatedArticle = articleRepository.findById(article.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArticle are not directly saved in db
        em.detach(updatedArticle);
        updatedArticle
            .category(UPDATED_CATEGORY)
            .code(UPDATED_CODE)
            .designation(UPDATED_DESIGNATION)
            .accountDiv(UPDATED_ACCOUNT_DIV)
            .codeEnd(UPDATED_CODE_END)
            .paragraph(UPDATED_PARAGRAPH);

        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArticle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArticleToMatchAllProperties(updatedArticle);
    }

    @Test
    @Transactional
    void putNonExistingArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(put(ENTITY_API_URL_ID, article.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(article)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle.category(UPDATED_CATEGORY).code(UPDATED_CODE).codeEnd(UPDATED_CODE_END).paragraph(UPDATED_PARAGRAPH);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedArticle, article), getPersistedArticle(article));
    }

    @Test
    @Transactional
    void fullUpdateArticleWithPatch() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the article using partial update
        Article partialUpdatedArticle = new Article();
        partialUpdatedArticle.setId(article.getId());

        partialUpdatedArticle
            .category(UPDATED_CATEGORY)
            .code(UPDATED_CODE)
            .designation(UPDATED_DESIGNATION)
            .accountDiv(UPDATED_ACCOUNT_DIV)
            .codeEnd(UPDATED_CODE_END)
            .paragraph(UPDATED_PARAGRAPH);

        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArticle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArticle))
            )
            .andExpect(status().isOk());

        // Validate the Article in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArticleUpdatableFieldsEquals(partialUpdatedArticle, getPersistedArticle(partialUpdatedArticle));
    }

    @Test
    @Transactional
    void patchNonExistingArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, article.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(article))
            )
            .andExpect(status().isBadRequest());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArticle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        article.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArticleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(article)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Article in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArticle() throws Exception {
        // Initialize the database
        insertedArticle = articleRepository.saveAndFlush(article);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the article
        restArticleMockMvc
            .perform(delete(ENTITY_API_URL_ID, article.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return articleRepository.count();
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

    protected Article getPersistedArticle(Article article) {
        return articleRepository.findById(article.getId()).orElseThrow();
    }

    protected void assertPersistedArticleToMatchAllProperties(Article expectedArticle) {
        assertArticleAllPropertiesEquals(expectedArticle, getPersistedArticle(expectedArticle));
    }

    protected void assertPersistedArticleToMatchUpdatableProperties(Article expectedArticle) {
        assertArticleAllUpdatablePropertiesEquals(expectedArticle, getPersistedArticle(expectedArticle));
    }
}
