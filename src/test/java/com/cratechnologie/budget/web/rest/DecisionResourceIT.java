package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.DecisionAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.domain.Decision;
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.repository.DecisionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link DecisionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecisionResourceIT {

    private static final String DEFAULT_DECISION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_DECISION_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_DECISION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DECISION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/decisions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DecisionRepository decisionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionMockMvc;

    private Decision decision;

    private Decision insertedDecision;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createEntity() {
        return new Decision().decisionNumber(DEFAULT_DECISION_NUMBER).decisionDate(DEFAULT_DECISION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Decision createUpdatedEntity() {
        return new Decision().decisionNumber(UPDATED_DECISION_NUMBER).decisionDate(UPDATED_DECISION_DATE);
    }

    @BeforeEach
    void initTest() {
        decision = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDecision != null) {
            decisionRepository.delete(insertedDecision);
            insertedDecision = null;
        }
    }

    @Test
    @Transactional
    void createDecision() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Decision
        var returnedDecision = om.readValue(
            restDecisionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Decision.class
        );

        // Validate the Decision in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDecisionUpdatableFieldsEquals(returnedDecision, getPersistedDecision(returnedDecision));

        insertedDecision = returnedDecision;
    }

    @Test
    @Transactional
    void createDecisionWithExistingId() throws Exception {
        // Create the Decision with an existing ID
        decision.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision)))
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDecisionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        decision.setDecisionNumber(null);

        // Create the Decision, which fails.

        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDecisionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        decision.setDecisionDate(null);

        // Create the Decision, which fails.

        restDecisionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecisions() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decisionNumber").value(hasItem(DEFAULT_DECISION_NUMBER)))
            .andExpect(jsonPath("$.[*].decisionDate").value(hasItem(DEFAULT_DECISION_DATE.toString())));
    }

    @Test
    @Transactional
    void getDecision() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get the decision
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL_ID, decision.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decision.getId().intValue()))
            .andExpect(jsonPath("$.decisionNumber").value(DEFAULT_DECISION_NUMBER))
            .andExpect(jsonPath("$.decisionDate").value(DEFAULT_DECISION_DATE.toString()));
    }

    @Test
    @Transactional
    void getDecisionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        Long id = decision.getId();

        defaultDecisionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDecisionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDecisionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionNumber equals to
        defaultDecisionFiltering("decisionNumber.equals=" + DEFAULT_DECISION_NUMBER, "decisionNumber.equals=" + UPDATED_DECISION_NUMBER);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionNumber in
        defaultDecisionFiltering(
            "decisionNumber.in=" + DEFAULT_DECISION_NUMBER + "," + UPDATED_DECISION_NUMBER,
            "decisionNumber.in=" + UPDATED_DECISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionNumber is not null
        defaultDecisionFiltering("decisionNumber.specified=true", "decisionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionNumber contains
        defaultDecisionFiltering(
            "decisionNumber.contains=" + DEFAULT_DECISION_NUMBER,
            "decisionNumber.contains=" + UPDATED_DECISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionNumber does not contain
        defaultDecisionFiltering(
            "decisionNumber.doesNotContain=" + UPDATED_DECISION_NUMBER,
            "decisionNumber.doesNotContain=" + DEFAULT_DECISION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionDate equals to
        defaultDecisionFiltering("decisionDate.equals=" + DEFAULT_DECISION_DATE, "decisionDate.equals=" + UPDATED_DECISION_DATE);
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionDate in
        defaultDecisionFiltering(
            "decisionDate.in=" + DEFAULT_DECISION_DATE + "," + UPDATED_DECISION_DATE,
            "decisionDate.in=" + UPDATED_DECISION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDecisionsByDecisionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        // Get all the decisionList where decisionDate is not null
        defaultDecisionFiltering("decisionDate.specified=true", "decisionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionsByEngagementIsEqualToSomething() throws Exception {
        Engagement engagement;
        if (TestUtil.findAll(em, Engagement.class).isEmpty()) {
            decisionRepository.saveAndFlush(decision);
            engagement = EngagementResourceIT.createEntity();
        } else {
            engagement = TestUtil.findAll(em, Engagement.class).get(0);
        }
        em.persist(engagement);
        em.flush();
        decision.setEngagement(engagement);
        decisionRepository.saveAndFlush(decision);
        Long engagementId = engagement.getId();
        // Get all the decisionList where engagement equals to engagementId
        defaultDecisionShouldBeFound("engagementId.equals=" + engagementId);

        // Get all the decisionList where engagement equals to (engagementId + 1)
        defaultDecisionShouldNotBeFound("engagementId.equals=" + (engagementId + 1));
    }

    @Test
    @Transactional
    void getAllDecisionsByAnnexDecisionIsEqualToSomething() throws Exception {
        AnnexDecision annexDecision;
        if (TestUtil.findAll(em, AnnexDecision.class).isEmpty()) {
            decisionRepository.saveAndFlush(decision);
            annexDecision = AnnexDecisionResourceIT.createEntity();
        } else {
            annexDecision = TestUtil.findAll(em, AnnexDecision.class).get(0);
        }
        em.persist(annexDecision);
        em.flush();
        decision.setAnnexDecision(annexDecision);
        decisionRepository.saveAndFlush(decision);
        Long annexDecisionId = annexDecision.getId();
        // Get all the decisionList where annexDecision equals to annexDecisionId
        defaultDecisionShouldBeFound("annexDecisionId.equals=" + annexDecisionId);

        // Get all the decisionList where annexDecision equals to (annexDecisionId + 1)
        defaultDecisionShouldNotBeFound("annexDecisionId.equals=" + (annexDecisionId + 1));
    }

    private void defaultDecisionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDecisionShouldBeFound(shouldBeFound);
        defaultDecisionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionShouldBeFound(String filter) throws Exception {
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decision.getId().intValue())))
            .andExpect(jsonPath("$.[*].decisionNumber").value(hasItem(DEFAULT_DECISION_NUMBER)))
            .andExpect(jsonPath("$.[*].decisionDate").value(hasItem(DEFAULT_DECISION_DATE.toString())));

        // Check, that the count call also returns 1
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionShouldNotBeFound(String filter) throws Exception {
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecision() throws Exception {
        // Get the decision
        restDecisionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDecision() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decision
        Decision updatedDecision = decisionRepository.findById(decision.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDecision are not directly saved in db
        em.detach(updatedDecision);
        updatedDecision.decisionNumber(UPDATED_DECISION_NUMBER).decisionDate(UPDATED_DECISION_DATE);

        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDecision.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDecisionToMatchAllProperties(updatedDecision);
    }

    @Test
    @Transactional
    void putNonExistingDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decision.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        partialUpdatedDecision.decisionNumber(UPDATED_DECISION_NUMBER).decisionDate(UPDATED_DECISION_DATE);

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDecisionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDecision, decision), getPersistedDecision(decision));
    }

    @Test
    @Transactional
    void fullUpdateDecisionWithPatch() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decision using partial update
        Decision partialUpdatedDecision = new Decision();
        partialUpdatedDecision.setId(decision.getId());

        partialUpdatedDecision.decisionNumber(UPDATED_DECISION_NUMBER).decisionDate(UPDATED_DECISION_DATE);

        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecision))
            )
            .andExpect(status().isOk());

        // Validate the Decision in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDecisionUpdatableFieldsEquals(partialUpdatedDecision, getPersistedDecision(partialUpdatedDecision));
    }

    @Test
    @Transactional
    void patchNonExistingDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decision.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decision))
            )
            .andExpect(status().isBadRequest());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecision() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decision.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(decision)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Decision in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecision() throws Exception {
        // Initialize the database
        insertedDecision = decisionRepository.saveAndFlush(decision);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the decision
        restDecisionMockMvc
            .perform(delete(ENTITY_API_URL_ID, decision.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return decisionRepository.count();
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

    protected Decision getPersistedDecision(Decision decision) {
        return decisionRepository.findById(decision.getId()).orElseThrow();
    }

    protected void assertPersistedDecisionToMatchAllProperties(Decision expectedDecision) {
        assertDecisionAllPropertiesEquals(expectedDecision, getPersistedDecision(expectedDecision));
    }

    protected void assertPersistedDecisionToMatchUpdatableProperties(Decision expectedDecision) {
        assertDecisionAllUpdatablePropertiesEquals(expectedDecision, getPersistedDecision(expectedDecision));
    }
}
