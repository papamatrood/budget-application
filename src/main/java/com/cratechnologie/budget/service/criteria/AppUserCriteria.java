package com.cratechnologie.budget.service.criteria;

import com.cratechnologie.budget.domain.enumeration.FamilySituationEnum;
import com.cratechnologie.budget.domain.enumeration.GenderEnum;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.AppUser} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.AppUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppUserCriteria implements Serializable, Criteria {

    /**
     * Class for filtering GenderEnum
     */
    public static class GenderEnumFilter extends Filter<GenderEnum> {

        public GenderEnumFilter() {}

        public GenderEnumFilter(GenderEnumFilter filter) {
            super(filter);
        }

        @Override
        public GenderEnumFilter copy() {
            return new GenderEnumFilter(this);
        }
    }

    /**
     * Class for filtering FamilySituationEnum
     */
    public static class FamilySituationEnumFilter extends Filter<FamilySituationEnum> {

        public FamilySituationEnumFilter() {}

        public FamilySituationEnumFilter(FamilySituationEnumFilter filter) {
            super(filter);
        }

        @Override
        public FamilySituationEnumFilter copy() {
            return new FamilySituationEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter accountStatus;

    private InstantFilter lastDateUpdate;

    private InstantFilter dateCreated;

    private StringFilter firstname;

    private StringFilter lastname;

    private StringFilter phoneNumber;

    private InstantFilter birthDate;

    private StringFilter birthPlace;

    private GenderEnumFilter gender;

    private FamilySituationEnumFilter familySituation;

    private StringFilter position;

    private StringFilter address;

    private LongFilter userId;

    private Boolean distinct;

    public AppUserCriteria() {}

    public AppUserCriteria(AppUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.accountStatus = other.optionalAccountStatus().map(BooleanFilter::copy).orElse(null);
        this.lastDateUpdate = other.optionalLastDateUpdate().map(InstantFilter::copy).orElse(null);
        this.dateCreated = other.optionalDateCreated().map(InstantFilter::copy).orElse(null);
        this.firstname = other.optionalFirstname().map(StringFilter::copy).orElse(null);
        this.lastname = other.optionalLastname().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.birthDate = other.optionalBirthDate().map(InstantFilter::copy).orElse(null);
        this.birthPlace = other.optionalBirthPlace().map(StringFilter::copy).orElse(null);
        this.gender = other.optionalGender().map(GenderEnumFilter::copy).orElse(null);
        this.familySituation = other.optionalFamilySituation().map(FamilySituationEnumFilter::copy).orElse(null);
        this.position = other.optionalPosition().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppUserCriteria copy() {
        return new AppUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getAccountStatus() {
        return accountStatus;
    }

    public Optional<BooleanFilter> optionalAccountStatus() {
        return Optional.ofNullable(accountStatus);
    }

    public BooleanFilter accountStatus() {
        if (accountStatus == null) {
            setAccountStatus(new BooleanFilter());
        }
        return accountStatus;
    }

    public void setAccountStatus(BooleanFilter accountStatus) {
        this.accountStatus = accountStatus;
    }

    public InstantFilter getLastDateUpdate() {
        return lastDateUpdate;
    }

    public Optional<InstantFilter> optionalLastDateUpdate() {
        return Optional.ofNullable(lastDateUpdate);
    }

    public InstantFilter lastDateUpdate() {
        if (lastDateUpdate == null) {
            setLastDateUpdate(new InstantFilter());
        }
        return lastDateUpdate;
    }

    public void setLastDateUpdate(InstantFilter lastDateUpdate) {
        this.lastDateUpdate = lastDateUpdate;
    }

    public InstantFilter getDateCreated() {
        return dateCreated;
    }

    public Optional<InstantFilter> optionalDateCreated() {
        return Optional.ofNullable(dateCreated);
    }

    public InstantFilter dateCreated() {
        if (dateCreated == null) {
            setDateCreated(new InstantFilter());
        }
        return dateCreated;
    }

    public void setDateCreated(InstantFilter dateCreated) {
        this.dateCreated = dateCreated;
    }

    public StringFilter getFirstname() {
        return firstname;
    }

    public Optional<StringFilter> optionalFirstname() {
        return Optional.ofNullable(firstname);
    }

    public StringFilter firstname() {
        if (firstname == null) {
            setFirstname(new StringFilter());
        }
        return firstname;
    }

    public void setFirstname(StringFilter firstname) {
        this.firstname = firstname;
    }

    public StringFilter getLastname() {
        return lastname;
    }

    public Optional<StringFilter> optionalLastname() {
        return Optional.ofNullable(lastname);
    }

    public StringFilter lastname() {
        if (lastname == null) {
            setLastname(new StringFilter());
        }
        return lastname;
    }

    public void setLastname(StringFilter lastname) {
        this.lastname = lastname;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public InstantFilter getBirthDate() {
        return birthDate;
    }

    public Optional<InstantFilter> optionalBirthDate() {
        return Optional.ofNullable(birthDate);
    }

    public InstantFilter birthDate() {
        if (birthDate == null) {
            setBirthDate(new InstantFilter());
        }
        return birthDate;
    }

    public void setBirthDate(InstantFilter birthDate) {
        this.birthDate = birthDate;
    }

    public StringFilter getBirthPlace() {
        return birthPlace;
    }

    public Optional<StringFilter> optionalBirthPlace() {
        return Optional.ofNullable(birthPlace);
    }

    public StringFilter birthPlace() {
        if (birthPlace == null) {
            setBirthPlace(new StringFilter());
        }
        return birthPlace;
    }

    public void setBirthPlace(StringFilter birthPlace) {
        this.birthPlace = birthPlace;
    }

    public GenderEnumFilter getGender() {
        return gender;
    }

    public Optional<GenderEnumFilter> optionalGender() {
        return Optional.ofNullable(gender);
    }

    public GenderEnumFilter gender() {
        if (gender == null) {
            setGender(new GenderEnumFilter());
        }
        return gender;
    }

    public void setGender(GenderEnumFilter gender) {
        this.gender = gender;
    }

    public FamilySituationEnumFilter getFamilySituation() {
        return familySituation;
    }

    public Optional<FamilySituationEnumFilter> optionalFamilySituation() {
        return Optional.ofNullable(familySituation);
    }

    public FamilySituationEnumFilter familySituation() {
        if (familySituation == null) {
            setFamilySituation(new FamilySituationEnumFilter());
        }
        return familySituation;
    }

    public void setFamilySituation(FamilySituationEnumFilter familySituation) {
        this.familySituation = familySituation;
    }

    public StringFilter getPosition() {
        return position;
    }

    public Optional<StringFilter> optionalPosition() {
        return Optional.ofNullable(position);
    }

    public StringFilter position() {
        if (position == null) {
            setPosition(new StringFilter());
        }
        return position;
    }

    public void setPosition(StringFilter position) {
        this.position = position;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppUserCriteria that = (AppUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(accountStatus, that.accountStatus) &&
            Objects.equals(lastDateUpdate, that.lastDateUpdate) &&
            Objects.equals(dateCreated, that.dateCreated) &&
            Objects.equals(firstname, that.firstname) &&
            Objects.equals(lastname, that.lastname) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(birthDate, that.birthDate) &&
            Objects.equals(birthPlace, that.birthPlace) &&
            Objects.equals(gender, that.gender) &&
            Objects.equals(familySituation, that.familySituation) &&
            Objects.equals(position, that.position) &&
            Objects.equals(address, that.address) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            accountStatus,
            lastDateUpdate,
            dateCreated,
            firstname,
            lastname,
            phoneNumber,
            birthDate,
            birthPlace,
            gender,
            familySituation,
            position,
            address,
            userId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAccountStatus().map(f -> "accountStatus=" + f + ", ").orElse("") +
            optionalLastDateUpdate().map(f -> "lastDateUpdate=" + f + ", ").orElse("") +
            optionalDateCreated().map(f -> "dateCreated=" + f + ", ").orElse("") +
            optionalFirstname().map(f -> "firstname=" + f + ", ").orElse("") +
            optionalLastname().map(f -> "lastname=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalBirthDate().map(f -> "birthDate=" + f + ", ").orElse("") +
            optionalBirthPlace().map(f -> "birthPlace=" + f + ", ").orElse("") +
            optionalGender().map(f -> "gender=" + f + ", ").orElse("") +
            optionalFamilySituation().map(f -> "familySituation=" + f + ", ").orElse("") +
            optionalPosition().map(f -> "position=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
