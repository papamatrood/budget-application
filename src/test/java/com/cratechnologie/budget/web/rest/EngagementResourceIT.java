package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.EngagementAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.repository.EngagementRepository;
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
 * Integration tests for the {@link EngagementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EngagementResourceIT {

    private static final String DEFAULT_ENGAGEMENT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ENGAGEMENT_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ENGAGEMENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENGAGEMENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_OBJECT_OF_EXPENSE = "AAAAAAAAAA";
    private static final String UPDATED_OBJECT_OF_EXPENSE = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFIED_CREDITS = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFIED_CREDITS = "BBBBBBBBBB";

    private static final String DEFAULT_CREDIT_COMMITTED = "AAAAAAAAAA";
    private static final String UPDATED_CREDIT_COMMITTED = "BBBBBBBBBB";

    private static final String DEFAULT_CREDITS_AVAILABLE = "AAAAAAAAAA";
    private static final String UPDATED_CREDITS_AVAILABLE = "BBBBBBBBBB";

    private static final String DEFAULT_AMOUNT_PROPOSED_COMMITMENT = "AAAAAAAAAA";
    private static final String UPDATED_AMOUNT_PROPOSED_COMMITMENT = "BBBBBBBBBB";

    private static final String DEFAULT_HEAD_DAF = "AAAAAAAAAA";
    private static final String UPDATED_HEAD_DAF = "BBBBBBBBBB";

    private static final String DEFAULT_FINANCIAL_CONTROLLER = "AAAAAAAAAA";
    private static final String UPDATED_FINANCIAL_CONTROLLER = "BBBBBBBBBB";

    private static final String DEFAULT_GENERAL_MANAGER = "AAAAAAAAAA";
    private static final String UPDATED_GENERAL_MANAGER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/engagements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EngagementRepository engagementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEngagementMockMvc;

    private Engagement engagement;

    private Engagement insertedEngagement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Engagement createEntity() {
        return new Engagement()
            .engagementNumber(DEFAULT_ENGAGEMENT_NUMBER)
            .engagementDate(DEFAULT_ENGAGEMENT_DATE)
            .objectOfExpense(DEFAULT_OBJECT_OF_EXPENSE)
            .notifiedCredits(DEFAULT_NOTIFIED_CREDITS)
            .creditCommitted(DEFAULT_CREDIT_COMMITTED)
            .creditsAvailable(DEFAULT_CREDITS_AVAILABLE)
            .amountProposedCommitment(DEFAULT_AMOUNT_PROPOSED_COMMITMENT)
            .headDaf(DEFAULT_HEAD_DAF)
            .financialController(DEFAULT_FINANCIAL_CONTROLLER)
            .generalManager(DEFAULT_GENERAL_MANAGER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Engagement createUpdatedEntity() {
        return new Engagement()
            .engagementNumber(UPDATED_ENGAGEMENT_NUMBER)
            .engagementDate(UPDATED_ENGAGEMENT_DATE)
            .objectOfExpense(UPDATED_OBJECT_OF_EXPENSE)
            .notifiedCredits(UPDATED_NOTIFIED_CREDITS)
            .creditCommitted(UPDATED_CREDIT_COMMITTED)
            .creditsAvailable(UPDATED_CREDITS_AVAILABLE)
            .amountProposedCommitment(UPDATED_AMOUNT_PROPOSED_COMMITMENT)
            .headDaf(UPDATED_HEAD_DAF)
            .financialController(UPDATED_FINANCIAL_CONTROLLER)
            .generalManager(UPDATED_GENERAL_MANAGER);
    }

    @BeforeEach
    void initTest() {
        engagement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEngagement != null) {
            engagementRepository.delete(insertedEngagement);
            insertedEngagement = null;
        }
    }

    @Test
    @Transactional
    void createEngagement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Engagement
        var returnedEngagement = om.readValue(
            restEngagementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Engagement.class
        );

        // Validate the Engagement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEngagementUpdatableFieldsEquals(returnedEngagement, getPersistedEngagement(returnedEngagement));

        insertedEngagement = returnedEngagement;
    }

    @Test
    @Transactional
    void createEngagementWithExistingId() throws Exception {
        // Create the Engagement with an existing ID
        engagement.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEngagementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement)))
            .andExpect(status().isBadRequest());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEngagementNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engagement.setEngagementNumber(null);

        // Create the Engagement, which fails.

        restEngagementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEngagementDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        engagement.setEngagementDate(null);

        // Create the Engagement, which fails.

        restEngagementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEngagements() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engagement.getId().intValue())))
            .andExpect(jsonPath("$.[*].engagementNumber").value(hasItem(DEFAULT_ENGAGEMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].engagementDate").value(hasItem(DEFAULT_ENGAGEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].objectOfExpense").value(hasItem(DEFAULT_OBJECT_OF_EXPENSE)))
            .andExpect(jsonPath("$.[*].notifiedCredits").value(hasItem(DEFAULT_NOTIFIED_CREDITS)))
            .andExpect(jsonPath("$.[*].creditCommitted").value(hasItem(DEFAULT_CREDIT_COMMITTED)))
            .andExpect(jsonPath("$.[*].creditsAvailable").value(hasItem(DEFAULT_CREDITS_AVAILABLE)))
            .andExpect(jsonPath("$.[*].amountProposedCommitment").value(hasItem(DEFAULT_AMOUNT_PROPOSED_COMMITMENT)))
            .andExpect(jsonPath("$.[*].headDaf").value(hasItem(DEFAULT_HEAD_DAF)))
            .andExpect(jsonPath("$.[*].financialController").value(hasItem(DEFAULT_FINANCIAL_CONTROLLER)))
            .andExpect(jsonPath("$.[*].generalManager").value(hasItem(DEFAULT_GENERAL_MANAGER)));
    }

    @Test
    @Transactional
    void getEngagement() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get the engagement
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL_ID, engagement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(engagement.getId().intValue()))
            .andExpect(jsonPath("$.engagementNumber").value(DEFAULT_ENGAGEMENT_NUMBER))
            .andExpect(jsonPath("$.engagementDate").value(DEFAULT_ENGAGEMENT_DATE.toString()))
            .andExpect(jsonPath("$.objectOfExpense").value(DEFAULT_OBJECT_OF_EXPENSE))
            .andExpect(jsonPath("$.notifiedCredits").value(DEFAULT_NOTIFIED_CREDITS))
            .andExpect(jsonPath("$.creditCommitted").value(DEFAULT_CREDIT_COMMITTED))
            .andExpect(jsonPath("$.creditsAvailable").value(DEFAULT_CREDITS_AVAILABLE))
            .andExpect(jsonPath("$.amountProposedCommitment").value(DEFAULT_AMOUNT_PROPOSED_COMMITMENT))
            .andExpect(jsonPath("$.headDaf").value(DEFAULT_HEAD_DAF))
            .andExpect(jsonPath("$.financialController").value(DEFAULT_FINANCIAL_CONTROLLER))
            .andExpect(jsonPath("$.generalManager").value(DEFAULT_GENERAL_MANAGER));
    }

    @Test
    @Transactional
    void getEngagementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        Long id = engagement.getId();

        defaultEngagementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEngagementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEngagementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementNumber equals to
        defaultEngagementFiltering(
            "engagementNumber.equals=" + DEFAULT_ENGAGEMENT_NUMBER,
            "engagementNumber.equals=" + UPDATED_ENGAGEMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementNumber in
        defaultEngagementFiltering(
            "engagementNumber.in=" + DEFAULT_ENGAGEMENT_NUMBER + "," + UPDATED_ENGAGEMENT_NUMBER,
            "engagementNumber.in=" + UPDATED_ENGAGEMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementNumber is not null
        defaultEngagementFiltering("engagementNumber.specified=true", "engagementNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementNumber contains
        defaultEngagementFiltering(
            "engagementNumber.contains=" + DEFAULT_ENGAGEMENT_NUMBER,
            "engagementNumber.contains=" + UPDATED_ENGAGEMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementNumber does not contain
        defaultEngagementFiltering(
            "engagementNumber.doesNotContain=" + UPDATED_ENGAGEMENT_NUMBER,
            "engagementNumber.doesNotContain=" + DEFAULT_ENGAGEMENT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementDate equals to
        defaultEngagementFiltering("engagementDate.equals=" + DEFAULT_ENGAGEMENT_DATE, "engagementDate.equals=" + UPDATED_ENGAGEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementDate in
        defaultEngagementFiltering(
            "engagementDate.in=" + DEFAULT_ENGAGEMENT_DATE + "," + UPDATED_ENGAGEMENT_DATE,
            "engagementDate.in=" + UPDATED_ENGAGEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByEngagementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where engagementDate is not null
        defaultEngagementFiltering("engagementDate.specified=true", "engagementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByObjectOfExpenseIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where objectOfExpense equals to
        defaultEngagementFiltering(
            "objectOfExpense.equals=" + DEFAULT_OBJECT_OF_EXPENSE,
            "objectOfExpense.equals=" + UPDATED_OBJECT_OF_EXPENSE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByObjectOfExpenseIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where objectOfExpense in
        defaultEngagementFiltering(
            "objectOfExpense.in=" + DEFAULT_OBJECT_OF_EXPENSE + "," + UPDATED_OBJECT_OF_EXPENSE,
            "objectOfExpense.in=" + UPDATED_OBJECT_OF_EXPENSE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByObjectOfExpenseIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where objectOfExpense is not null
        defaultEngagementFiltering("objectOfExpense.specified=true", "objectOfExpense.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByObjectOfExpenseContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where objectOfExpense contains
        defaultEngagementFiltering(
            "objectOfExpense.contains=" + DEFAULT_OBJECT_OF_EXPENSE,
            "objectOfExpense.contains=" + UPDATED_OBJECT_OF_EXPENSE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByObjectOfExpenseNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where objectOfExpense does not contain
        defaultEngagementFiltering(
            "objectOfExpense.doesNotContain=" + UPDATED_OBJECT_OF_EXPENSE,
            "objectOfExpense.doesNotContain=" + DEFAULT_OBJECT_OF_EXPENSE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByNotifiedCreditsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where notifiedCredits equals to
        defaultEngagementFiltering(
            "notifiedCredits.equals=" + DEFAULT_NOTIFIED_CREDITS,
            "notifiedCredits.equals=" + UPDATED_NOTIFIED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByNotifiedCreditsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where notifiedCredits in
        defaultEngagementFiltering(
            "notifiedCredits.in=" + DEFAULT_NOTIFIED_CREDITS + "," + UPDATED_NOTIFIED_CREDITS,
            "notifiedCredits.in=" + UPDATED_NOTIFIED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByNotifiedCreditsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where notifiedCredits is not null
        defaultEngagementFiltering("notifiedCredits.specified=true", "notifiedCredits.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByNotifiedCreditsContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where notifiedCredits contains
        defaultEngagementFiltering(
            "notifiedCredits.contains=" + DEFAULT_NOTIFIED_CREDITS,
            "notifiedCredits.contains=" + UPDATED_NOTIFIED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByNotifiedCreditsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where notifiedCredits does not contain
        defaultEngagementFiltering(
            "notifiedCredits.doesNotContain=" + UPDATED_NOTIFIED_CREDITS,
            "notifiedCredits.doesNotContain=" + DEFAULT_NOTIFIED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditCommittedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditCommitted equals to
        defaultEngagementFiltering(
            "creditCommitted.equals=" + DEFAULT_CREDIT_COMMITTED,
            "creditCommitted.equals=" + UPDATED_CREDIT_COMMITTED
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditCommittedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditCommitted in
        defaultEngagementFiltering(
            "creditCommitted.in=" + DEFAULT_CREDIT_COMMITTED + "," + UPDATED_CREDIT_COMMITTED,
            "creditCommitted.in=" + UPDATED_CREDIT_COMMITTED
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditCommittedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditCommitted is not null
        defaultEngagementFiltering("creditCommitted.specified=true", "creditCommitted.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditCommittedContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditCommitted contains
        defaultEngagementFiltering(
            "creditCommitted.contains=" + DEFAULT_CREDIT_COMMITTED,
            "creditCommitted.contains=" + UPDATED_CREDIT_COMMITTED
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditCommittedNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditCommitted does not contain
        defaultEngagementFiltering(
            "creditCommitted.doesNotContain=" + UPDATED_CREDIT_COMMITTED,
            "creditCommitted.doesNotContain=" + DEFAULT_CREDIT_COMMITTED
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditsAvailableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditsAvailable equals to
        defaultEngagementFiltering(
            "creditsAvailable.equals=" + DEFAULT_CREDITS_AVAILABLE,
            "creditsAvailable.equals=" + UPDATED_CREDITS_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditsAvailableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditsAvailable in
        defaultEngagementFiltering(
            "creditsAvailable.in=" + DEFAULT_CREDITS_AVAILABLE + "," + UPDATED_CREDITS_AVAILABLE,
            "creditsAvailable.in=" + UPDATED_CREDITS_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditsAvailableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditsAvailable is not null
        defaultEngagementFiltering("creditsAvailable.specified=true", "creditsAvailable.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditsAvailableContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditsAvailable contains
        defaultEngagementFiltering(
            "creditsAvailable.contains=" + DEFAULT_CREDITS_AVAILABLE,
            "creditsAvailable.contains=" + UPDATED_CREDITS_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByCreditsAvailableNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where creditsAvailable does not contain
        defaultEngagementFiltering(
            "creditsAvailable.doesNotContain=" + UPDATED_CREDITS_AVAILABLE,
            "creditsAvailable.doesNotContain=" + DEFAULT_CREDITS_AVAILABLE
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByAmountProposedCommitmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where amountProposedCommitment equals to
        defaultEngagementFiltering(
            "amountProposedCommitment.equals=" + DEFAULT_AMOUNT_PROPOSED_COMMITMENT,
            "amountProposedCommitment.equals=" + UPDATED_AMOUNT_PROPOSED_COMMITMENT
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByAmountProposedCommitmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where amountProposedCommitment in
        defaultEngagementFiltering(
            "amountProposedCommitment.in=" + DEFAULT_AMOUNT_PROPOSED_COMMITMENT + "," + UPDATED_AMOUNT_PROPOSED_COMMITMENT,
            "amountProposedCommitment.in=" + UPDATED_AMOUNT_PROPOSED_COMMITMENT
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByAmountProposedCommitmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where amountProposedCommitment is not null
        defaultEngagementFiltering("amountProposedCommitment.specified=true", "amountProposedCommitment.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByAmountProposedCommitmentContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where amountProposedCommitment contains
        defaultEngagementFiltering(
            "amountProposedCommitment.contains=" + DEFAULT_AMOUNT_PROPOSED_COMMITMENT,
            "amountProposedCommitment.contains=" + UPDATED_AMOUNT_PROPOSED_COMMITMENT
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByAmountProposedCommitmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where amountProposedCommitment does not contain
        defaultEngagementFiltering(
            "amountProposedCommitment.doesNotContain=" + UPDATED_AMOUNT_PROPOSED_COMMITMENT,
            "amountProposedCommitment.doesNotContain=" + DEFAULT_AMOUNT_PROPOSED_COMMITMENT
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByHeadDafIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where headDaf equals to
        defaultEngagementFiltering("headDaf.equals=" + DEFAULT_HEAD_DAF, "headDaf.equals=" + UPDATED_HEAD_DAF);
    }

    @Test
    @Transactional
    void getAllEngagementsByHeadDafIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where headDaf in
        defaultEngagementFiltering("headDaf.in=" + DEFAULT_HEAD_DAF + "," + UPDATED_HEAD_DAF, "headDaf.in=" + UPDATED_HEAD_DAF);
    }

    @Test
    @Transactional
    void getAllEngagementsByHeadDafIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where headDaf is not null
        defaultEngagementFiltering("headDaf.specified=true", "headDaf.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByHeadDafContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where headDaf contains
        defaultEngagementFiltering("headDaf.contains=" + DEFAULT_HEAD_DAF, "headDaf.contains=" + UPDATED_HEAD_DAF);
    }

    @Test
    @Transactional
    void getAllEngagementsByHeadDafNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where headDaf does not contain
        defaultEngagementFiltering("headDaf.doesNotContain=" + UPDATED_HEAD_DAF, "headDaf.doesNotContain=" + DEFAULT_HEAD_DAF);
    }

    @Test
    @Transactional
    void getAllEngagementsByFinancialControllerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where financialController equals to
        defaultEngagementFiltering(
            "financialController.equals=" + DEFAULT_FINANCIAL_CONTROLLER,
            "financialController.equals=" + UPDATED_FINANCIAL_CONTROLLER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByFinancialControllerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where financialController in
        defaultEngagementFiltering(
            "financialController.in=" + DEFAULT_FINANCIAL_CONTROLLER + "," + UPDATED_FINANCIAL_CONTROLLER,
            "financialController.in=" + UPDATED_FINANCIAL_CONTROLLER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByFinancialControllerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where financialController is not null
        defaultEngagementFiltering("financialController.specified=true", "financialController.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByFinancialControllerContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where financialController contains
        defaultEngagementFiltering(
            "financialController.contains=" + DEFAULT_FINANCIAL_CONTROLLER,
            "financialController.contains=" + UPDATED_FINANCIAL_CONTROLLER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByFinancialControllerNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where financialController does not contain
        defaultEngagementFiltering(
            "financialController.doesNotContain=" + UPDATED_FINANCIAL_CONTROLLER,
            "financialController.doesNotContain=" + DEFAULT_FINANCIAL_CONTROLLER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByGeneralManagerIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where generalManager equals to
        defaultEngagementFiltering("generalManager.equals=" + DEFAULT_GENERAL_MANAGER, "generalManager.equals=" + UPDATED_GENERAL_MANAGER);
    }

    @Test
    @Transactional
    void getAllEngagementsByGeneralManagerIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where generalManager in
        defaultEngagementFiltering(
            "generalManager.in=" + DEFAULT_GENERAL_MANAGER + "," + UPDATED_GENERAL_MANAGER,
            "generalManager.in=" + UPDATED_GENERAL_MANAGER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByGeneralManagerIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where generalManager is not null
        defaultEngagementFiltering("generalManager.specified=true", "generalManager.specified=false");
    }

    @Test
    @Transactional
    void getAllEngagementsByGeneralManagerContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where generalManager contains
        defaultEngagementFiltering(
            "generalManager.contains=" + DEFAULT_GENERAL_MANAGER,
            "generalManager.contains=" + UPDATED_GENERAL_MANAGER
        );
    }

    @Test
    @Transactional
    void getAllEngagementsByGeneralManagerNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        // Get all the engagementList where generalManager does not contain
        defaultEngagementFiltering(
            "generalManager.doesNotContain=" + UPDATED_GENERAL_MANAGER,
            "generalManager.doesNotContain=" + DEFAULT_GENERAL_MANAGER
        );
    }

    private void defaultEngagementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEngagementShouldBeFound(shouldBeFound);
        defaultEngagementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEngagementShouldBeFound(String filter) throws Exception {
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(engagement.getId().intValue())))
            .andExpect(jsonPath("$.[*].engagementNumber").value(hasItem(DEFAULT_ENGAGEMENT_NUMBER)))
            .andExpect(jsonPath("$.[*].engagementDate").value(hasItem(DEFAULT_ENGAGEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].objectOfExpense").value(hasItem(DEFAULT_OBJECT_OF_EXPENSE)))
            .andExpect(jsonPath("$.[*].notifiedCredits").value(hasItem(DEFAULT_NOTIFIED_CREDITS)))
            .andExpect(jsonPath("$.[*].creditCommitted").value(hasItem(DEFAULT_CREDIT_COMMITTED)))
            .andExpect(jsonPath("$.[*].creditsAvailable").value(hasItem(DEFAULT_CREDITS_AVAILABLE)))
            .andExpect(jsonPath("$.[*].amountProposedCommitment").value(hasItem(DEFAULT_AMOUNT_PROPOSED_COMMITMENT)))
            .andExpect(jsonPath("$.[*].headDaf").value(hasItem(DEFAULT_HEAD_DAF)))
            .andExpect(jsonPath("$.[*].financialController").value(hasItem(DEFAULT_FINANCIAL_CONTROLLER)))
            .andExpect(jsonPath("$.[*].generalManager").value(hasItem(DEFAULT_GENERAL_MANAGER)));

        // Check, that the count call also returns 1
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEngagementShouldNotBeFound(String filter) throws Exception {
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEngagementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEngagement() throws Exception {
        // Get the engagement
        restEngagementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEngagement() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engagement
        Engagement updatedEngagement = engagementRepository.findById(engagement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEngagement are not directly saved in db
        em.detach(updatedEngagement);
        updatedEngagement
            .engagementNumber(UPDATED_ENGAGEMENT_NUMBER)
            .engagementDate(UPDATED_ENGAGEMENT_DATE)
            .objectOfExpense(UPDATED_OBJECT_OF_EXPENSE)
            .notifiedCredits(UPDATED_NOTIFIED_CREDITS)
            .creditCommitted(UPDATED_CREDIT_COMMITTED)
            .creditsAvailable(UPDATED_CREDITS_AVAILABLE)
            .amountProposedCommitment(UPDATED_AMOUNT_PROPOSED_COMMITMENT)
            .headDaf(UPDATED_HEAD_DAF)
            .financialController(UPDATED_FINANCIAL_CONTROLLER)
            .generalManager(UPDATED_GENERAL_MANAGER);

        restEngagementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEngagement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEngagement))
            )
            .andExpect(status().isOk());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEngagementToMatchAllProperties(updatedEngagement);
    }

    @Test
    @Transactional
    void putNonExistingEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, engagement.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(engagement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(engagement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEngagementWithPatch() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engagement using partial update
        Engagement partialUpdatedEngagement = new Engagement();
        partialUpdatedEngagement.setId(engagement.getId());

        partialUpdatedEngagement
            .engagementDate(UPDATED_ENGAGEMENT_DATE)
            .objectOfExpense(UPDATED_OBJECT_OF_EXPENSE)
            .amountProposedCommitment(UPDATED_AMOUNT_PROPOSED_COMMITMENT);

        restEngagementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEngagement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEngagement))
            )
            .andExpect(status().isOk());

        // Validate the Engagement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEngagementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEngagement, engagement),
            getPersistedEngagement(engagement)
        );
    }

    @Test
    @Transactional
    void fullUpdateEngagementWithPatch() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the engagement using partial update
        Engagement partialUpdatedEngagement = new Engagement();
        partialUpdatedEngagement.setId(engagement.getId());

        partialUpdatedEngagement
            .engagementNumber(UPDATED_ENGAGEMENT_NUMBER)
            .engagementDate(UPDATED_ENGAGEMENT_DATE)
            .objectOfExpense(UPDATED_OBJECT_OF_EXPENSE)
            .notifiedCredits(UPDATED_NOTIFIED_CREDITS)
            .creditCommitted(UPDATED_CREDIT_COMMITTED)
            .creditsAvailable(UPDATED_CREDITS_AVAILABLE)
            .amountProposedCommitment(UPDATED_AMOUNT_PROPOSED_COMMITMENT)
            .headDaf(UPDATED_HEAD_DAF)
            .financialController(UPDATED_FINANCIAL_CONTROLLER)
            .generalManager(UPDATED_GENERAL_MANAGER);

        restEngagementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEngagement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEngagement))
            )
            .andExpect(status().isOk());

        // Validate the Engagement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEngagementUpdatableFieldsEquals(partialUpdatedEngagement, getPersistedEngagement(partialUpdatedEngagement));
    }

    @Test
    @Transactional
    void patchNonExistingEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, engagement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(engagement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(engagement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEngagement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        engagement.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEngagementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(engagement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Engagement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEngagement() throws Exception {
        // Initialize the database
        insertedEngagement = engagementRepository.saveAndFlush(engagement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the engagement
        restEngagementMockMvc
            .perform(delete(ENTITY_API_URL_ID, engagement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return engagementRepository.count();
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

    protected Engagement getPersistedEngagement(Engagement engagement) {
        return engagementRepository.findById(engagement.getId()).orElseThrow();
    }

    protected void assertPersistedEngagementToMatchAllProperties(Engagement expectedEngagement) {
        assertEngagementAllPropertiesEquals(expectedEngagement, getPersistedEngagement(expectedEngagement));
    }

    protected void assertPersistedEngagementToMatchUpdatableProperties(Engagement expectedEngagement) {
        assertEngagementAllUpdatablePropertiesEquals(expectedEngagement, getPersistedEngagement(expectedEngagement));
    }
}
