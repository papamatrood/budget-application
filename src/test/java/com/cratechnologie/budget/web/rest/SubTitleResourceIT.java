package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.SubTitleAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.SubTitle;
import com.cratechnologie.budget.repository.SubTitleRepository;
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
 * Integration tests for the {@link SubTitleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubTitleResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sub-titles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SubTitleRepository subTitleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubTitleMockMvc;

    private SubTitle subTitle;

    private SubTitle insertedSubTitle;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTitle createEntity() {
        return new SubTitle().code(DEFAULT_CODE).designation(DEFAULT_DESIGNATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SubTitle createUpdatedEntity() {
        return new SubTitle().code(UPDATED_CODE).designation(UPDATED_DESIGNATION);
    }

    @BeforeEach
    void initTest() {
        subTitle = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSubTitle != null) {
            subTitleRepository.delete(insertedSubTitle);
            insertedSubTitle = null;
        }
    }

    @Test
    @Transactional
    void createSubTitle() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SubTitle
        var returnedSubTitle = om.readValue(
            restSubTitleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SubTitle.class
        );

        // Validate the SubTitle in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSubTitleUpdatableFieldsEquals(returnedSubTitle, getPersistedSubTitle(returnedSubTitle));

        insertedSubTitle = returnedSubTitle;
    }

    @Test
    @Transactional
    void createSubTitleWithExistingId() throws Exception {
        // Create the SubTitle with an existing ID
        subTitle.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubTitleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle)))
            .andExpect(status().isBadRequest());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subTitle.setCode(null);

        // Create the SubTitle, which fails.

        restSubTitleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        subTitle.setDesignation(null);

        // Create the SubTitle, which fails.

        restSubTitleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSubTitles() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTitle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)));
    }

    @Test
    @Transactional
    void getSubTitle() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get the subTitle
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL_ID, subTitle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(subTitle.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION));
    }

    @Test
    @Transactional
    void getSubTitlesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        Long id = subTitle.getId();

        defaultSubTitleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSubTitleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSubTitleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSubTitlesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where code equals to
        defaultSubTitleFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubTitlesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where code in
        defaultSubTitleFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubTitlesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where code is not null
        defaultSubTitleFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllSubTitlesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where code contains
        defaultSubTitleFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllSubTitlesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where code does not contain
        defaultSubTitleFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllSubTitlesByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where designation equals to
        defaultSubTitleFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSubTitlesByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where designation in
        defaultSubTitleFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllSubTitlesByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where designation is not null
        defaultSubTitleFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllSubTitlesByDesignationContainsSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where designation contains
        defaultSubTitleFiltering("designation.contains=" + DEFAULT_DESIGNATION, "designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllSubTitlesByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        // Get all the subTitleList where designation does not contain
        defaultSubTitleFiltering("designation.doesNotContain=" + UPDATED_DESIGNATION, "designation.doesNotContain=" + DEFAULT_DESIGNATION);
    }

    private void defaultSubTitleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSubTitleShouldBeFound(shouldBeFound);
        defaultSubTitleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSubTitleShouldBeFound(String filter) throws Exception {
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subTitle.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)));

        // Check, that the count call also returns 1
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSubTitleShouldNotBeFound(String filter) throws Exception {
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSubTitleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSubTitle() throws Exception {
        // Get the subTitle
        restSubTitleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubTitle() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subTitle
        SubTitle updatedSubTitle = subTitleRepository.findById(subTitle.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSubTitle are not directly saved in db
        em.detach(updatedSubTitle);
        updatedSubTitle.code(UPDATED_CODE).designation(UPDATED_DESIGNATION);

        restSubTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSubTitle.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSubTitle))
            )
            .andExpect(status().isOk());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSubTitleToMatchAllProperties(updatedSubTitle);
    }

    @Test
    @Transactional
    void putNonExistingSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, subTitle.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(subTitle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(subTitle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubTitleWithPatch() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subTitle using partial update
        SubTitle partialUpdatedSubTitle = new SubTitle();
        partialUpdatedSubTitle.setId(subTitle.getId());

        partialUpdatedSubTitle.code(UPDATED_CODE).designation(UPDATED_DESIGNATION);

        restSubTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubTitle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubTitle))
            )
            .andExpect(status().isOk());

        // Validate the SubTitle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubTitleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSubTitle, subTitle), getPersistedSubTitle(subTitle));
    }

    @Test
    @Transactional
    void fullUpdateSubTitleWithPatch() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the subTitle using partial update
        SubTitle partialUpdatedSubTitle = new SubTitle();
        partialUpdatedSubTitle.setId(subTitle.getId());

        partialUpdatedSubTitle.code(UPDATED_CODE).designation(UPDATED_DESIGNATION);

        restSubTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubTitle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSubTitle))
            )
            .andExpect(status().isOk());

        // Validate the SubTitle in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSubTitleUpdatableFieldsEquals(partialUpdatedSubTitle, getPersistedSubTitle(partialUpdatedSubTitle));
    }

    @Test
    @Transactional
    void patchNonExistingSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, subTitle.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subTitle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(subTitle))
            )
            .andExpect(status().isBadRequest());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubTitle() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        subTitle.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubTitleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(subTitle)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SubTitle in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubTitle() throws Exception {
        // Initialize the database
        insertedSubTitle = subTitleRepository.saveAndFlush(subTitle);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the subTitle
        restSubTitleMockMvc
            .perform(delete(ENTITY_API_URL_ID, subTitle.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return subTitleRepository.count();
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

    protected SubTitle getPersistedSubTitle(SubTitle subTitle) {
        return subTitleRepository.findById(subTitle.getId()).orElseThrow();
    }

    protected void assertPersistedSubTitleToMatchAllProperties(SubTitle expectedSubTitle) {
        assertSubTitleAllPropertiesEquals(expectedSubTitle, getPersistedSubTitle(expectedSubTitle));
    }

    protected void assertPersistedSubTitleToMatchUpdatableProperties(SubTitle expectedSubTitle) {
        assertSubTitleAllUpdatablePropertiesEquals(expectedSubTitle, getPersistedSubTitle(expectedSubTitle));
    }
}
