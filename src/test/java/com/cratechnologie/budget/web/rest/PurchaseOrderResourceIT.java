package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.PurchaseOrderAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static com.cratechnologie.budget.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.AnnexDecision;
import com.cratechnologie.budget.domain.Engagement;
import com.cratechnologie.budget.domain.PurchaseOrder;
import com.cratechnologie.budget.domain.Supplier;
import com.cratechnologie.budget.repository.PurchaseOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PurchaseOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderResourceIT {

    private static final String DEFAULT_NAME_OF_THE_MINISTRY = "AAAAAAAAAA";
    private static final String UPDATED_NAME_OF_THE_MINISTRY = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT_WITHOUT_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT_WITHOUT_TAX = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TAX_RATE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAX_RATE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TAX_RATE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_TAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_TAX_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_PREPAID_TAX_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PREPAID_TAX_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PREPAID_TAX_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT_WITH_TAX = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT_WITH_TAX = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT_WITH_TAX = new BigDecimal(1 - 1);

    private static final String DEFAULT_AUTH_EXPENDITURE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_AUTH_EXPENDITURE_NUMBER = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ALLOCATED_CREDITS = new BigDecimal(1);
    private static final BigDecimal UPDATED_ALLOCATED_CREDITS = new BigDecimal(2);
    private static final BigDecimal SMALLER_ALLOCATED_CREDITS = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_COMMITTED_EXPENDITURES = new BigDecimal(1);
    private static final BigDecimal UPDATED_COMMITTED_EXPENDITURES = new BigDecimal(2);
    private static final BigDecimal SMALLER_COMMITTED_EXPENDITURES = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_AVAILABLE_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVAILABLE_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_AVAILABLE_BALANCE = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/purchase-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderMockMvc;

    private PurchaseOrder purchaseOrder;

    private PurchaseOrder insertedPurchaseOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createEntity() {
        return new PurchaseOrder()
            .nameOfTheMinistry(DEFAULT_NAME_OF_THE_MINISTRY)
            .orderNumber(DEFAULT_ORDER_NUMBER)
            .orderDate(DEFAULT_ORDER_DATE)
            .totalAmountWithoutTax(DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX)
            .taxRate(DEFAULT_TAX_RATE)
            .totalTaxAmount(DEFAULT_TOTAL_TAX_AMOUNT)
            .prepaidTaxAmount(DEFAULT_PREPAID_TAX_AMOUNT)
            .totalAmountWithTax(DEFAULT_TOTAL_AMOUNT_WITH_TAX)
            .authExpenditureNumber(DEFAULT_AUTH_EXPENDITURE_NUMBER)
            .allocatedCredits(DEFAULT_ALLOCATED_CREDITS)
            .committedExpenditures(DEFAULT_COMMITTED_EXPENDITURES)
            .availableBalance(DEFAULT_AVAILABLE_BALANCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrder createUpdatedEntity() {
        return new PurchaseOrder()
            .nameOfTheMinistry(UPDATED_NAME_OF_THE_MINISTRY)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .orderDate(UPDATED_ORDER_DATE)
            .totalAmountWithoutTax(UPDATED_TOTAL_AMOUNT_WITHOUT_TAX)
            .taxRate(UPDATED_TAX_RATE)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .prepaidTaxAmount(UPDATED_PREPAID_TAX_AMOUNT)
            .totalAmountWithTax(UPDATED_TOTAL_AMOUNT_WITH_TAX)
            .authExpenditureNumber(UPDATED_AUTH_EXPENDITURE_NUMBER)
            .allocatedCredits(UPDATED_ALLOCATED_CREDITS)
            .committedExpenditures(UPDATED_COMMITTED_EXPENDITURES)
            .availableBalance(UPDATED_AVAILABLE_BALANCE);
    }

    @BeforeEach
    void initTest() {
        purchaseOrder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPurchaseOrder != null) {
            purchaseOrderRepository.delete(insertedPurchaseOrder);
            insertedPurchaseOrder = null;
        }
    }

    @Test
    @Transactional
    void createPurchaseOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchaseOrder
        var returnedPurchaseOrder = om.readValue(
            restPurchaseOrderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchaseOrder.class
        );

        // Validate the PurchaseOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPurchaseOrderUpdatableFieldsEquals(returnedPurchaseOrder, getPersistedPurchaseOrder(returnedPurchaseOrder));

        insertedPurchaseOrder = returnedPurchaseOrder;
    }

    @Test
    @Transactional
    void createPurchaseOrderWithExistingId() throws Exception {
        // Create the PurchaseOrder with an existing ID
        purchaseOrder.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameOfTheMinistryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrder.setNameOfTheMinistry(null);

        // Create the PurchaseOrder, which fails.

        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrder.setOrderNumber(null);

        // Create the PurchaseOrder, which fails.

        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrder.setOrderDate(null);

        // Create the PurchaseOrder, which fails.

        restPurchaseOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseOrders() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameOfTheMinistry").value(hasItem(DEFAULT_NAME_OF_THE_MINISTRY)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmountWithoutTax").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX))))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(sameNumber(DEFAULT_TAX_RATE))))
            .andExpect(jsonPath("$.[*].totalTaxAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].prepaidTaxAmount").value(hasItem(sameNumber(DEFAULT_PREPAID_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmountWithTax").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT_WITH_TAX))))
            .andExpect(jsonPath("$.[*].authExpenditureNumber").value(hasItem(DEFAULT_AUTH_EXPENDITURE_NUMBER)))
            .andExpect(jsonPath("$.[*].allocatedCredits").value(hasItem(sameNumber(DEFAULT_ALLOCATED_CREDITS))))
            .andExpect(jsonPath("$.[*].committedExpenditures").value(hasItem(sameNumber(DEFAULT_COMMITTED_EXPENDITURES))))
            .andExpect(jsonPath("$.[*].availableBalance").value(hasItem(sameNumber(DEFAULT_AVAILABLE_BALANCE))));
    }

    @Test
    @Transactional
    void getPurchaseOrder() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrder.getId().intValue()))
            .andExpect(jsonPath("$.nameOfTheMinistry").value(DEFAULT_NAME_OF_THE_MINISTRY))
            .andExpect(jsonPath("$.orderNumber").value(DEFAULT_ORDER_NUMBER))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.totalAmountWithoutTax").value(sameNumber(DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX)))
            .andExpect(jsonPath("$.taxRate").value(sameNumber(DEFAULT_TAX_RATE)))
            .andExpect(jsonPath("$.totalTaxAmount").value(sameNumber(DEFAULT_TOTAL_TAX_AMOUNT)))
            .andExpect(jsonPath("$.prepaidTaxAmount").value(sameNumber(DEFAULT_PREPAID_TAX_AMOUNT)))
            .andExpect(jsonPath("$.totalAmountWithTax").value(sameNumber(DEFAULT_TOTAL_AMOUNT_WITH_TAX)))
            .andExpect(jsonPath("$.authExpenditureNumber").value(DEFAULT_AUTH_EXPENDITURE_NUMBER))
            .andExpect(jsonPath("$.allocatedCredits").value(sameNumber(DEFAULT_ALLOCATED_CREDITS)))
            .andExpect(jsonPath("$.committedExpenditures").value(sameNumber(DEFAULT_COMMITTED_EXPENDITURES)))
            .andExpect(jsonPath("$.availableBalance").value(sameNumber(DEFAULT_AVAILABLE_BALANCE)));
    }

    @Test
    @Transactional
    void getPurchaseOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        Long id = purchaseOrder.getId();

        defaultPurchaseOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPurchaseOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPurchaseOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNameOfTheMinistryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where nameOfTheMinistry equals to
        defaultPurchaseOrderFiltering(
            "nameOfTheMinistry.equals=" + DEFAULT_NAME_OF_THE_MINISTRY,
            "nameOfTheMinistry.equals=" + UPDATED_NAME_OF_THE_MINISTRY
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNameOfTheMinistryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where nameOfTheMinistry in
        defaultPurchaseOrderFiltering(
            "nameOfTheMinistry.in=" + DEFAULT_NAME_OF_THE_MINISTRY + "," + UPDATED_NAME_OF_THE_MINISTRY,
            "nameOfTheMinistry.in=" + UPDATED_NAME_OF_THE_MINISTRY
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNameOfTheMinistryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where nameOfTheMinistry is not null
        defaultPurchaseOrderFiltering("nameOfTheMinistry.specified=true", "nameOfTheMinistry.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNameOfTheMinistryContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where nameOfTheMinistry contains
        defaultPurchaseOrderFiltering(
            "nameOfTheMinistry.contains=" + DEFAULT_NAME_OF_THE_MINISTRY,
            "nameOfTheMinistry.contains=" + UPDATED_NAME_OF_THE_MINISTRY
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByNameOfTheMinistryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where nameOfTheMinistry does not contain
        defaultPurchaseOrderFiltering(
            "nameOfTheMinistry.doesNotContain=" + UPDATED_NAME_OF_THE_MINISTRY,
            "nameOfTheMinistry.doesNotContain=" + DEFAULT_NAME_OF_THE_MINISTRY
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderNumber equals to
        defaultPurchaseOrderFiltering("orderNumber.equals=" + DEFAULT_ORDER_NUMBER, "orderNumber.equals=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderNumber in
        defaultPurchaseOrderFiltering(
            "orderNumber.in=" + DEFAULT_ORDER_NUMBER + "," + UPDATED_ORDER_NUMBER,
            "orderNumber.in=" + UPDATED_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderNumber is not null
        defaultPurchaseOrderFiltering("orderNumber.specified=true", "orderNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderNumber contains
        defaultPurchaseOrderFiltering("orderNumber.contains=" + DEFAULT_ORDER_NUMBER, "orderNumber.contains=" + UPDATED_ORDER_NUMBER);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderNumber does not contain
        defaultPurchaseOrderFiltering(
            "orderNumber.doesNotContain=" + UPDATED_ORDER_NUMBER,
            "orderNumber.doesNotContain=" + DEFAULT_ORDER_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderDate equals to
        defaultPurchaseOrderFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderDate in
        defaultPurchaseOrderFiltering(
            "orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE,
            "orderDate.in=" + UPDATED_ORDER_DATE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where orderDate is not null
        defaultPurchaseOrderFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax equals to
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.equals=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.equals=" + UPDATED_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax in
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.in=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX + "," + UPDATED_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.in=" + UPDATED_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax is not null
        defaultPurchaseOrderFiltering("totalAmountWithoutTax.specified=true", "totalAmountWithoutTax.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax is greater than or equal to
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax is less than or equal to
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax is less than
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.lessThan=" + UPDATED_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.lessThan=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithoutTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithoutTax is greater than
        defaultPurchaseOrderFiltering(
            "totalAmountWithoutTax.greaterThan=" + SMALLER_TOTAL_AMOUNT_WITHOUT_TAX,
            "totalAmountWithoutTax.greaterThan=" + DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate equals to
        defaultPurchaseOrderFiltering("taxRate.equals=" + DEFAULT_TAX_RATE, "taxRate.equals=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate in
        defaultPurchaseOrderFiltering("taxRate.in=" + DEFAULT_TAX_RATE + "," + UPDATED_TAX_RATE, "taxRate.in=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate is not null
        defaultPurchaseOrderFiltering("taxRate.specified=true", "taxRate.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate is greater than or equal to
        defaultPurchaseOrderFiltering("taxRate.greaterThanOrEqual=" + DEFAULT_TAX_RATE, "taxRate.greaterThanOrEqual=" + UPDATED_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate is less than or equal to
        defaultPurchaseOrderFiltering("taxRate.lessThanOrEqual=" + DEFAULT_TAX_RATE, "taxRate.lessThanOrEqual=" + SMALLER_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate is less than
        defaultPurchaseOrderFiltering("taxRate.lessThan=" + UPDATED_TAX_RATE, "taxRate.lessThan=" + DEFAULT_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTaxRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where taxRate is greater than
        defaultPurchaseOrderFiltering("taxRate.greaterThan=" + SMALLER_TAX_RATE, "taxRate.greaterThan=" + DEFAULT_TAX_RATE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount equals to
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.equals=" + DEFAULT_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.equals=" + UPDATED_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount in
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.in=" + DEFAULT_TOTAL_TAX_AMOUNT + "," + UPDATED_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.in=" + UPDATED_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount is not null
        defaultPurchaseOrderFiltering("totalTaxAmount.specified=true", "totalTaxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount is greater than or equal to
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.greaterThanOrEqual=" + UPDATED_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount is less than or equal to
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.lessThanOrEqual=" + DEFAULT_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.lessThanOrEqual=" + SMALLER_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount is less than
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.lessThan=" + UPDATED_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.lessThan=" + DEFAULT_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalTaxAmount is greater than
        defaultPurchaseOrderFiltering(
            "totalTaxAmount.greaterThan=" + SMALLER_TOTAL_TAX_AMOUNT,
            "totalTaxAmount.greaterThan=" + DEFAULT_TOTAL_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount equals to
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.equals=" + DEFAULT_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.equals=" + UPDATED_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount in
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.in=" + DEFAULT_PREPAID_TAX_AMOUNT + "," + UPDATED_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.in=" + UPDATED_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount is not null
        defaultPurchaseOrderFiltering("prepaidTaxAmount.specified=true", "prepaidTaxAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount is greater than or equal to
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.greaterThanOrEqual=" + DEFAULT_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.greaterThanOrEqual=" + UPDATED_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount is less than or equal to
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.lessThanOrEqual=" + DEFAULT_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.lessThanOrEqual=" + SMALLER_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount is less than
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.lessThan=" + UPDATED_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.lessThan=" + DEFAULT_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByPrepaidTaxAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where prepaidTaxAmount is greater than
        defaultPurchaseOrderFiltering(
            "prepaidTaxAmount.greaterThan=" + SMALLER_PREPAID_TAX_AMOUNT,
            "prepaidTaxAmount.greaterThan=" + DEFAULT_PREPAID_TAX_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax equals to
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.equals=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.equals=" + UPDATED_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax in
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.in=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX + "," + UPDATED_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.in=" + UPDATED_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax is not null
        defaultPurchaseOrderFiltering("totalAmountWithTax.specified=true", "totalAmountWithTax.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax is greater than or equal to
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax is less than or equal to
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax is less than
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.lessThan=" + UPDATED_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.lessThan=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByTotalAmountWithTaxIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where totalAmountWithTax is greater than
        defaultPurchaseOrderFiltering(
            "totalAmountWithTax.greaterThan=" + SMALLER_TOTAL_AMOUNT_WITH_TAX,
            "totalAmountWithTax.greaterThan=" + DEFAULT_TOTAL_AMOUNT_WITH_TAX
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAuthExpenditureNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where authExpenditureNumber equals to
        defaultPurchaseOrderFiltering(
            "authExpenditureNumber.equals=" + DEFAULT_AUTH_EXPENDITURE_NUMBER,
            "authExpenditureNumber.equals=" + UPDATED_AUTH_EXPENDITURE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAuthExpenditureNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where authExpenditureNumber in
        defaultPurchaseOrderFiltering(
            "authExpenditureNumber.in=" + DEFAULT_AUTH_EXPENDITURE_NUMBER + "," + UPDATED_AUTH_EXPENDITURE_NUMBER,
            "authExpenditureNumber.in=" + UPDATED_AUTH_EXPENDITURE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAuthExpenditureNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where authExpenditureNumber is not null
        defaultPurchaseOrderFiltering("authExpenditureNumber.specified=true", "authExpenditureNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAuthExpenditureNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where authExpenditureNumber contains
        defaultPurchaseOrderFiltering(
            "authExpenditureNumber.contains=" + DEFAULT_AUTH_EXPENDITURE_NUMBER,
            "authExpenditureNumber.contains=" + UPDATED_AUTH_EXPENDITURE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAuthExpenditureNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where authExpenditureNumber does not contain
        defaultPurchaseOrderFiltering(
            "authExpenditureNumber.doesNotContain=" + UPDATED_AUTH_EXPENDITURE_NUMBER,
            "authExpenditureNumber.doesNotContain=" + DEFAULT_AUTH_EXPENDITURE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits equals to
        defaultPurchaseOrderFiltering(
            "allocatedCredits.equals=" + DEFAULT_ALLOCATED_CREDITS,
            "allocatedCredits.equals=" + UPDATED_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits in
        defaultPurchaseOrderFiltering(
            "allocatedCredits.in=" + DEFAULT_ALLOCATED_CREDITS + "," + UPDATED_ALLOCATED_CREDITS,
            "allocatedCredits.in=" + UPDATED_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits is not null
        defaultPurchaseOrderFiltering("allocatedCredits.specified=true", "allocatedCredits.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits is greater than or equal to
        defaultPurchaseOrderFiltering(
            "allocatedCredits.greaterThanOrEqual=" + DEFAULT_ALLOCATED_CREDITS,
            "allocatedCredits.greaterThanOrEqual=" + UPDATED_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits is less than or equal to
        defaultPurchaseOrderFiltering(
            "allocatedCredits.lessThanOrEqual=" + DEFAULT_ALLOCATED_CREDITS,
            "allocatedCredits.lessThanOrEqual=" + SMALLER_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits is less than
        defaultPurchaseOrderFiltering(
            "allocatedCredits.lessThan=" + UPDATED_ALLOCATED_CREDITS,
            "allocatedCredits.lessThan=" + DEFAULT_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAllocatedCreditsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where allocatedCredits is greater than
        defaultPurchaseOrderFiltering(
            "allocatedCredits.greaterThan=" + SMALLER_ALLOCATED_CREDITS,
            "allocatedCredits.greaterThan=" + DEFAULT_ALLOCATED_CREDITS
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures equals to
        defaultPurchaseOrderFiltering(
            "committedExpenditures.equals=" + DEFAULT_COMMITTED_EXPENDITURES,
            "committedExpenditures.equals=" + UPDATED_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures in
        defaultPurchaseOrderFiltering(
            "committedExpenditures.in=" + DEFAULT_COMMITTED_EXPENDITURES + "," + UPDATED_COMMITTED_EXPENDITURES,
            "committedExpenditures.in=" + UPDATED_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures is not null
        defaultPurchaseOrderFiltering("committedExpenditures.specified=true", "committedExpenditures.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures is greater than or equal to
        defaultPurchaseOrderFiltering(
            "committedExpenditures.greaterThanOrEqual=" + DEFAULT_COMMITTED_EXPENDITURES,
            "committedExpenditures.greaterThanOrEqual=" + UPDATED_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures is less than or equal to
        defaultPurchaseOrderFiltering(
            "committedExpenditures.lessThanOrEqual=" + DEFAULT_COMMITTED_EXPENDITURES,
            "committedExpenditures.lessThanOrEqual=" + SMALLER_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures is less than
        defaultPurchaseOrderFiltering(
            "committedExpenditures.lessThan=" + UPDATED_COMMITTED_EXPENDITURES,
            "committedExpenditures.lessThan=" + DEFAULT_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByCommittedExpendituresIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where committedExpenditures is greater than
        defaultPurchaseOrderFiltering(
            "committedExpenditures.greaterThan=" + SMALLER_COMMITTED_EXPENDITURES,
            "committedExpenditures.greaterThan=" + DEFAULT_COMMITTED_EXPENDITURES
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance equals to
        defaultPurchaseOrderFiltering(
            "availableBalance.equals=" + DEFAULT_AVAILABLE_BALANCE,
            "availableBalance.equals=" + UPDATED_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance in
        defaultPurchaseOrderFiltering(
            "availableBalance.in=" + DEFAULT_AVAILABLE_BALANCE + "," + UPDATED_AVAILABLE_BALANCE,
            "availableBalance.in=" + UPDATED_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance is not null
        defaultPurchaseOrderFiltering("availableBalance.specified=true", "availableBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance is greater than or equal to
        defaultPurchaseOrderFiltering(
            "availableBalance.greaterThanOrEqual=" + DEFAULT_AVAILABLE_BALANCE,
            "availableBalance.greaterThanOrEqual=" + UPDATED_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance is less than or equal to
        defaultPurchaseOrderFiltering(
            "availableBalance.lessThanOrEqual=" + DEFAULT_AVAILABLE_BALANCE,
            "availableBalance.lessThanOrEqual=" + SMALLER_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance is less than
        defaultPurchaseOrderFiltering(
            "availableBalance.lessThan=" + UPDATED_AVAILABLE_BALANCE,
            "availableBalance.lessThan=" + DEFAULT_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAvailableBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        // Get all the purchaseOrderList where availableBalance is greater than
        defaultPurchaseOrderFiltering(
            "availableBalance.greaterThan=" + SMALLER_AVAILABLE_BALANCE,
            "availableBalance.greaterThan=" + DEFAULT_AVAILABLE_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByAnnexDecisionIsEqualToSomething() throws Exception {
        AnnexDecision annexDecision;
        if (TestUtil.findAll(em, AnnexDecision.class).isEmpty()) {
            purchaseOrderRepository.saveAndFlush(purchaseOrder);
            annexDecision = AnnexDecisionResourceIT.createEntity();
        } else {
            annexDecision = TestUtil.findAll(em, AnnexDecision.class).get(0);
        }
        em.persist(annexDecision);
        em.flush();
        purchaseOrder.setAnnexDecision(annexDecision);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long annexDecisionId = annexDecision.getId();
        // Get all the purchaseOrderList where annexDecision equals to annexDecisionId
        defaultPurchaseOrderShouldBeFound("annexDecisionId.equals=" + annexDecisionId);

        // Get all the purchaseOrderList where annexDecision equals to (annexDecisionId + 1)
        defaultPurchaseOrderShouldNotBeFound("annexDecisionId.equals=" + (annexDecisionId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersBySupplierIsEqualToSomething() throws Exception {
        Supplier supplier;
        if (TestUtil.findAll(em, Supplier.class).isEmpty()) {
            purchaseOrderRepository.saveAndFlush(purchaseOrder);
            supplier = SupplierResourceIT.createEntity();
        } else {
            supplier = TestUtil.findAll(em, Supplier.class).get(0);
        }
        em.persist(supplier);
        em.flush();
        purchaseOrder.setSupplier(supplier);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long supplierId = supplier.getId();
        // Get all the purchaseOrderList where supplier equals to supplierId
        defaultPurchaseOrderShouldBeFound("supplierId.equals=" + supplierId);

        // Get all the purchaseOrderList where supplier equals to (supplierId + 1)
        defaultPurchaseOrderShouldNotBeFound("supplierId.equals=" + (supplierId + 1));
    }

    @Test
    @Transactional
    void getAllPurchaseOrdersByEngagementIsEqualToSomething() throws Exception {
        Engagement engagement;
        if (TestUtil.findAll(em, Engagement.class).isEmpty()) {
            purchaseOrderRepository.saveAndFlush(purchaseOrder);
            engagement = EngagementResourceIT.createEntity();
        } else {
            engagement = TestUtil.findAll(em, Engagement.class).get(0);
        }
        em.persist(engagement);
        em.flush();
        purchaseOrder.setEngagement(engagement);
        purchaseOrderRepository.saveAndFlush(purchaseOrder);
        Long engagementId = engagement.getId();
        // Get all the purchaseOrderList where engagement equals to engagementId
        defaultPurchaseOrderShouldBeFound("engagementId.equals=" + engagementId);

        // Get all the purchaseOrderList where engagement equals to (engagementId + 1)
        defaultPurchaseOrderShouldNotBeFound("engagementId.equals=" + (engagementId + 1));
    }

    private void defaultPurchaseOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPurchaseOrderShouldBeFound(shouldBeFound);
        defaultPurchaseOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderShouldBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].nameOfTheMinistry").value(hasItem(DEFAULT_NAME_OF_THE_MINISTRY)))
            .andExpect(jsonPath("$.[*].orderNumber").value(hasItem(DEFAULT_ORDER_NUMBER)))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalAmountWithoutTax").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT_WITHOUT_TAX))))
            .andExpect(jsonPath("$.[*].taxRate").value(hasItem(sameNumber(DEFAULT_TAX_RATE))))
            .andExpect(jsonPath("$.[*].totalTaxAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].prepaidTaxAmount").value(hasItem(sameNumber(DEFAULT_PREPAID_TAX_AMOUNT))))
            .andExpect(jsonPath("$.[*].totalAmountWithTax").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT_WITH_TAX))))
            .andExpect(jsonPath("$.[*].authExpenditureNumber").value(hasItem(DEFAULT_AUTH_EXPENDITURE_NUMBER)))
            .andExpect(jsonPath("$.[*].allocatedCredits").value(hasItem(sameNumber(DEFAULT_ALLOCATED_CREDITS))))
            .andExpect(jsonPath("$.[*].committedExpenditures").value(hasItem(sameNumber(DEFAULT_COMMITTED_EXPENDITURES))))
            .andExpect(jsonPath("$.[*].availableBalance").value(hasItem(sameNumber(DEFAULT_AVAILABLE_BALANCE))));

        // Check, that the count call also returns 1
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrder() throws Exception {
        // Get the purchaseOrder
        restPurchaseOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseOrder() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrder
        PurchaseOrder updatedPurchaseOrder = purchaseOrderRepository.findById(purchaseOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchaseOrder are not directly saved in db
        em.detach(updatedPurchaseOrder);
        updatedPurchaseOrder
            .nameOfTheMinistry(UPDATED_NAME_OF_THE_MINISTRY)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .orderDate(UPDATED_ORDER_DATE)
            .totalAmountWithoutTax(UPDATED_TOTAL_AMOUNT_WITHOUT_TAX)
            .taxRate(UPDATED_TAX_RATE)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .prepaidTaxAmount(UPDATED_PREPAID_TAX_AMOUNT)
            .totalAmountWithTax(UPDATED_TOTAL_AMOUNT_WITH_TAX)
            .authExpenditureNumber(UPDATED_AUTH_EXPENDITURE_NUMBER)
            .allocatedCredits(UPDATED_ALLOCATED_CREDITS)
            .committedExpenditures(UPDATED_COMMITTED_EXPENDITURES)
            .availableBalance(UPDATED_AVAILABLE_BALANCE);

        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchaseOrderToMatchAllProperties(updatedPurchaseOrder);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .nameOfTheMinistry(UPDATED_NAME_OF_THE_MINISTRY)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .totalAmountWithoutTax(UPDATED_TOTAL_AMOUNT_WITHOUT_TAX)
            .taxRate(UPDATED_TAX_RATE)
            .prepaidTaxAmount(UPDATED_PREPAID_TAX_AMOUNT)
            .totalAmountWithTax(UPDATED_TOTAL_AMOUNT_WITH_TAX)
            .allocatedCredits(UPDATED_ALLOCATED_CREDITS)
            .committedExpenditures(UPDATED_COMMITTED_EXPENDITURES);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseOrderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchaseOrder, purchaseOrder),
            getPersistedPurchaseOrder(purchaseOrder)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderWithPatch() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrder using partial update
        PurchaseOrder partialUpdatedPurchaseOrder = new PurchaseOrder();
        partialUpdatedPurchaseOrder.setId(purchaseOrder.getId());

        partialUpdatedPurchaseOrder
            .nameOfTheMinistry(UPDATED_NAME_OF_THE_MINISTRY)
            .orderNumber(UPDATED_ORDER_NUMBER)
            .orderDate(UPDATED_ORDER_DATE)
            .totalAmountWithoutTax(UPDATED_TOTAL_AMOUNT_WITHOUT_TAX)
            .taxRate(UPDATED_TAX_RATE)
            .totalTaxAmount(UPDATED_TOTAL_TAX_AMOUNT)
            .prepaidTaxAmount(UPDATED_PREPAID_TAX_AMOUNT)
            .totalAmountWithTax(UPDATED_TOTAL_AMOUNT_WITH_TAX)
            .authExpenditureNumber(UPDATED_AUTH_EXPENDITURE_NUMBER)
            .allocatedCredits(UPDATED_ALLOCATED_CREDITS)
            .committedExpenditures(UPDATED_COMMITTED_EXPENDITURES)
            .availableBalance(UPDATED_AVAILABLE_BALANCE);

        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchaseOrder))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseOrderUpdatableFieldsEquals(partialUpdatedPurchaseOrder, getPersistedPurchaseOrder(partialUpdatedPurchaseOrder));
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchaseOrder))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrder.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchaseOrder)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrder() throws Exception {
        // Initialize the database
        insertedPurchaseOrder = purchaseOrderRepository.saveAndFlush(purchaseOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchaseOrder
        restPurchaseOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchaseOrderRepository.count();
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

    protected PurchaseOrder getPersistedPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderRepository.findById(purchaseOrder.getId()).orElseThrow();
    }

    protected void assertPersistedPurchaseOrderToMatchAllProperties(PurchaseOrder expectedPurchaseOrder) {
        assertPurchaseOrderAllPropertiesEquals(expectedPurchaseOrder, getPersistedPurchaseOrder(expectedPurchaseOrder));
    }

    protected void assertPersistedPurchaseOrderToMatchUpdatableProperties(PurchaseOrder expectedPurchaseOrder) {
        assertPurchaseOrderAllUpdatablePropertiesEquals(expectedPurchaseOrder, getPersistedPurchaseOrder(expectedPurchaseOrder));
    }
}
