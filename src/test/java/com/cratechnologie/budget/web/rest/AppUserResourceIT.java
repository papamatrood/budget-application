package com.cratechnologie.budget.web.rest;

import static com.cratechnologie.budget.domain.AppUserAsserts.*;
import static com.cratechnologie.budget.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.budget.IntegrationTest;
import com.cratechnologie.budget.domain.AppUser;
import com.cratechnologie.budget.domain.User;
import com.cratechnologie.budget.domain.enumeration.FamilySituationEnum;
import com.cratechnologie.budget.domain.enumeration.GenderEnum;
import com.cratechnologie.budget.repository.AppUserRepository;
import com.cratechnologie.budget.repository.UserRepository;
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
 * Integration tests for the {@link AppUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AppUserResourceIT {

    private static final Boolean DEFAULT_ACCOUNT_STATUS = false;
    private static final Boolean UPDATED_ACCOUNT_STATUS = true;

    private static final Instant DEFAULT_LAST_DATE_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_DATE_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_BIRTH_PLACE = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_PLACE = "BBBBBBBBBB";

    private static final GenderEnum DEFAULT_GENDER = GenderEnum.MALE;
    private static final GenderEnum UPDATED_GENDER = GenderEnum.FEMALE;

    private static final FamilySituationEnum DEFAULT_FAMILY_SITUATION = FamilySituationEnum.CELIBATAIRE;
    private static final FamilySituationEnum UPDATED_FAMILY_SITUATION = FamilySituationEnum.MARIE;

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/app-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAppUserMockMvc;

    private AppUser appUser;

    private AppUser insertedAppUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createEntity() {
        return new AppUser()
            .accountStatus(DEFAULT_ACCOUNT_STATUS)
            .lastDateUpdate(DEFAULT_LAST_DATE_UPDATE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .firstname(DEFAULT_FIRSTNAME)
            .lastname(DEFAULT_LASTNAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .birthDate(DEFAULT_BIRTH_DATE)
            .birthPlace(DEFAULT_BIRTH_PLACE)
            .gender(DEFAULT_GENDER)
            .familySituation(DEFAULT_FAMILY_SITUATION)
            .position(DEFAULT_POSITION)
            .address(DEFAULT_ADDRESS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppUser createUpdatedEntity() {
        return new AppUser()
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .lastDateUpdate(UPDATED_LAST_DATE_UPDATE)
            .dateCreated(UPDATED_DATE_CREATED)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .gender(UPDATED_GENDER)
            .familySituation(UPDATED_FAMILY_SITUATION)
            .position(UPDATED_POSITION)
            .address(UPDATED_ADDRESS);
    }

    @BeforeEach
    void initTest() {
        appUser = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAppUser != null) {
            appUserRepository.delete(insertedAppUser);
            insertedAppUser = null;
        }
    }

    @Test
    @Transactional
    void createAppUser() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AppUser
        var returnedAppUser = om.readValue(
            restAppUserMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUser)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AppUser.class
        );

        // Validate the AppUser in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAppUserUpdatableFieldsEquals(returnedAppUser, getPersistedAppUser(returnedAppUser));

        insertedAppUser = returnedAppUser;
    }

    @Test
    @Transactional
    void createAppUserWithExistingId() throws Exception {
        // Create the AppUser with an existing ID
        appUser.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppUserMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAppUsers() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS)))
            .andExpect(jsonPath("$.[*].lastDateUpdate").value(hasItem(DEFAULT_LAST_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].familySituation").value(hasItem(DEFAULT_FAMILY_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @Test
    @Transactional
    void getAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get the appUser
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL_ID, appUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appUser.getId().intValue()))
            .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS))
            .andExpect(jsonPath("$.lastDateUpdate").value(DEFAULT_LAST_DATE_UPDATE.toString()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.birthPlace").value(DEFAULT_BIRTH_PLACE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.familySituation").value(DEFAULT_FAMILY_SITUATION.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getAppUsersByIdFiltering() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        Long id = appUser.getId();

        defaultAppUserFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAppUserFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAppUserFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAppUsersByAccountStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where accountStatus equals to
        defaultAppUserFiltering("accountStatus.equals=" + DEFAULT_ACCOUNT_STATUS, "accountStatus.equals=" + UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    void getAllAppUsersByAccountStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where accountStatus in
        defaultAppUserFiltering(
            "accountStatus.in=" + DEFAULT_ACCOUNT_STATUS + "," + UPDATED_ACCOUNT_STATUS,
            "accountStatus.in=" + UPDATED_ACCOUNT_STATUS
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByAccountStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where accountStatus is not null
        defaultAppUserFiltering("accountStatus.specified=true", "accountStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByLastDateUpdateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastDateUpdate equals to
        defaultAppUserFiltering("lastDateUpdate.equals=" + DEFAULT_LAST_DATE_UPDATE, "lastDateUpdate.equals=" + UPDATED_LAST_DATE_UPDATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastDateUpdateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastDateUpdate in
        defaultAppUserFiltering(
            "lastDateUpdate.in=" + DEFAULT_LAST_DATE_UPDATE + "," + UPDATED_LAST_DATE_UPDATE,
            "lastDateUpdate.in=" + UPDATED_LAST_DATE_UPDATE
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByLastDateUpdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastDateUpdate is not null
        defaultAppUserFiltering("lastDateUpdate.specified=true", "lastDateUpdate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreated equals to
        defaultAppUserFiltering("dateCreated.equals=" + DEFAULT_DATE_CREATED, "dateCreated.equals=" + UPDATED_DATE_CREATED);
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreated in
        defaultAppUserFiltering(
            "dateCreated.in=" + DEFAULT_DATE_CREATED + "," + UPDATED_DATE_CREATED,
            "dateCreated.in=" + UPDATED_DATE_CREATED
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByDateCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where dateCreated is not null
        defaultAppUserFiltering("dateCreated.specified=true", "dateCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstnameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstname equals to
        defaultAppUserFiltering("firstname.equals=" + DEFAULT_FIRSTNAME, "firstname.equals=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstnameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstname in
        defaultAppUserFiltering("firstname.in=" + DEFAULT_FIRSTNAME + "," + UPDATED_FIRSTNAME, "firstname.in=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstname is not null
        defaultAppUserFiltering("firstname.specified=true", "firstname.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstnameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstname contains
        defaultAppUserFiltering("firstname.contains=" + DEFAULT_FIRSTNAME, "firstname.contains=" + UPDATED_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByFirstnameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where firstname does not contain
        defaultAppUserFiltering("firstname.doesNotContain=" + UPDATED_FIRSTNAME, "firstname.doesNotContain=" + DEFAULT_FIRSTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastnameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastname equals to
        defaultAppUserFiltering("lastname.equals=" + DEFAULT_LASTNAME, "lastname.equals=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastnameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastname in
        defaultAppUserFiltering("lastname.in=" + DEFAULT_LASTNAME + "," + UPDATED_LASTNAME, "lastname.in=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastname is not null
        defaultAppUserFiltering("lastname.specified=true", "lastname.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByLastnameContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastname contains
        defaultAppUserFiltering("lastname.contains=" + DEFAULT_LASTNAME, "lastname.contains=" + UPDATED_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByLastnameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where lastname does not contain
        defaultAppUserFiltering("lastname.doesNotContain=" + UPDATED_LASTNAME, "lastname.doesNotContain=" + DEFAULT_LASTNAME);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber equals to
        defaultAppUserFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber in
        defaultAppUserFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber is not null
        defaultAppUserFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber contains
        defaultAppUserFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where phoneNumber does not contain
        defaultAppUserFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate equals to
        defaultAppUserFiltering("birthDate.equals=" + DEFAULT_BIRTH_DATE, "birthDate.equals=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate in
        defaultAppUserFiltering("birthDate.in=" + DEFAULT_BIRTH_DATE + "," + UPDATED_BIRTH_DATE, "birthDate.in=" + UPDATED_BIRTH_DATE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthDate is not null
        defaultAppUserFiltering("birthDate.specified=true", "birthDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthPlaceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthPlace equals to
        defaultAppUserFiltering("birthPlace.equals=" + DEFAULT_BIRTH_PLACE, "birthPlace.equals=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthPlaceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthPlace in
        defaultAppUserFiltering("birthPlace.in=" + DEFAULT_BIRTH_PLACE + "," + UPDATED_BIRTH_PLACE, "birthPlace.in=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthPlaceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthPlace is not null
        defaultAppUserFiltering("birthPlace.specified=true", "birthPlace.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthPlaceContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthPlace contains
        defaultAppUserFiltering("birthPlace.contains=" + DEFAULT_BIRTH_PLACE, "birthPlace.contains=" + UPDATED_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllAppUsersByBirthPlaceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where birthPlace does not contain
        defaultAppUserFiltering("birthPlace.doesNotContain=" + UPDATED_BIRTH_PLACE, "birthPlace.doesNotContain=" + DEFAULT_BIRTH_PLACE);
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender equals to
        defaultAppUserFiltering("gender.equals=" + DEFAULT_GENDER, "gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender in
        defaultAppUserFiltering("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER, "gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    void getAllAppUsersByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where gender is not null
        defaultAppUserFiltering("gender.specified=true", "gender.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByFamilySituationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where familySituation equals to
        defaultAppUserFiltering("familySituation.equals=" + DEFAULT_FAMILY_SITUATION, "familySituation.equals=" + UPDATED_FAMILY_SITUATION);
    }

    @Test
    @Transactional
    void getAllAppUsersByFamilySituationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where familySituation in
        defaultAppUserFiltering(
            "familySituation.in=" + DEFAULT_FAMILY_SITUATION + "," + UPDATED_FAMILY_SITUATION,
            "familySituation.in=" + UPDATED_FAMILY_SITUATION
        );
    }

    @Test
    @Transactional
    void getAllAppUsersByFamilySituationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where familySituation is not null
        defaultAppUserFiltering("familySituation.specified=true", "familySituation.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where position equals to
        defaultAppUserFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllAppUsersByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where position in
        defaultAppUserFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllAppUsersByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where position is not null
        defaultAppUserFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByPositionContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where position contains
        defaultAppUserFiltering("position.contains=" + DEFAULT_POSITION, "position.contains=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllAppUsersByPositionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where position does not contain
        defaultAppUserFiltering("position.doesNotContain=" + UPDATED_POSITION, "position.doesNotContain=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllAppUsersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where address equals to
        defaultAppUserFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAppUsersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where address in
        defaultAppUserFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAppUsersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where address is not null
        defaultAppUserFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllAppUsersByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where address contains
        defaultAppUserFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAppUsersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        // Get all the appUserList where address does not contain
        defaultAppUserFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAppUsersByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            appUserRepository.saveAndFlush(appUser);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        appUser.setUser(user);
        appUserRepository.saveAndFlush(appUser);
        Long userId = user.getId();
        // Get all the appUserList where user equals to userId
        defaultAppUserShouldBeFound("userId.equals=" + userId);

        // Get all the appUserList where user equals to (userId + 1)
        defaultAppUserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultAppUserFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAppUserShouldBeFound(shouldBeFound);
        defaultAppUserShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAppUserShouldBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS)))
            .andExpect(jsonPath("$.[*].lastDateUpdate").value(hasItem(DEFAULT_LAST_DATE_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME)))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].birthPlace").value(hasItem(DEFAULT_BIRTH_PLACE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].familySituation").value(hasItem(DEFAULT_FAMILY_SITUATION.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));

        // Check, that the count call also returns 1
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAppUserShouldNotBeFound(String filter) throws Exception {
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAppUserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAppUser() throws Exception {
        // Get the appUser
        restAppUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser
        AppUser updatedAppUser = appUserRepository.findById(appUser.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAppUser are not directly saved in db
        em.detach(updatedAppUser);
        updatedAppUser
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .lastDateUpdate(UPDATED_LAST_DATE_UPDATE)
            .dateCreated(UPDATED_DATE_CREATED)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .gender(UPDATED_GENDER)
            .familySituation(UPDATED_FAMILY_SITUATION)
            .position(UPDATED_POSITION)
            .address(UPDATED_ADDRESS);

        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAppUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAppUserToMatchAllProperties(updatedAppUser);
    }

    @Test
    @Transactional
    void putNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL_ID, appUser.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUser)))
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .firstname(UPDATED_FIRSTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .position(UPDATED_POSITION)
            .address(UPDATED_ADDRESS);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAppUser, appUser), getPersistedAppUser(appUser));
    }

    @Test
    @Transactional
    void fullUpdateAppUserWithPatch() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the appUser using partial update
        AppUser partialUpdatedAppUser = new AppUser();
        partialUpdatedAppUser.setId(appUser.getId());

        partialUpdatedAppUser
            .accountStatus(UPDATED_ACCOUNT_STATUS)
            .lastDateUpdate(UPDATED_LAST_DATE_UPDATE)
            .dateCreated(UPDATED_DATE_CREATED)
            .firstname(UPDATED_FIRSTNAME)
            .lastname(UPDATED_LASTNAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .birthDate(UPDATED_BIRTH_DATE)
            .birthPlace(UPDATED_BIRTH_PLACE)
            .gender(UPDATED_GENDER)
            .familySituation(UPDATED_FAMILY_SITUATION)
            .position(UPDATED_POSITION)
            .address(UPDATED_ADDRESS);

        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAppUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAppUser))
            )
            .andExpect(status().isOk());

        // Validate the AppUser in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAppUserUpdatableFieldsEquals(partialUpdatedAppUser, getPersistedAppUser(partialUpdatedAppUser));
    }

    @Test
    @Transactional
    void patchNonExistingAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, appUser.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(appUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAppUser() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        appUser.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAppUserMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(appUser)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AppUser in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAppUser() throws Exception {
        // Initialize the database
        insertedAppUser = appUserRepository.saveAndFlush(appUser);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the appUser
        restAppUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, appUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return appUserRepository.count();
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

    protected AppUser getPersistedAppUser(AppUser appUser) {
        return appUserRepository.findById(appUser.getId()).orElseThrow();
    }

    protected void assertPersistedAppUserToMatchAllProperties(AppUser expectedAppUser) {
        assertAppUserAllPropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }

    protected void assertPersistedAppUserToMatchUpdatableProperties(AppUser expectedAppUser) {
        assertAppUserAllUpdatablePropertiesEquals(expectedAppUser, getPersistedAppUser(expectedAppUser));
    }
}
