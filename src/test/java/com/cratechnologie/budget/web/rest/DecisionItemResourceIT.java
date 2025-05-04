package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.DecisionItemAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static com.cratechnologie.budget.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Decision;
import com.cratechnologie.budget.domain.DecisionItem;
import com.cratechnologie.budget.repository.DecisionItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DecisionItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DecisionItemResourceIT {

    private static final String DEFAULT_BENEFICIARY = "AAAAAAAAAA";
    private static final String UPDATED_BENEFICIARY = "BBBBBBBBBB";

    private static final Integer DEFAULT_AMOUNT = 1;
    private static final Integer UPDATED_AMOUNT = 2;
    private static final Integer SMALLER_AMOUNT = 1 - 1;

    private static final BigDecimal DEFAULT_OBSERVATION = new BigDecimal(1);
    private static final BigDecimal UPDATED_OBSERVATION = new BigDecimal(2);
    private static final BigDecimal SMALLER_OBSERVATION = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/decision-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DecisionItemRepository decisionItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDecisionItemMockMvc;

    private DecisionItem decisionItem;

    private DecisionItem insertedDecisionItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecisionItem createEntity() {
        return new DecisionItem().beneficiary(DEFAULT_BENEFICIARY).amount(DEFAULT_AMOUNT).observation(DEFAULT_OBSERVATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DecisionItem createUpdatedEntity() {
        return new DecisionItem().beneficiary(UPDATED_BENEFICIARY).amount(UPDATED_AMOUNT).observation(UPDATED_OBSERVATION);
    }

    @BeforeEach
    void initTest() {
        decisionItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDecisionItem != null) {
            decisionItemRepository.delete(insertedDecisionItem);
            insertedDecisionItem = null;
        }
    }

    @Test
    @Transactional
    void createDecisionItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DecisionItem
        var returnedDecisionItem = om.readValue(
            restDecisionItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DecisionItem.class
        );

        // Validate the DecisionItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDecisionItemUpdatableFieldsEquals(returnedDecisionItem, getPersistedDecisionItem(returnedDecisionItem));

        insertedDecisionItem = returnedDecisionItem;
    }

    @Test
    @Transactional
    void createDecisionItemWithExistingId() throws Exception {
        // Create the DecisionItem with an existing ID
        decisionItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDecisionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionItem)))
            .andExpect(status().isBadRequest());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBeneficiaryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        decisionItem.setBeneficiary(null);

        // Create the DecisionItem, which fails.

        restDecisionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDecisionItems() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].beneficiary").value(hasItem(DEFAULT_BENEFICIARY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(sameNumber(DEFAULT_OBSERVATION))));
    }

    @Test
    @Transactional
    void getDecisionItem() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get the decisionItem
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL_ID, decisionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(decisionItem.getId().intValue()))
            .andExpect(jsonPath("$.beneficiary").value(DEFAULT_BENEFICIARY))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT))
            .andExpect(jsonPath("$.observation").value(sameNumber(DEFAULT_OBSERVATION)));
    }

    @Test
    @Transactional
    void getDecisionItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        Long id = decisionItem.getId();

        defaultDecisionItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDecisionItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDecisionItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByBeneficiaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where beneficiary equals to
        defaultDecisionItemFiltering("beneficiary.equals=" + DEFAULT_BENEFICIARY, "beneficiary.equals=" + UPDATED_BENEFICIARY);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByBeneficiaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where beneficiary in
        defaultDecisionItemFiltering(
            "beneficiary.in=" + DEFAULT_BENEFICIARY + "," + UPDATED_BENEFICIARY,
            "beneficiary.in=" + UPDATED_BENEFICIARY
        );
    }

    @Test
    @Transactional
    void getAllDecisionItemsByBeneficiaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where beneficiary is not null
        defaultDecisionItemFiltering("beneficiary.specified=true", "beneficiary.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionItemsByBeneficiaryContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where beneficiary contains
        defaultDecisionItemFiltering("beneficiary.contains=" + DEFAULT_BENEFICIARY, "beneficiary.contains=" + UPDATED_BENEFICIARY);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByBeneficiaryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where beneficiary does not contain
        defaultDecisionItemFiltering(
            "beneficiary.doesNotContain=" + UPDATED_BENEFICIARY,
            "beneficiary.doesNotContain=" + DEFAULT_BENEFICIARY
        );
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount equals to
        defaultDecisionItemFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount in
        defaultDecisionItemFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount is not null
        defaultDecisionItemFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount is greater than or equal to
        defaultDecisionItemFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount is less than or equal to
        defaultDecisionItemFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount is less than
        defaultDecisionItemFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where amount is greater than
        defaultDecisionItemFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation equals to
        defaultDecisionItemFiltering("observation.equals=" + DEFAULT_OBSERVATION, "observation.equals=" + UPDATED_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation in
        defaultDecisionItemFiltering(
            "observation.in=" + DEFAULT_OBSERVATION + "," + UPDATED_OBSERVATION,
            "observation.in=" + UPDATED_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation is not null
        defaultDecisionItemFiltering("observation.specified=true", "observation.specified=false");
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation is greater than or equal to
        defaultDecisionItemFiltering(
            "observation.greaterThanOrEqual=" + DEFAULT_OBSERVATION,
            "observation.greaterThanOrEqual=" + UPDATED_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation is less than or equal to
        defaultDecisionItemFiltering(
            "observation.lessThanOrEqual=" + DEFAULT_OBSERVATION,
            "observation.lessThanOrEqual=" + SMALLER_OBSERVATION
        );
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation is less than
        defaultDecisionItemFiltering("observation.lessThan=" + UPDATED_OBSERVATION, "observation.lessThan=" + DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByObservationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        // Get all the decisionItemList where observation is greater than
        defaultDecisionItemFiltering("observation.greaterThan=" + SMALLER_OBSERVATION, "observation.greaterThan=" + DEFAULT_OBSERVATION);
    }

    @Test
    @Transactional
    void getAllDecisionItemsByDecisionIsEqualToSomething() throws Exception {
        Decision decision;
        if (TestUtil.findAll(em, Decision.class).isEmpty()) {
            decisionItemRepository.saveAndFlush(decisionItem);
            decision = DecisionResourceIT.createEntity();
        } else {
            decision = TestUtil.findAll(em, Decision.class).get(0);
        }
        em.persist(decision);
        em.flush();
        decisionItem.setDecision(decision);
        decisionItemRepository.saveAndFlush(decisionItem);
        Long decisionId = decision.getId();
        // Get all the decisionItemList where decision equals to decisionId
        defaultDecisionItemShouldBeFound("decisionId.equals=" + decisionId);

        // Get all the decisionItemList where decision equals to (decisionId + 1)
        defaultDecisionItemShouldNotBeFound("decisionId.equals=" + (decisionId + 1));
    }

    private void defaultDecisionItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDecisionItemShouldBeFound(shouldBeFound);
        defaultDecisionItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDecisionItemShouldBeFound(String filter) throws Exception {
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(decisionItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].beneficiary").value(hasItem(DEFAULT_BENEFICIARY)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.[*].observation").value(hasItem(sameNumber(DEFAULT_OBSERVATION))));

        // Check, that the count call also returns 1
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDecisionItemShouldNotBeFound(String filter) throws Exception {
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDecisionItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDecisionItem() throws Exception {
        // Get the decisionItem
        restDecisionItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDecisionItem() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decisionItem
        DecisionItem updatedDecisionItem = decisionItemRepository.findById(decisionItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDecisionItem are not directly saved in db
        em.detach(updatedDecisionItem);
        updatedDecisionItem.beneficiary(UPDATED_BENEFICIARY).amount(UPDATED_AMOUNT).observation(UPDATED_OBSERVATION);

        restDecisionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDecisionItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDecisionItem))
            )
            .andExpect(status().isOk());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDecisionItemToMatchAllProperties(updatedDecisionItem);
    }

    @Test
    @Transactional
    void putNonExistingDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, decisionItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decisionItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(decisionItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(decisionItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDecisionItemWithPatch() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decisionItem using partial update
        DecisionItem partialUpdatedDecisionItem = new DecisionItem();
        partialUpdatedDecisionItem.setId(decisionItem.getId());

        partialUpdatedDecisionItem.beneficiary(UPDATED_BENEFICIARY);

        restDecisionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecisionItem))
            )
            .andExpect(status().isOk());

        // Validate the DecisionItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDecisionItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDecisionItem, decisionItem),
            getPersistedDecisionItem(decisionItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateDecisionItemWithPatch() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the decisionItem using partial update
        DecisionItem partialUpdatedDecisionItem = new DecisionItem();
        partialUpdatedDecisionItem.setId(decisionItem.getId());

        partialUpdatedDecisionItem.beneficiary(UPDATED_BENEFICIARY).amount(UPDATED_AMOUNT).observation(UPDATED_OBSERVATION);

        restDecisionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDecisionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDecisionItem))
            )
            .andExpect(status().isOk());

        // Validate the DecisionItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDecisionItemUpdatableFieldsEquals(partialUpdatedDecisionItem, getPersistedDecisionItem(partialUpdatedDecisionItem));
    }

    @Test
    @Transactional
    void patchNonExistingDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, decisionItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decisionItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(decisionItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDecisionItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        decisionItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDecisionItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(decisionItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DecisionItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDecisionItem() throws Exception {
        // Initialize the database
        insertedDecisionItem = decisionItemRepository.saveAndFlush(decisionItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the decisionItem
        restDecisionItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, decisionItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return decisionItemRepository.count();
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

    protected DecisionItem getPersistedDecisionItem(DecisionItem decisionItem) {
        return decisionItemRepository.findById(decisionItem.getId()).orElseThrow();
    }

    protected void assertPersistedDecisionItemToMatchAllProperties(DecisionItem expectedDecisionItem) {
        assertDecisionItemAllPropertiesEquals(expectedDecisionItem, getPersistedDecisionItem(expectedDecisionItem));
    }

    protected void assertPersistedDecisionItemToMatchUpdatableProperties(DecisionItem expectedDecisionItem) {
        assertDecisionItemAllUpdatablePropertiesEquals(expectedDecisionItem, getPersistedDecisionItem(expectedDecisionItem));
    }
}
