package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.ChapterAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Chapter;
import com.cratechnologie.budget.domain.SubTitle;
import com.cratechnologie.budget.repository.ChapterRepository;
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
 * Integration tests for the {@link ChapterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ChapterResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/chapters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restChapterMockMvc;

    private Chapter chapter;

    private Chapter insertedChapter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chapter createEntity() {
        return new Chapter().code(DEFAULT_CODE).designation(DEFAULT_DESIGNATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chapter createUpdatedEntity() {
        return new Chapter().code(UPDATED_CODE).designation(UPDATED_DESIGNATION);
    }

    @BeforeEach
    void initTest() {
        chapter = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedChapter != null) {
            chapterRepository.delete(insertedChapter);
            insertedChapter = null;
        }
    }

    @Test
    @Transactional
    void createChapter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Chapter
        var returnedChapter = om.readValue(
            restChapterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Chapter.class
        );

        // Validate the Chapter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertChapterUpdatableFieldsEquals(returnedChapter, getPersistedChapter(returnedChapter));

        insertedChapter = returnedChapter;
    }

    @Test
    @Transactional
    void createChapterWithExistingId() throws Exception {
        // Create the Chapter with an existing ID
        chapter.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chapter.setCode(null);

        // Create the Chapter, which fails.

        restChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDesignationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        chapter.setDesignation(null);

        // Create the Chapter, which fails.

        restChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllChapters() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList
        restChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)));
    }

    @Test
    @Transactional
    void getChapter() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get the chapter
        restChapterMockMvc
            .perform(get(ENTITY_API_URL_ID, chapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(chapter.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION));
    }

    @Test
    @Transactional
    void getChaptersByIdFiltering() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        Long id = chapter.getId();

        defaultChapterFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultChapterFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultChapterFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllChaptersByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where code equals to
        defaultChapterFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChaptersByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where code in
        defaultChapterFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChaptersByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where code is not null
        defaultChapterFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllChaptersByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where code contains
        defaultChapterFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllChaptersByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where code does not contain
        defaultChapterFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllChaptersByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where designation equals to
        defaultChapterFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllChaptersByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where designation in
        defaultChapterFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllChaptersByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where designation is not null
        defaultChapterFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllChaptersByDesignationContainsSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where designation contains
        defaultChapterFiltering("designation.contains=" + DEFAULT_DESIGNATION, "designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllChaptersByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        // Get all the chapterList where designation does not contain
        defaultChapterFiltering("designation.doesNotContain=" + UPDATED_DESIGNATION, "designation.doesNotContain=" + DEFAULT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllChaptersBySubTitleIsEqualToSomething() throws Exception {
        SubTitle subTitle;
        if (TestUtil.findAll(em, SubTitle.class).isEmpty()) {
            chapterRepository.saveAndFlush(chapter);
            subTitle = SubTitleResourceIT.createEntity();
        } else {
            subTitle = TestUtil.findAll(em, SubTitle.class).get(0);
        }
        em.persist(subTitle);
        em.flush();
        chapter.setSubTitle(subTitle);
        chapterRepository.saveAndFlush(chapter);
        Long subTitleId = subTitle.getId();
        // Get all the chapterList where subTitle equals to subTitleId
        defaultChapterShouldBeFound("subTitleId.equals=" + subTitleId);

        // Get all the chapterList where subTitle equals to (subTitleId + 1)
        defaultChapterShouldNotBeFound("subTitleId.equals=" + (subTitleId + 1));
    }

    private void defaultChapterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultChapterShouldBeFound(shouldBeFound);
        defaultChapterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultChapterShouldBeFound(String filter) throws Exception {
        restChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)));

        // Check, that the count call also returns 1
        restChapterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultChapterShouldNotBeFound(String filter) throws Exception {
        restChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restChapterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingChapter() throws Exception {
        // Get the chapter
        restChapterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingChapter() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chapter
        Chapter updatedChapter = chapterRepository.findById(chapter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedChapter are not directly saved in db
        em.detach(updatedChapter);
        updatedChapter.code(UPDATED_CODE).designation(UPDATED_DESIGNATION);

        restChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedChapter.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedChapter))
            )
            .andExpect(status().isOk());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedChapterToMatchAllProperties(updatedChapter);
    }

    @Test
    @Transactional
    void putNonExistingChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(put(ENTITY_API_URL_ID, chapter.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(chapter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateChapterWithPatch() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chapter using partial update
        Chapter partialUpdatedChapter = new Chapter();
        partialUpdatedChapter.setId(chapter.getId());

        restChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChapter))
            )
            .andExpect(status().isOk());

        // Validate the Chapter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChapterUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedChapter, chapter), getPersistedChapter(chapter));
    }

    @Test
    @Transactional
    void fullUpdateChapterWithPatch() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the chapter using partial update
        Chapter partialUpdatedChapter = new Chapter();
        partialUpdatedChapter.setId(chapter.getId());

        partialUpdatedChapter.code(UPDATED_CODE).designation(UPDATED_DESIGNATION);

        restChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedChapter))
            )
            .andExpect(status().isOk());

        // Validate the Chapter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertChapterUpdatableFieldsEquals(partialUpdatedChapter, getPersistedChapter(partialUpdatedChapter));
    }

    @Test
    @Transactional
    void patchNonExistingChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, chapter.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chapter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(chapter))
            )
            .andExpect(status().isBadRequest());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        chapter.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restChapterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(chapter)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Chapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteChapter() throws Exception {
        // Initialize the database
        insertedChapter = chapterRepository.saveAndFlush(chapter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the chapter
        restChapterMockMvc
            .perform(delete(ENTITY_API_URL_ID, chapter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return chapterRepository.count();
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

    protected Chapter getPersistedChapter(Chapter chapter) {
        return chapterRepository.findById(chapter.getId()).orElseThrow();
    }

    protected void assertPersistedChapterToMatchAllProperties(Chapter expectedChapter) {
        assertChapterAllPropertiesEquals(expectedChapter, getPersistedChapter(expectedChapter));
    }

    protected void assertPersistedChapterToMatchUpdatableProperties(Chapter expectedChapter) {
        assertChapterAllUpdatablePropertiesEquals(expectedChapter, getPersistedChapter(expectedChapter));
    }
}
