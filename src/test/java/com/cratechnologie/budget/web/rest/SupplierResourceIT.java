package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.SupplierAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.Supplier;
import com.cratechnologie.budget.repository.SupplierRepository;
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
 * Integration tests for the {@link SupplierResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SupplierResourceIT {

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_NIF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NIF_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_COMMERCIAL_REGISTER = "AAAAAAAAAA";
    private static final String UPDATED_COMMERCIAL_REGISTER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_BANK_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_MANDATING_ESTABLISHMENT = "AAAAAAAAAA";
    private static final String UPDATED_MANDATING_ESTABLISHMENT = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACTLASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_CONTACTLASTNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/suppliers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    private Supplier insertedSupplier;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity() {
        return new Supplier()
            .companyName(DEFAULT_COMPANY_NAME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .nifNumber(DEFAULT_NIF_NUMBER)
            .commercialRegister(DEFAULT_COMMERCIAL_REGISTER)
            .bankAccount(DEFAULT_BANK_ACCOUNT)
            .mandatingEstablishment(DEFAULT_MANDATING_ESTABLISHMENT)
            .email(DEFAULT_EMAIL)
            .website(DEFAULT_WEBSITE)
            .description(DEFAULT_DESCRIPTION)
            .contactFirstname(DEFAULT_CONTACT_FIRSTNAME)
            .contactlastname(DEFAULT_CONTACTLASTNAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createUpdatedEntity() {
        return new Supplier()
            .companyName(UPDATED_COMPANY_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .nifNumber(UPDATED_NIF_NUMBER)
            .commercialRegister(UPDATED_COMMERCIAL_REGISTER)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .mandatingEstablishment(UPDATED_MANDATING_ESTABLISHMENT)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .contactFirstname(UPDATED_CONTACT_FIRSTNAME)
            .contactlastname(UPDATED_CONTACTLASTNAME);
    }

    @BeforeEach
    void initTest() {
        supplier = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSupplier != null) {
            supplierRepository.delete(insertedSupplier);
            insertedSupplier = null;
        }
    }

    @Test
    @Transactional
    void createSupplier() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Supplier
        var returnedSupplier = om.readValue(
            restSupplierMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplier)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Supplier.class
        );

        // Validate the Supplier in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSupplierUpdatableFieldsEquals(returnedSupplier, getPersistedSupplier(returnedSupplier));

        insertedSupplier = returnedSupplier;
    }

    @Test
    @Transactional
    void createSupplierWithExistingId() throws Exception {
        // Create the Supplier with an existing ID
        supplier.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSupplierMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplier)))
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSuppliers() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].nifNumber").value(hasItem(DEFAULT_NIF_NUMBER)))
            .andExpect(jsonPath("$.[*].commercialRegister").value(hasItem(DEFAULT_COMMERCIAL_REGISTER)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].mandatingEstablishment").value(hasItem(DEFAULT_MANDATING_ESTABLISHMENT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactFirstname").value(hasItem(DEFAULT_CONTACT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].contactlastname").value(hasItem(DEFAULT_CONTACTLASTNAME)));
    }

    @Test
    @Transactional
    void getSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL_ID, supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.nifNumber").value(DEFAULT_NIF_NUMBER))
            .andExpect(jsonPath("$.commercialRegister").value(DEFAULT_COMMERCIAL_REGISTER))
            .andExpect(jsonPath("$.bankAccount").value(DEFAULT_BANK_ACCOUNT))
            .andExpect(jsonPath("$.mandatingEstablishment").value(DEFAULT_MANDATING_ESTABLISHMENT))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.contactFirstname").value(DEFAULT_CONTACT_FIRSTNAME))
            .andExpect(jsonPath("$.contactlastname").value(DEFAULT_CONTACTLASTNAME));
    }

    @Test
    @Transactional
    void getSuppliersByIdFiltering() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        Long id = supplier.getId();

        defaultSupplierFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSupplierFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSupplierFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSuppliersByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where companyName equals to
        defaultSupplierFiltering("companyName.equals=" + DEFAULT_COMPANY_NAME, "companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where companyName in
        defaultSupplierFiltering(
            "companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME,
            "companyName.in=" + UPDATED_COMPANY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where companyName is not null
        defaultSupplierFiltering("companyName.specified=true", "companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where companyName contains
        defaultSupplierFiltering("companyName.contains=" + DEFAULT_COMPANY_NAME, "companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where companyName does not contain
        defaultSupplierFiltering(
            "companyName.doesNotContain=" + UPDATED_COMPANY_NAME,
            "companyName.doesNotContain=" + DEFAULT_COMPANY_NAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address equals to
        defaultSupplierFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address in
        defaultSupplierFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address is not null
        defaultSupplierFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address contains
        defaultSupplierFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSuppliersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where address does not contain
        defaultSupplierFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone equals to
        defaultSupplierFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone in
        defaultSupplierFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone is not null
        defaultSupplierFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone contains
        defaultSupplierFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where phone does not contain
        defaultSupplierFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllSuppliersByNifNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where nifNumber equals to
        defaultSupplierFiltering("nifNumber.equals=" + DEFAULT_NIF_NUMBER, "nifNumber.equals=" + UPDATED_NIF_NUMBER);
    }

    @Test
    @Transactional
    void getAllSuppliersByNifNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where nifNumber in
        defaultSupplierFiltering("nifNumber.in=" + DEFAULT_NIF_NUMBER + "," + UPDATED_NIF_NUMBER, "nifNumber.in=" + UPDATED_NIF_NUMBER);
    }

    @Test
    @Transactional
    void getAllSuppliersByNifNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where nifNumber is not null
        defaultSupplierFiltering("nifNumber.specified=true", "nifNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByNifNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where nifNumber contains
        defaultSupplierFiltering("nifNumber.contains=" + DEFAULT_NIF_NUMBER, "nifNumber.contains=" + UPDATED_NIF_NUMBER);
    }

    @Test
    @Transactional
    void getAllSuppliersByNifNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where nifNumber does not contain
        defaultSupplierFiltering("nifNumber.doesNotContain=" + UPDATED_NIF_NUMBER, "nifNumber.doesNotContain=" + DEFAULT_NIF_NUMBER);
    }

    @Test
    @Transactional
    void getAllSuppliersByCommercialRegisterIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where commercialRegister equals to
        defaultSupplierFiltering(
            "commercialRegister.equals=" + DEFAULT_COMMERCIAL_REGISTER,
            "commercialRegister.equals=" + UPDATED_COMMERCIAL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByCommercialRegisterIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where commercialRegister in
        defaultSupplierFiltering(
            "commercialRegister.in=" + DEFAULT_COMMERCIAL_REGISTER + "," + UPDATED_COMMERCIAL_REGISTER,
            "commercialRegister.in=" + UPDATED_COMMERCIAL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByCommercialRegisterIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where commercialRegister is not null
        defaultSupplierFiltering("commercialRegister.specified=true", "commercialRegister.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByCommercialRegisterContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where commercialRegister contains
        defaultSupplierFiltering(
            "commercialRegister.contains=" + DEFAULT_COMMERCIAL_REGISTER,
            "commercialRegister.contains=" + UPDATED_COMMERCIAL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByCommercialRegisterNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where commercialRegister does not contain
        defaultSupplierFiltering(
            "commercialRegister.doesNotContain=" + UPDATED_COMMERCIAL_REGISTER,
            "commercialRegister.doesNotContain=" + DEFAULT_COMMERCIAL_REGISTER
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByBankAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount equals to
        defaultSupplierFiltering("bankAccount.equals=" + DEFAULT_BANK_ACCOUNT, "bankAccount.equals=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllSuppliersByBankAccountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount in
        defaultSupplierFiltering(
            "bankAccount.in=" + DEFAULT_BANK_ACCOUNT + "," + UPDATED_BANK_ACCOUNT,
            "bankAccount.in=" + UPDATED_BANK_ACCOUNT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByBankAccountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount is not null
        defaultSupplierFiltering("bankAccount.specified=true", "bankAccount.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByBankAccountContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount contains
        defaultSupplierFiltering("bankAccount.contains=" + DEFAULT_BANK_ACCOUNT, "bankAccount.contains=" + UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    void getAllSuppliersByBankAccountNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where bankAccount does not contain
        defaultSupplierFiltering(
            "bankAccount.doesNotContain=" + UPDATED_BANK_ACCOUNT,
            "bankAccount.doesNotContain=" + DEFAULT_BANK_ACCOUNT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByMandatingEstablishmentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where mandatingEstablishment equals to
        defaultSupplierFiltering(
            "mandatingEstablishment.equals=" + DEFAULT_MANDATING_ESTABLISHMENT,
            "mandatingEstablishment.equals=" + UPDATED_MANDATING_ESTABLISHMENT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByMandatingEstablishmentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where mandatingEstablishment in
        defaultSupplierFiltering(
            "mandatingEstablishment.in=" + DEFAULT_MANDATING_ESTABLISHMENT + "," + UPDATED_MANDATING_ESTABLISHMENT,
            "mandatingEstablishment.in=" + UPDATED_MANDATING_ESTABLISHMENT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByMandatingEstablishmentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where mandatingEstablishment is not null
        defaultSupplierFiltering("mandatingEstablishment.specified=true", "mandatingEstablishment.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByMandatingEstablishmentContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where mandatingEstablishment contains
        defaultSupplierFiltering(
            "mandatingEstablishment.contains=" + DEFAULT_MANDATING_ESTABLISHMENT,
            "mandatingEstablishment.contains=" + UPDATED_MANDATING_ESTABLISHMENT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByMandatingEstablishmentNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where mandatingEstablishment does not contain
        defaultSupplierFiltering(
            "mandatingEstablishment.doesNotContain=" + UPDATED_MANDATING_ESTABLISHMENT,
            "mandatingEstablishment.doesNotContain=" + DEFAULT_MANDATING_ESTABLISHMENT
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email equals to
        defaultSupplierFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email in
        defaultSupplierFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email is not null
        defaultSupplierFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email contains
        defaultSupplierFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where email does not contain
        defaultSupplierFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllSuppliersByWebsiteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where website equals to
        defaultSupplierFiltering("website.equals=" + DEFAULT_WEBSITE, "website.equals=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllSuppliersByWebsiteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where website in
        defaultSupplierFiltering("website.in=" + DEFAULT_WEBSITE + "," + UPDATED_WEBSITE, "website.in=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllSuppliersByWebsiteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where website is not null
        defaultSupplierFiltering("website.specified=true", "website.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByWebsiteContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where website contains
        defaultSupplierFiltering("website.contains=" + DEFAULT_WEBSITE, "website.contains=" + UPDATED_WEBSITE);
    }

    @Test
    @Transactional
    void getAllSuppliersByWebsiteNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where website does not contain
        defaultSupplierFiltering("website.doesNotContain=" + UPDATED_WEBSITE, "website.doesNotContain=" + DEFAULT_WEBSITE);
    }

    @Test
    @Transactional
    void getAllSuppliersByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where description equals to
        defaultSupplierFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSuppliersByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where description in
        defaultSupplierFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where description is not null
        defaultSupplierFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where description contains
        defaultSupplierFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSuppliersByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where description does not contain
        defaultSupplierFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSuppliersByContactFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactFirstname equals to
        defaultSupplierFiltering(
            "contactFirstname.equals=" + DEFAULT_CONTACT_FIRSTNAME,
            "contactFirstname.equals=" + UPDATED_CONTACT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactFirstname in
        defaultSupplierFiltering(
            "contactFirstname.in=" + DEFAULT_CONTACT_FIRSTNAME + "," + UPDATED_CONTACT_FIRSTNAME,
            "contactFirstname.in=" + UPDATED_CONTACT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactFirstname is not null
        defaultSupplierFiltering("contactFirstname.specified=true", "contactFirstname.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByContactFirstnameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactFirstname contains
        defaultSupplierFiltering(
            "contactFirstname.contains=" + DEFAULT_CONTACT_FIRSTNAME,
            "contactFirstname.contains=" + UPDATED_CONTACT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactFirstname does not contain
        defaultSupplierFiltering(
            "contactFirstname.doesNotContain=" + UPDATED_CONTACT_FIRSTNAME,
            "contactFirstname.doesNotContain=" + DEFAULT_CONTACT_FIRSTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactlastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactlastname equals to
        defaultSupplierFiltering("contactlastname.equals=" + DEFAULT_CONTACTLASTNAME, "contactlastname.equals=" + UPDATED_CONTACTLASTNAME);
    }

    @Test
    @Transactional
    void getAllSuppliersByContactlastnameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactlastname in
        defaultSupplierFiltering(
            "contactlastname.in=" + DEFAULT_CONTACTLASTNAME + "," + UPDATED_CONTACTLASTNAME,
            "contactlastname.in=" + UPDATED_CONTACTLASTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactlastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactlastname is not null
        defaultSupplierFiltering("contactlastname.specified=true", "contactlastname.specified=false");
    }

    @Test
    @Transactional
    void getAllSuppliersByContactlastnameContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactlastname contains
        defaultSupplierFiltering(
            "contactlastname.contains=" + DEFAULT_CONTACTLASTNAME,
            "contactlastname.contains=" + UPDATED_CONTACTLASTNAME
        );
    }

    @Test
    @Transactional
    void getAllSuppliersByContactlastnameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        // Get all the supplierList where contactlastname does not contain
        defaultSupplierFiltering(
            "contactlastname.doesNotContain=" + UPDATED_CONTACTLASTNAME,
            "contactlastname.doesNotContain=" + DEFAULT_CONTACTLASTNAME
        );
    }

    private void defaultSupplierFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSupplierShouldBeFound(shouldBeFound);
        defaultSupplierShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSupplierShouldBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].nifNumber").value(hasItem(DEFAULT_NIF_NUMBER)))
            .andExpect(jsonPath("$.[*].commercialRegister").value(hasItem(DEFAULT_COMMERCIAL_REGISTER)))
            .andExpect(jsonPath("$.[*].bankAccount").value(hasItem(DEFAULT_BANK_ACCOUNT)))
            .andExpect(jsonPath("$.[*].mandatingEstablishment").value(hasItem(DEFAULT_MANDATING_ESTABLISHMENT)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].contactFirstname").value(hasItem(DEFAULT_CONTACT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].contactlastname").value(hasItem(DEFAULT_CONTACTLASTNAME)));

        // Check, that the count call also returns 1
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSupplierShouldNotBeFound(String filter) throws Exception {
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSupplierMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findById(supplier.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSupplier are not directly saved in db
        em.detach(updatedSupplier);
        updatedSupplier
            .companyName(UPDATED_COMPANY_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .nifNumber(UPDATED_NIF_NUMBER)
            .commercialRegister(UPDATED_COMMERCIAL_REGISTER)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .mandatingEstablishment(UPDATED_MANDATING_ESTABLISHMENT)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .contactFirstname(UPDATED_CONTACT_FIRSTNAME)
            .contactlastname(UPDATED_CONTACTLASTNAME);

        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSupplier.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSupplierToMatchAllProperties(updatedSupplier);
    }

    @Test
    @Transactional
    void putNonExistingSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, supplier.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .companyName(UPDATED_COMPANY_NAME)
            .nifNumber(UPDATED_NIF_NUMBER)
            .commercialRegister(UPDATED_COMMERCIAL_REGISTER)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .email(UPDATED_EMAIL)
            .description(UPDATED_DESCRIPTION)
            .contactFirstname(UPDATED_CONTACT_FIRSTNAME)
            .contactlastname(UPDATED_CONTACTLASTNAME);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSupplier, supplier), getPersistedSupplier(supplier));
    }

    @Test
    @Transactional
    void fullUpdateSupplierWithPatch() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the supplier using partial update
        Supplier partialUpdatedSupplier = new Supplier();
        partialUpdatedSupplier.setId(supplier.getId());

        partialUpdatedSupplier
            .companyName(UPDATED_COMPANY_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .nifNumber(UPDATED_NIF_NUMBER)
            .commercialRegister(UPDATED_COMMERCIAL_REGISTER)
            .bankAccount(UPDATED_BANK_ACCOUNT)
            .mandatingEstablishment(UPDATED_MANDATING_ESTABLISHMENT)
            .email(UPDATED_EMAIL)
            .website(UPDATED_WEBSITE)
            .description(UPDATED_DESCRIPTION)
            .contactFirstname(UPDATED_CONTACT_FIRSTNAME)
            .contactlastname(UPDATED_CONTACTLASTNAME);

        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSupplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSupplier))
            )
            .andExpect(status().isOk());

        // Validate the Supplier in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSupplierUpdatableFieldsEquals(partialUpdatedSupplier, getPersistedSupplier(partialUpdatedSupplier));
    }

    @Test
    @Transactional
    void patchNonExistingSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, supplier.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(supplier))
            )
            .andExpect(status().isBadRequest());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSupplier() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        supplier.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSupplierMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(supplier)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Supplier in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSupplier() throws Exception {
        // Initialize the database
        insertedSupplier = supplierRepository.saveAndFlush(supplier);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the supplier
        restSupplierMockMvc
            .perform(delete(ENTITY_API_URL_ID, supplier.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return supplierRepository.count();
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

    protected Supplier getPersistedSupplier(Supplier supplier) {
        return supplierRepository.findById(supplier.getId()).orElseThrow();
    }

    protected void assertPersistedSupplierToMatchAllProperties(Supplier expectedSupplier) {
        assertSupplierAllPropertiesEquals(expectedSupplier, getPersistedSupplier(expectedSupplier));
    }

    protected void assertPersistedSupplierToMatchUpdatableProperties(Supplier expectedSupplier) {
        assertSupplierAllUpdatablePropertiesEquals(expectedSupplier, getPersistedSupplier(expectedSupplier));
    }
}
