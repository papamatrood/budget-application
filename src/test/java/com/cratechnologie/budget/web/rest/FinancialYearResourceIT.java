package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.FinancialYearAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.FinancialYear;
import com.cratechnologie.budget.repository.FinancialYearRepository;
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
 * Integration tests for the {@link FinancialYearResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FinancialYearResourceIT {

    private static final Integer DEFAULT_THE_YEAR = 1;
    private static final Integer UPDATED_THE_YEAR = 2;
    private static final Integer SMALLER_THE_YEAR = 1 - 1;

    private static final String ENTITY_API_URL = "/api/financial-years";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FinancialYearRepository financialYearRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFinancialYearMockMvc;

    private FinancialYear financialYear;

    private FinancialYear insertedFinancialYear;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialYear createEntity() {
        return new FinancialYear().theYear(DEFAULT_THE_YEAR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FinancialYear createUpdatedEntity() {
        return new FinancialYear().theYear(UPDATED_THE_YEAR);
    }

    @BeforeEach
    void initTest() {
        financialYear = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFinancialYear != null) {
            financialYearRepository.delete(insertedFinancialYear);
            insertedFinancialYear = null;
        }
    }

    @Test
    @Transactional
    void createFinancialYear() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FinancialYear
        var returnedFinancialYear = om.readValue(
            restFinancialYearMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialYear)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FinancialYear.class
        );

        // Validate the FinancialYear in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertFinancialYearUpdatableFieldsEquals(returnedFinancialYear, getPersistedFinancialYear(returnedFinancialYear));

        insertedFinancialYear = returnedFinancialYear;
    }

    @Test
    @Transactional
    void createFinancialYearWithExistingId() throws Exception {
        // Create the FinancialYear with an existing ID
        financialYear.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFinancialYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialYear)))
            .andExpect(status().isBadRequest());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTheYearIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        financialYear.setTheYear(null);

        // Create the FinancialYear, which fails.

        restFinancialYearMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialYear)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFinancialYears() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].theYear").value(hasItem(DEFAULT_THE_YEAR)));
    }

    @Test
    @Transactional
    void getFinancialYear() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get the financialYear
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL_ID, financialYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(financialYear.getId().intValue()))
            .andExpect(jsonPath("$.theYear").value(DEFAULT_THE_YEAR));
    }

    @Test
    @Transactional
    void getFinancialYearsByIdFiltering() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        Long id = financialYear.getId();

        defaultFinancialYearFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFinancialYearFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFinancialYearFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear equals to
        defaultFinancialYearFiltering("theYear.equals=" + DEFAULT_THE_YEAR, "theYear.equals=" + UPDATED_THE_YEAR);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear in
        defaultFinancialYearFiltering("theYear.in=" + DEFAULT_THE_YEAR + "," + UPDATED_THE_YEAR, "theYear.in=" + UPDATED_THE_YEAR);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear is not null
        defaultFinancialYearFiltering("theYear.specified=true", "theYear.specified=false");
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear is greater than or equal to
        defaultFinancialYearFiltering("theYear.greaterThanOrEqual=" + DEFAULT_THE_YEAR, "theYear.greaterThanOrEqual=" + UPDATED_THE_YEAR);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear is less than or equal to
        defaultFinancialYearFiltering("theYear.lessThanOrEqual=" + DEFAULT_THE_YEAR, "theYear.lessThanOrEqual=" + SMALLER_THE_YEAR);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear is less than
        defaultFinancialYearFiltering("theYear.lessThan=" + UPDATED_THE_YEAR, "theYear.lessThan=" + DEFAULT_THE_YEAR);
    }

    @Test
    @Transactional
    void getAllFinancialYearsByTheYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        // Get all the financialYearList where theYear is greater than
        defaultFinancialYearFiltering("theYear.greaterThan=" + SMALLER_THE_YEAR, "theYear.greaterThan=" + DEFAULT_THE_YEAR);
    }

    private void defaultFinancialYearFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFinancialYearShouldBeFound(shouldBeFound);
        defaultFinancialYearShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFinancialYearShouldBeFound(String filter) throws Exception {
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(financialYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].theYear").value(hasItem(DEFAULT_THE_YEAR)));

        // Check, that the count call also returns 1
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFinancialYearShouldNotBeFound(String filter) throws Exception {
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFinancialYearMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFinancialYear() throws Exception {
        // Get the financialYear
        restFinancialYearMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFinancialYear() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialYear
        FinancialYear updatedFinancialYear = financialYearRepository.findById(financialYear.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFinancialYear are not directly saved in db
        em.detach(updatedFinancialYear);
        updatedFinancialYear.theYear(UPDATED_THE_YEAR);

        restFinancialYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFinancialYear.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedFinancialYear))
            )
            .andExpect(status().isOk());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFinancialYearToMatchAllProperties(updatedFinancialYear);
    }

    @Test
    @Transactional
    void putNonExistingFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, financialYear.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialYear))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(financialYear))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(financialYear)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFinancialYearWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialYear using partial update
        FinancialYear partialUpdatedFinancialYear = new FinancialYear();
        partialUpdatedFinancialYear.setId(financialYear.getId());

        partialUpdatedFinancialYear.theYear(UPDATED_THE_YEAR);

        restFinancialYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialYear))
            )
            .andExpect(status().isOk());

        // Validate the FinancialYear in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialYearUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFinancialYear, financialYear),
            getPersistedFinancialYear(financialYear)
        );
    }

    @Test
    @Transactional
    void fullUpdateFinancialYearWithPatch() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the financialYear using partial update
        FinancialYear partialUpdatedFinancialYear = new FinancialYear();
        partialUpdatedFinancialYear.setId(financialYear.getId());

        partialUpdatedFinancialYear.theYear(UPDATED_THE_YEAR);

        restFinancialYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFinancialYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFinancialYear))
            )
            .andExpect(status().isOk());

        // Validate the FinancialYear in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFinancialYearUpdatableFieldsEquals(partialUpdatedFinancialYear, getPersistedFinancialYear(partialUpdatedFinancialYear));
    }

    @Test
    @Transactional
    void patchNonExistingFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, financialYear.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialYear))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(financialYear))
            )
            .andExpect(status().isBadRequest());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFinancialYear() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        financialYear.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFinancialYearMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(financialYear)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FinancialYear in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFinancialYear() throws Exception {
        // Initialize the database
        insertedFinancialYear = financialYearRepository.saveAndFlush(financialYear);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the financialYear
        restFinancialYearMockMvc
            .perform(delete(ENTITY_API_URL_ID, financialYear.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return financialYearRepository.count();
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

    protected FinancialYear getPersistedFinancialYear(FinancialYear financialYear) {
        return financialYearRepository.findById(financialYear.getId()).orElseThrow();
    }

    protected void assertPersistedFinancialYearToMatchAllProperties(FinancialYear expectedFinancialYear) {
        assertFinancialYearAllPropertiesEquals(expectedFinancialYear, getPersistedFinancialYear(expectedFinancialYear));
    }

    protected void assertPersistedFinancialYearToMatchUpdatableProperties(FinancialYear expectedFinancialYear) {
        assertFinancialYearAllUpdatablePropertiesEquals(expectedFinancialYear, getPersistedFinancialYear(expectedFinancialYear));
    }
}
