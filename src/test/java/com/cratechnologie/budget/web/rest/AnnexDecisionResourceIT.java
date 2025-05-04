package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.AnnexDecisionAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.repository.AnnexDecisionRepository;
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
 * Integration tests for the {@link AnnexDecisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AnnexDecisionResourceIT {

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_EXPENSE_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSE_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITS_ALREADY_OPEN = "AAAAAAAAAA";
    private static final String UPDATED_CREDITS_ALREADY_OPEN = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITS_OPEN = "AAAAAAAAAA";
    private static final String UPDATED_CREDITS_OPEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/annex-decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnnexDecisionRepository annexDecisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnnexDecisionMockMvc;

    private AnnexDecision annexDecision;

    private AnnexDecision insertedAnnexDecision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexDecision createEntity() {
        return new AnnexDecision()
            .designation(DEFAULT_DESIGNATION)
            .expenseAmount(DEFAULT_EXPENSE_AMOUNT)
            .creditsAlreadyOpen(DEFAULT_CREDITS_ALREADY_OPEN)
            .creditsOpen(DEFAULT_CREDITS_OPEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnexDecision createUpdatedEntity() {
        return new AnnexDecision()
            .designation(UPDATED_DESIGNATION)
            .expenseAmount(UPDATED_EXPENSE_AMOUNT)
            .creditsAlreadyOpen(UPDATED_CREDITS_ALREADY_OPEN)
            .creditsOpen(UPDATED_CREDITS_OPEN);
    }

    @BeforeEach
    void initTest() {
        annexDecision = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAnnexDecision != null) {
            annexDecisionRepository.delete(insertedAnnexDecision);
            insertedAnnexDecision = null;
        }
    }

    @Test
    @Transactional
    void createAnnexDecision() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AnnexDecision
        var returnedAnnexDecision = om.readValue(
            restAnnexDecisionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annexDecision)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnnexDecision.class
        );

        // Validate the AnnexDecision in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAnnexDecisionUpdatableFieldsEquals(returnedAnnexDecision, getPersistedAnnexDecision(returnedAnnexDecision));

        insertedAnnexDecision = returnedAnnexDecision;
    }

    @Test
    @Transactional
    void createAnnexDecisionWithExistingId() throws Exception {
        // Create the AnnexDecision with an existing ID
        annexDecision.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnexDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annexDecision)))
            .andExpect(status().isBadRequest());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAnnexDecisions() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annexDecision.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].expenseAmount").value(hasItem(DEFAULT_EXPENSE_AMOUNT)))
            .andExpect(jsonPath("$.[*].creditsAlreadyOpen").value(hasItem(DEFAULT_CREDITS_ALREADY_OPEN)))
            .andExpect(jsonPath("$.[*].creditsOpen").value(hasItem(DEFAULT_CREDITS_OPEN)));
    }

    @Test
    @Transactional
    void getAnnexDecision() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get the annexDecision
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL_ID, annexDecision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annexDecision.getId().intValue()))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.expenseAmount").value(DEFAULT_EXPENSE_AMOUNT))
            .andExpect(jsonPath("$.creditsAlreadyOpen").value(DEFAULT_CREDITS_ALREADY_OPEN))
            .andExpect(jsonPath("$.creditsOpen").value(DEFAULT_CREDITS_OPEN));
    }

    @Test
    @Transactional
    void getAnnexDecisionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        Long id = annexDecision.getId();

        defaultAnnexDecisionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAnnexDecisionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAnnexDecisionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where designation equals to
        defaultAnnexDecisionFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where designation in
        defaultAnnexDecisionFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where designation is not null
        defaultAnnexDecisionFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByDesignationContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where designation contains
        defaultAnnexDecisionFiltering("designation.contains=" + DEFAULT_DESIGNATION, "designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where designation does not contain
        defaultAnnexDecisionFiltering(
            "designation.doesNotContain=" + UPDATED_DESIGNATION,
            "designation.doesNotContain=" + DEFAULT_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByExpenseAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where expenseAmount equals to
        defaultAnnexDecisionFiltering("expenseAmount.equals=" + DEFAULT_EXPENSE_AMOUNT, "expenseAmount.equals=" + UPDATED_EXPENSE_AMOUNT);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByExpenseAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where expenseAmount in
        defaultAnnexDecisionFiltering(
            "expenseAmount.in=" + DEFAULT_EXPENSE_AMOUNT + "," + UPDATED_EXPENSE_AMOUNT,
            "expenseAmount.in=" + UPDATED_EXPENSE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByExpenseAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where expenseAmount is not null
        defaultAnnexDecisionFiltering("expenseAmount.specified=true", "expenseAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByExpenseAmountContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where expenseAmount contains
        defaultAnnexDecisionFiltering(
            "expenseAmount.contains=" + DEFAULT_EXPENSE_AMOUNT,
            "expenseAmount.contains=" + UPDATED_EXPENSE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByExpenseAmountNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where expenseAmount does not contain
        defaultAnnexDecisionFiltering(
            "expenseAmount.doesNotContain=" + UPDATED_EXPENSE_AMOUNT,
            "expenseAmount.doesNotContain=" + DEFAULT_EXPENSE_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsAlreadyOpenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsAlreadyOpen equals to
        defaultAnnexDecisionFiltering(
            "creditsAlreadyOpen.equals=" + DEFAULT_CREDITS_ALREADY_OPEN,
            "creditsAlreadyOpen.equals=" + UPDATED_CREDITS_ALREADY_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsAlreadyOpenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsAlreadyOpen in
        defaultAnnexDecisionFiltering(
            "creditsAlreadyOpen.in=" + DEFAULT_CREDITS_ALREADY_OPEN + "," + UPDATED_CREDITS_ALREADY_OPEN,
            "creditsAlreadyOpen.in=" + UPDATED_CREDITS_ALREADY_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsAlreadyOpenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsAlreadyOpen is not null
        defaultAnnexDecisionFiltering("creditsAlreadyOpen.specified=true", "creditsAlreadyOpen.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsAlreadyOpenContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsAlreadyOpen contains
        defaultAnnexDecisionFiltering(
            "creditsAlreadyOpen.contains=" + DEFAULT_CREDITS_ALREADY_OPEN,
            "creditsAlreadyOpen.contains=" + UPDATED_CREDITS_ALREADY_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsAlreadyOpenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsAlreadyOpen does not contain
        defaultAnnexDecisionFiltering(
            "creditsAlreadyOpen.doesNotContain=" + UPDATED_CREDITS_ALREADY_OPEN,
            "creditsAlreadyOpen.doesNotContain=" + DEFAULT_CREDITS_ALREADY_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsOpenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsOpen equals to
        defaultAnnexDecisionFiltering("creditsOpen.equals=" + DEFAULT_CREDITS_OPEN, "creditsOpen.equals=" + UPDATED_CREDITS_OPEN);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsOpenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsOpen in
        defaultAnnexDecisionFiltering(
            "creditsOpen.in=" + DEFAULT_CREDITS_OPEN + "," + UPDATED_CREDITS_OPEN,
            "creditsOpen.in=" + UPDATED_CREDITS_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsOpenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsOpen is not null
        defaultAnnexDecisionFiltering("creditsOpen.specified=true", "creditsOpen.specified=false");
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsOpenContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsOpen contains
        defaultAnnexDecisionFiltering("creditsOpen.contains=" + DEFAULT_CREDITS_OPEN, "creditsOpen.contains=" + UPDATED_CREDITS_OPEN);
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByCreditsOpenNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        // Get all the annexDecisionList where creditsOpen does not contain
        defaultAnnexDecisionFiltering(
            "creditsOpen.doesNotContain=" + UPDATED_CREDITS_OPEN,
            "creditsOpen.doesNotContain=" + DEFAULT_CREDITS_OPEN
        );
    }

    @Test
    @Transactional
    void getAllAnnexDecisionsByFinancialYearIsEqualToSomething() throws Exception {
        FinancialYear financialYear;
        if (TestUtil.findAll(em, FinancialYear.class).isEmpty()) {
            annexDecisionRepository.saveAndFlush(annexDecision);
            financialYear = FinancialYearResourceIT.createEntity();
        } else {
            financialYear = TestUtil.findAll(em, FinancialYear.class).get(0);
        }
        em.persist(financialYear);
        em.flush();
        annexDecision.setFinancialYear(financialYear);
        annexDecisionRepository.saveAndFlush(annexDecision);
        Long financialYearId = financialYear.getId();
        // Get all the annexDecisionList where financialYear equals to financialYearId
        defaultAnnexDecisionShouldBeFound("financialYearId.equals=" + financialYearId);

        // Get all the annexDecisionList where financialYear equals to (financialYearId + 1)
        defaultAnnexDecisionShouldNotBeFound("financialYearId.equals=" + (financialYearId + 1));
    }

    private void defaultAnnexDecisionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAnnexDecisionShouldBeFound(shouldBeFound);
        defaultAnnexDecisionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnexDecisionShouldBeFound(String filter) throws Exception {
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annexDecision.getId().intValue())))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].expenseAmount").value(hasItem(DEFAULT_EXPENSE_AMOUNT)))
            .andExpect(jsonPath("$.[*].creditsAlreadyOpen").value(hasItem(DEFAULT_CREDITS_ALREADY_OPEN)))
            .andExpect(jsonPath("$.[*].creditsOpen").value(hasItem(DEFAULT_CREDITS_OPEN)));

        // Check, that the count call also returns 1
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnexDecisionShouldNotBeFound(String filter) throws Exception {
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnexDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAnnexDecision() throws Exception {
        // Get the annexDecision
        restAnnexDecisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnexDecision() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annexDecision
        AnnexDecision updatedAnnexDecision = annexDecisionRepository.findById(annexDecision.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnnexDecision are not directly saved in db
        em.detach(updatedAnnexDecision);
        updatedAnnexDecision
            .designation(UPDATED_DESIGNATION)
            .expenseAmount(UPDATED_EXPENSE_AMOUNT)
            .creditsAlreadyOpen(UPDATED_CREDITS_ALREADY_OPEN)
            .creditsOpen(UPDATED_CREDITS_OPEN);

        restAnnexDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnnexDecision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAnnexDecision))
            )
            .andExpect(status().isOk());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnnexDecisionToMatchAllProperties(updatedAnnexDecision);
    }

    @Test
    @Transactional
    void putNonExistingAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annexDecision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(annexDecision))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(annexDecision))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(annexDecision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnexDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annexDecision using partial update
        AnnexDecision partialUpdatedAnnexDecision = new AnnexDecision();
        partialUpdatedAnnexDecision.setId(annexDecision.getId());

        partialUpdatedAnnexDecision.expenseAmount(UPDATED_EXPENSE_AMOUNT).creditsAlreadyOpen(UPDATED_CREDITS_ALREADY_OPEN);

        restAnnexDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnexDecision))
            )
            .andExpect(status().isOk());

        // Validate the AnnexDecision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnexDecisionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAnnexDecision, annexDecision),
            getPersistedAnnexDecision(annexDecision)
        );
    }

    @Test
    @Transactional
    void fullUpdateAnnexDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the annexDecision using partial update
        AnnexDecision partialUpdatedAnnexDecision = new AnnexDecision();
        partialUpdatedAnnexDecision.setId(annexDecision.getId());

        partialUpdatedAnnexDecision
            .designation(UPDATED_DESIGNATION)
            .expenseAmount(UPDATED_EXPENSE_AMOUNT)
            .creditsAlreadyOpen(UPDATED_CREDITS_ALREADY_OPEN)
            .creditsOpen(UPDATED_CREDITS_OPEN);

        restAnnexDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnexDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnnexDecision))
            )
            .andExpect(status().isOk());

        // Validate the AnnexDecision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnnexDecisionUpdatableFieldsEquals(partialUpdatedAnnexDecision, getPersistedAnnexDecision(partialUpdatedAnnexDecision));
    }

    @Test
    @Transactional
    void patchNonExistingAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annexDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(annexDecision))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(annexDecision))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnexDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        annexDecision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnexDecisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(annexDecision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnexDecision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnexDecision() throws Exception {
        // Initialize the database
        insertedAnnexDecision = annexDecisionRepository.saveAndFlush(annexDecision);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the annexDecision
        restAnnexDecisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, annexDecision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return annexDecisionRepository.count();
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

    protected AnnexDecision getPersistedAnnexDecision(AnnexDecision annexDecision) {
        return annexDecisionRepository.findById(annexDecision.getId()).orElseThrow();
    }

    protected void assertPersistedAnnexDecisionToMatchAllProperties(AnnexDecision expectedAnnexDecision) {
        assertAnnexDecisionAllPropertiesEquals(expectedAnnexDecision, getPersistedAnnexDecision(expectedAnnexDecision));
    }

    protected void assertPersistedAnnexDecisionToMatchUpdatableProperties(AnnexDecision expectedAnnexDecision) {
        assertAnnexDecisionAllUpdatablePropertiesEquals(expectedAnnexDecision, getPersistedAnnexDecision(expectedAnnexDecision));
    }
}
