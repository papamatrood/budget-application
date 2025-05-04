package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.PurchaseOrderItemAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static com.cratechnologie.budget.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.PurchaseOrder;
import com.cratechnologie.budget.domain.PurchaseOrderItem;
import com.cratechnologie.budget.repository.PurchaseOrderItemRepository;
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
 * Integration tests for the {@link PurchaseOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PurchaseOrderItemResourceIT {

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final String ENTITY_API_URL = "/api/purchase-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPurchaseOrderItemMockMvc;

    private PurchaseOrderItem purchaseOrderItem;

    private PurchaseOrderItem insertedPurchaseOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderItem createEntity() {
        return new PurchaseOrderItem()
            .productName(DEFAULT_PRODUCT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .totalAmount(DEFAULT_TOTAL_AMOUNT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseOrderItem createUpdatedEntity() {
        return new PurchaseOrderItem()
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);
    }

    @BeforeEach
    void initTest() {
        purchaseOrderItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPurchaseOrderItem != null) {
            purchaseOrderItemRepository.delete(insertedPurchaseOrderItem);
            insertedPurchaseOrderItem = null;
        }
    }

    @Test
    @Transactional
    void createPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PurchaseOrderItem
        var returnedPurchaseOrderItem = om.readValue(
            restPurchaseOrderItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PurchaseOrderItem.class
        );

        // Validate the PurchaseOrderItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPurchaseOrderItemUpdatableFieldsEquals(returnedPurchaseOrderItem, getPersistedPurchaseOrderItem(returnedPurchaseOrderItem));

        insertedPurchaseOrderItem = returnedPurchaseOrderItem;
    }

    @Test
    @Transactional
    void createPurchaseOrderItemWithExistingId() throws Exception {
        // Create the PurchaseOrderItem with an existing ID
        purchaseOrderItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProductNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrderItem.setProductName(null);

        // Create the PurchaseOrderItem, which fails.

        restPurchaseOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrderItem.setQuantity(null);

        // Create the PurchaseOrderItem, which fails.

        restPurchaseOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrderItem.setUnitPrice(null);

        // Create the PurchaseOrderItem, which fails.

        restPurchaseOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        purchaseOrderItem.setTotalAmount(null);

        // Create the PurchaseOrderItem, which fails.

        restPurchaseOrderItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItems() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));
    }

    @Test
    @Transactional
    void getPurchaseOrderItem() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, purchaseOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.totalAmount").value(sameNumber(DEFAULT_TOTAL_AMOUNT)));
    }

    @Test
    @Transactional
    void getPurchaseOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        Long id = purchaseOrderItem.getId();

        defaultPurchaseOrderItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPurchaseOrderItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPurchaseOrderItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByProductNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where productName equals to
        defaultPurchaseOrderItemFiltering("productName.equals=" + DEFAULT_PRODUCT_NAME, "productName.equals=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByProductNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where productName in
        defaultPurchaseOrderItemFiltering(
            "productName.in=" + DEFAULT_PRODUCT_NAME + "," + UPDATED_PRODUCT_NAME,
            "productName.in=" + UPDATED_PRODUCT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByProductNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where productName is not null
        defaultPurchaseOrderItemFiltering("productName.specified=true", "productName.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByProductNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where productName contains
        defaultPurchaseOrderItemFiltering("productName.contains=" + DEFAULT_PRODUCT_NAME, "productName.contains=" + UPDATED_PRODUCT_NAME);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByProductNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where productName does not contain
        defaultPurchaseOrderItemFiltering(
            "productName.doesNotContain=" + UPDATED_PRODUCT_NAME,
            "productName.doesNotContain=" + DEFAULT_PRODUCT_NAME
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity equals to
        defaultPurchaseOrderItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity in
        defaultPurchaseOrderItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity is not null
        defaultPurchaseOrderItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity is greater than or equal to
        defaultPurchaseOrderItemFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity is less than or equal to
        defaultPurchaseOrderItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity is less than
        defaultPurchaseOrderItemFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where quantity is greater than
        defaultPurchaseOrderItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice equals to
        defaultPurchaseOrderItemFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice in
        defaultPurchaseOrderItemFiltering(
            "unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE,
            "unitPrice.in=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice is not null
        defaultPurchaseOrderItemFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice is greater than or equal to
        defaultPurchaseOrderItemFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice is less than or equal to
        defaultPurchaseOrderItemFiltering(
            "unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice is less than
        defaultPurchaseOrderItemFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where unitPrice is greater than
        defaultPurchaseOrderItemFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount equals to
        defaultPurchaseOrderItemFiltering("totalAmount.equals=" + DEFAULT_TOTAL_AMOUNT, "totalAmount.equals=" + UPDATED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount in
        defaultPurchaseOrderItemFiltering(
            "totalAmount.in=" + DEFAULT_TOTAL_AMOUNT + "," + UPDATED_TOTAL_AMOUNT,
            "totalAmount.in=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount is not null
        defaultPurchaseOrderItemFiltering("totalAmount.specified=true", "totalAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount is greater than or equal to
        defaultPurchaseOrderItemFiltering(
            "totalAmount.greaterThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.greaterThanOrEqual=" + UPDATED_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount is less than or equal to
        defaultPurchaseOrderItemFiltering(
            "totalAmount.lessThanOrEqual=" + DEFAULT_TOTAL_AMOUNT,
            "totalAmount.lessThanOrEqual=" + SMALLER_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount is less than
        defaultPurchaseOrderItemFiltering("totalAmount.lessThan=" + UPDATED_TOTAL_AMOUNT, "totalAmount.lessThan=" + DEFAULT_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        // Get all the purchaseOrderItemList where totalAmount is greater than
        defaultPurchaseOrderItemFiltering(
            "totalAmount.greaterThan=" + SMALLER_TOTAL_AMOUNT,
            "totalAmount.greaterThan=" + DEFAULT_TOTAL_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllPurchaseOrderItemsByPurchaseOrderIsEqualToSomething() throws Exception {
        PurchaseOrder purchaseOrder;
        if (TestUtil.findAll(em, PurchaseOrder.class).isEmpty()) {
            purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);
            purchaseOrder = PurchaseOrderResourceIT.createEntity();
        } else {
            purchaseOrder = TestUtil.findAll(em, PurchaseOrder.class).get(0);
        }
        em.persist(purchaseOrder);
        em.flush();
        purchaseOrderItem.setPurchaseOrder(purchaseOrder);
        purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);
        Long purchaseOrderId = purchaseOrder.getId();
        // Get all the purchaseOrderItemList where purchaseOrder equals to purchaseOrderId
        defaultPurchaseOrderItemShouldBeFound("purchaseOrderId.equals=" + purchaseOrderId);

        // Get all the purchaseOrderItemList where purchaseOrder equals to (purchaseOrderId + 1)
        defaultPurchaseOrderItemShouldNotBeFound("purchaseOrderId.equals=" + (purchaseOrderId + 1));
    }

    private void defaultPurchaseOrderItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPurchaseOrderItemShouldBeFound(shouldBeFound);
        defaultPurchaseOrderItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPurchaseOrderItemShouldBeFound(String filter) throws Exception {
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].totalAmount").value(hasItem(sameNumber(DEFAULT_TOTAL_AMOUNT))));

        // Check, that the count call also returns 1
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPurchaseOrderItemShouldNotBeFound(String filter) throws Exception {
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPurchaseOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPurchaseOrderItem() throws Exception {
        // Get the purchaseOrderItem
        restPurchaseOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPurchaseOrderItem() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrderItem
        PurchaseOrderItem updatedPurchaseOrderItem = purchaseOrderItemRepository.findById(purchaseOrderItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPurchaseOrderItem are not directly saved in db
        em.detach(updatedPurchaseOrderItem);
        updatedPurchaseOrderItem
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPurchaseOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPurchaseOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPurchaseOrderItemToMatchAllProperties(updatedPurchaseOrderItem);
    }

    @Test
    @Transactional
    void putNonExistingPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, purchaseOrderItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchaseOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(purchaseOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePurchaseOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrderItem using partial update
        PurchaseOrderItem partialUpdatedPurchaseOrderItem = new PurchaseOrderItem();
        partialUpdatedPurchaseOrderItem.setId(purchaseOrderItem.getId());

        partialUpdatedPurchaseOrderItem.productName(UPDATED_PRODUCT_NAME).totalAmount(UPDATED_TOTAL_AMOUNT);

        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchaseOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseOrderItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPurchaseOrderItem, purchaseOrderItem),
            getPersistedPurchaseOrderItem(purchaseOrderItem)
        );
    }

    @Test
    @Transactional
    void fullUpdatePurchaseOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the purchaseOrderItem using partial update
        PurchaseOrderItem partialUpdatedPurchaseOrderItem = new PurchaseOrderItem();
        partialUpdatedPurchaseOrderItem.setId(purchaseOrderItem.getId());

        partialUpdatedPurchaseOrderItem
            .productName(UPDATED_PRODUCT_NAME)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalAmount(UPDATED_TOTAL_AMOUNT);

        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPurchaseOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPurchaseOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the PurchaseOrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPurchaseOrderItemUpdatableFieldsEquals(
            partialUpdatedPurchaseOrderItem,
            getPersistedPurchaseOrderItem(partialUpdatedPurchaseOrderItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, purchaseOrderItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchaseOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(purchaseOrderItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPurchaseOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        purchaseOrderItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPurchaseOrderItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(purchaseOrderItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PurchaseOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePurchaseOrderItem() throws Exception {
        // Initialize the database
        insertedPurchaseOrderItem = purchaseOrderItemRepository.saveAndFlush(purchaseOrderItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the purchaseOrderItem
        restPurchaseOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, purchaseOrderItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return purchaseOrderItemRepository.count();
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

    protected PurchaseOrderItem getPersistedPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        return purchaseOrderItemRepository.findById(purchaseOrderItem.getId()).orElseThrow();
    }

    protected void assertPersistedPurchaseOrderItemToMatchAllProperties(PurchaseOrderItem expectedPurchaseOrderItem) {
        assertPurchaseOrderItemAllPropertiesEquals(expectedPurchaseOrderItem, getPersistedPurchaseOrderItem(expectedPurchaseOrderItem));
    }

    protected void assertPersistedPurchaseOrderItemToMatchUpdatableProperties(PurchaseOrderItem expectedPurchaseOrderItem) {
        assertPurchaseOrderItemAllUpdatablePropertiesEquals(
            expectedPurchaseOrderItem,
            getPersistedPurchaseOrderItem(expectedPurchaseOrderItem)
        );
    }
}
