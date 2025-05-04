package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.MandateAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.domain.Mandate;
import com.cratechnologie.budget.repository.MandateRepository;
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
 * Integration tests for the {@link MandateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MandateResourceIT {

    private static final String DEFAULT_MANDATE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_MANDATE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_MANDATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MANDATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ISSUE_SLIP_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ISSUE_SLIP_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MONTH_AND_YEAR_OF_ISSUE = "AAAAAAAAAA";
    private static final String UPDATED_MONTH_AND_YEAR_OF_ISSUE = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORTING_DOCUMENTS = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORTING_DOCUMENTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/mandates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MandateRepository mandateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMandateMockMvc;

    private Mandate mandate;

    private Mandate insertedMandate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mandate createEntity() {
        return new Mandate()
            .mandateNumber(DEFAULT_MANDATE_NUMBER)
            .mandateDate(DEFAULT_MANDATE_DATE)
            .issueSlipNumber(DEFAULT_ISSUE_SLIP_NUMBER)
            .monthAndYearOfIssue(DEFAULT_MONTH_AND_YEAR_OF_ISSUE)
            .supportingDocuments(DEFAULT_SUPPORTING_DOCUMENTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Mandate createUpdatedEntity() {
        return new Mandate()
            .mandateNumber(UPDATED_MANDATE_NUMBER)
            .mandateDate(UPDATED_MANDATE_DATE)
            .issueSlipNumber(UPDATED_ISSUE_SLIP_NUMBER)
            .monthAndYearOfIssue(UPDATED_MONTH_AND_YEAR_OF_ISSUE)
            .supportingDocuments(UPDATED_SUPPORTING_DOCUMENTS);
    }

    @BeforeEach
    void initTest() {
        mandate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMandate != null) {
            mandateRepository.delete(insertedMandate);
            insertedMandate = null;
        }
    }

    @Test
    @Transactional
    void createMandate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Mandate
        var returnedMandate = om.readValue(
            restMandateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Mandate.class
        );

        // Validate the Mandate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertMandateUpdatableFieldsEquals(returnedMandate, getPersistedMandate(returnedMandate));

        insertedMandate = returnedMandate;
    }

    @Test
    @Transactional
    void createMandateWithExistingId() throws Exception {
        // Create the Mandate with an existing ID
        mandate.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMandateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMandateNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mandate.setMandateNumber(null);

        // Create the Mandate, which fails.

        restMandateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMandateDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mandate.setMandateDate(null);

        // Create the Mandate, which fails.

        restMandateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMandates() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mandate.getId().intValue())))
            .andExpect(jsonPath("$.[*].mandateNumber").value(hasItem(DEFAULT_MANDATE_NUMBER)))
            .andExpect(jsonPath("$.[*].mandateDate").value(hasItem(DEFAULT_MANDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].issueSlipNumber").value(hasItem(DEFAULT_ISSUE_SLIP_NUMBER)))
            .andExpect(jsonPath("$.[*].monthAndYearOfIssue").value(hasItem(DEFAULT_MONTH_AND_YEAR_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].supportingDocuments").value(hasItem(DEFAULT_SUPPORTING_DOCUMENTS)));
    }

    @Test
    @Transactional
    void getMandate() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get the mandate
        restMandateMockMvc
            .perform(get(ENTITY_API_URL_ID, mandate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mandate.getId().intValue()))
            .andExpect(jsonPath("$.mandateNumber").value(DEFAULT_MANDATE_NUMBER))
            .andExpect(jsonPath("$.mandateDate").value(DEFAULT_MANDATE_DATE.toString()))
            .andExpect(jsonPath("$.issueSlipNumber").value(DEFAULT_ISSUE_SLIP_NUMBER))
            .andExpect(jsonPath("$.monthAndYearOfIssue").value(DEFAULT_MONTH_AND_YEAR_OF_ISSUE))
            .andExpect(jsonPath("$.supportingDocuments").value(DEFAULT_SUPPORTING_DOCUMENTS));
    }

    @Test
    @Transactional
    void getMandatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        Long id = mandate.getId();

        defaultMandateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMandateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMandateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMandatesByMandateNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateNumber equals to
        defaultMandateFiltering("mandateNumber.equals=" + DEFAULT_MANDATE_NUMBER, "mandateNumber.equals=" + UPDATED_MANDATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMandatesByMandateNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateNumber in
        defaultMandateFiltering(
            "mandateNumber.in=" + DEFAULT_MANDATE_NUMBER + "," + UPDATED_MANDATE_NUMBER,
            "mandateNumber.in=" + UPDATED_MANDATE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMandateNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateNumber is not null
        defaultMandateFiltering("mandateNumber.specified=true", "mandateNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMandatesByMandateNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateNumber contains
        defaultMandateFiltering("mandateNumber.contains=" + DEFAULT_MANDATE_NUMBER, "mandateNumber.contains=" + UPDATED_MANDATE_NUMBER);
    }

    @Test
    @Transactional
    void getAllMandatesByMandateNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateNumber does not contain
        defaultMandateFiltering(
            "mandateNumber.doesNotContain=" + UPDATED_MANDATE_NUMBER,
            "mandateNumber.doesNotContain=" + DEFAULT_MANDATE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMandateDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateDate equals to
        defaultMandateFiltering("mandateDate.equals=" + DEFAULT_MANDATE_DATE, "mandateDate.equals=" + UPDATED_MANDATE_DATE);
    }

    @Test
    @Transactional
    void getAllMandatesByMandateDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateDate in
        defaultMandateFiltering(
            "mandateDate.in=" + DEFAULT_MANDATE_DATE + "," + UPDATED_MANDATE_DATE,
            "mandateDate.in=" + UPDATED_MANDATE_DATE
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMandateDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where mandateDate is not null
        defaultMandateFiltering("mandateDate.specified=true", "mandateDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMandatesByIssueSlipNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where issueSlipNumber equals to
        defaultMandateFiltering(
            "issueSlipNumber.equals=" + DEFAULT_ISSUE_SLIP_NUMBER,
            "issueSlipNumber.equals=" + UPDATED_ISSUE_SLIP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByIssueSlipNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where issueSlipNumber in
        defaultMandateFiltering(
            "issueSlipNumber.in=" + DEFAULT_ISSUE_SLIP_NUMBER + "," + UPDATED_ISSUE_SLIP_NUMBER,
            "issueSlipNumber.in=" + UPDATED_ISSUE_SLIP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByIssueSlipNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where issueSlipNumber is not null
        defaultMandateFiltering("issueSlipNumber.specified=true", "issueSlipNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllMandatesByIssueSlipNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where issueSlipNumber contains
        defaultMandateFiltering(
            "issueSlipNumber.contains=" + DEFAULT_ISSUE_SLIP_NUMBER,
            "issueSlipNumber.contains=" + UPDATED_ISSUE_SLIP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByIssueSlipNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where issueSlipNumber does not contain
        defaultMandateFiltering(
            "issueSlipNumber.doesNotContain=" + UPDATED_ISSUE_SLIP_NUMBER,
            "issueSlipNumber.doesNotContain=" + DEFAULT_ISSUE_SLIP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMonthAndYearOfIssueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where monthAndYearOfIssue equals to
        defaultMandateFiltering(
            "monthAndYearOfIssue.equals=" + DEFAULT_MONTH_AND_YEAR_OF_ISSUE,
            "monthAndYearOfIssue.equals=" + UPDATED_MONTH_AND_YEAR_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMonthAndYearOfIssueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where monthAndYearOfIssue in
        defaultMandateFiltering(
            "monthAndYearOfIssue.in=" + DEFAULT_MONTH_AND_YEAR_OF_ISSUE + "," + UPDATED_MONTH_AND_YEAR_OF_ISSUE,
            "monthAndYearOfIssue.in=" + UPDATED_MONTH_AND_YEAR_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMonthAndYearOfIssueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where monthAndYearOfIssue is not null
        defaultMandateFiltering("monthAndYearOfIssue.specified=true", "monthAndYearOfIssue.specified=false");
    }

    @Test
    @Transactional
    void getAllMandatesByMonthAndYearOfIssueContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where monthAndYearOfIssue contains
        defaultMandateFiltering(
            "monthAndYearOfIssue.contains=" + DEFAULT_MONTH_AND_YEAR_OF_ISSUE,
            "monthAndYearOfIssue.contains=" + UPDATED_MONTH_AND_YEAR_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllMandatesByMonthAndYearOfIssueNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where monthAndYearOfIssue does not contain
        defaultMandateFiltering(
            "monthAndYearOfIssue.doesNotContain=" + UPDATED_MONTH_AND_YEAR_OF_ISSUE,
            "monthAndYearOfIssue.doesNotContain=" + DEFAULT_MONTH_AND_YEAR_OF_ISSUE
        );
    }

    @Test
    @Transactional
    void getAllMandatesBySupportingDocumentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where supportingDocuments equals to
        defaultMandateFiltering(
            "supportingDocuments.equals=" + DEFAULT_SUPPORTING_DOCUMENTS,
            "supportingDocuments.equals=" + UPDATED_SUPPORTING_DOCUMENTS
        );
    }

    @Test
    @Transactional
    void getAllMandatesBySupportingDocumentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where supportingDocuments in
        defaultMandateFiltering(
            "supportingDocuments.in=" + DEFAULT_SUPPORTING_DOCUMENTS + "," + UPDATED_SUPPORTING_DOCUMENTS,
            "supportingDocuments.in=" + UPDATED_SUPPORTING_DOCUMENTS
        );
    }

    @Test
    @Transactional
    void getAllMandatesBySupportingDocumentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where supportingDocuments is not null
        defaultMandateFiltering("supportingDocuments.specified=true", "supportingDocuments.specified=false");
    }

    @Test
    @Transactional
    void getAllMandatesBySupportingDocumentsContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where supportingDocuments contains
        defaultMandateFiltering(
            "supportingDocuments.contains=" + DEFAULT_SUPPORTING_DOCUMENTS,
            "supportingDocuments.contains=" + UPDATED_SUPPORTING_DOCUMENTS
        );
    }

    @Test
    @Transactional
    void getAllMandatesBySupportingDocumentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        // Get all the mandateList where supportingDocuments does not contain
        defaultMandateFiltering(
            "supportingDocuments.doesNotContain=" + UPDATED_SUPPORTING_DOCUMENTS,
            "supportingDocuments.doesNotContain=" + DEFAULT_SUPPORTING_DOCUMENTS
        );
    }

    @Test
    @Transactional
    void getAllMandatesByEngagementIsEqualToSomething() throws Exception {
        Engagement engagement;
        if (TestUtil.findAll(em, Engagement.class).isEmpty()) {
            mandateRepository.saveAndFlush(mandate);
            engagement = EngagementResourceIT.createEntity();
        } else {
            engagement = TestUtil.findAll(em, Engagement.class).get(0);
        }
        em.persist(engagement);
        em.flush();
        mandate.setEngagement(engagement);
        mandateRepository.saveAndFlush(mandate);
        Long engagementId = engagement.getId();
        // Get all the mandateList where engagement equals to engagementId
        defaultMandateShouldBeFound("engagementId.equals=" + engagementId);

        // Get all the mandateList where engagement equals to (engagementId + 1)
        defaultMandateShouldNotBeFound("engagementId.equals=" + (engagementId + 1));
    }

    private void defaultMandateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMandateShouldBeFound(shouldBeFound);
        defaultMandateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMandateShouldBeFound(String filter) throws Exception {
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mandate.getId().intValue())))
            .andExpect(jsonPath("$.[*].mandateNumber").value(hasItem(DEFAULT_MANDATE_NUMBER)))
            .andExpect(jsonPath("$.[*].mandateDate").value(hasItem(DEFAULT_MANDATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].issueSlipNumber").value(hasItem(DEFAULT_ISSUE_SLIP_NUMBER)))
            .andExpect(jsonPath("$.[*].monthAndYearOfIssue").value(hasItem(DEFAULT_MONTH_AND_YEAR_OF_ISSUE)))
            .andExpect(jsonPath("$.[*].supportingDocuments").value(hasItem(DEFAULT_SUPPORTING_DOCUMENTS)));

        // Check, that the count call also returns 1
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMandateShouldNotBeFound(String filter) throws Exception {
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMandateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMandate() throws Exception {
        // Get the mandate
        restMandateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMandate() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mandate
        Mandate updatedMandate = mandateRepository.findById(mandate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMandate are not directly saved in db
        em.detach(updatedMandate);
        updatedMandate
            .mandateNumber(UPDATED_MANDATE_NUMBER)
            .mandateDate(UPDATED_MANDATE_DATE)
            .issueSlipNumber(UPDATED_ISSUE_SLIP_NUMBER)
            .monthAndYearOfIssue(UPDATED_MONTH_AND_YEAR_OF_ISSUE)
            .supportingDocuments(UPDATED_SUPPORTING_DOCUMENTS);

        restMandateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMandate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMandateToMatchAllProperties(updatedMandate);
    }

    @Test
    @Transactional
    void putNonExistingMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(put(ENTITY_API_URL_ID, mandate.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMandateWithPatch() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mandate using partial update
        Mandate partialUpdatedMandate = new Mandate();
        partialUpdatedMandate.setId(mandate.getId());

        partialUpdatedMandate.mandateNumber(UPDATED_MANDATE_NUMBER).supportingDocuments(UPDATED_SUPPORTING_DOCUMENTS);

        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMandate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMandateUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMandate, mandate), getPersistedMandate(mandate));
    }

    @Test
    @Transactional
    void fullUpdateMandateWithPatch() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mandate using partial update
        Mandate partialUpdatedMandate = new Mandate();
        partialUpdatedMandate.setId(mandate.getId());

        partialUpdatedMandate
            .mandateNumber(UPDATED_MANDATE_NUMBER)
            .mandateDate(UPDATED_MANDATE_DATE)
            .issueSlipNumber(UPDATED_ISSUE_SLIP_NUMBER)
            .monthAndYearOfIssue(UPDATED_MONTH_AND_YEAR_OF_ISSUE)
            .supportingDocuments(UPDATED_SUPPORTING_DOCUMENTS);

        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMandate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMandate))
            )
            .andExpect(status().isOk());

        // Validate the Mandate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMandateUpdatableFieldsEquals(partialUpdatedMandate, getPersistedMandate(partialUpdatedMandate));
    }

    @Test
    @Transactional
    void patchNonExistingMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mandate.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mandate))
            )
            .andExpect(status().isBadRequest());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMandate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mandate.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMandateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mandate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Mandate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMandate() throws Exception {
        // Initialize the database
        insertedMandate = mandateRepository.saveAndFlush(mandate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mandate
        restMandateMockMvc
            .perform(delete(ENTITY_API_URL_ID, mandate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mandateRepository.count();
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

    protected Mandate getPersistedMandate(Mandate mandate) {
        return mandateRepository.findById(mandate.getId()).orElseThrow();
    }

    protected void assertPersistedMandateToMatchAllProperties(Mandate expectedMandate) {
        assertMandateAllPropertiesEquals(expectedMandate, getPersistedMandate(expectedMandate));
    }

    protected void assertPersistedMandateToMatchUpdatableProperties(Mandate expectedMandate) {
        assertMandateAllUpdatablePropertiesEquals(expectedMandate, getPersistedMandate(expectedMandate));
    }
}
