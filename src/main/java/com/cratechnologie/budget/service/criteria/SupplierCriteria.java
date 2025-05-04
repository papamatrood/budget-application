package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Supplier} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.SupplierResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /suppliers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SupplierCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter companyName;

    private StringFilter address;

    private StringFilter phone;

    private StringFilter nifNumber;

    private StringFilter commercialRegister;

    private StringFilter bankAccount;

    private StringFilter mandatingEstablishment;

    private StringFilter email;

    private StringFilter website;

    private StringFilter description;

    private StringFilter contactFirstname;

    private StringFilter contactlastname;

    private LongFilter purchaseOrderId;

    private Boolean distinct;

    public SupplierCriteria() {}

    public SupplierCriteria(SupplierCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.companyName = other.optionalCompanyName().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.phone = other.optionalPhone().map(StringFilter::copy).orElse(null);
        this.nifNumber = other.optionalNifNumber().map(StringFilter::copy).orElse(null);
        this.commercialRegister = other.optionalCommercialRegister().map(StringFilter::copy).orElse(null);
        this.bankAccount = other.optionalBankAccount().map(StringFilter::copy).orElse(null);
        this.mandatingEstablishment = other.optionalMandatingEstablishment().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.website = other.optionalWebsite().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.contactFirstname = other.optionalContactFirstname().map(StringFilter::copy).orElse(null);
        this.contactlastname = other.optionalContactlastname().map(StringFilter::copy).orElse(null);
        this.purchaseOrderId = other.optionalPurchaseOrderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SupplierCriteria copy() {
        return new SupplierCriteria(this);
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

    public StringFilter getCompanyName() {
        return companyName;
    }

    public Optional<StringFilter> optionalCompanyName() {
        return Optional.ofNullable(companyName);
    }

    public StringFilter companyName() {
        if (companyName == null) {
            setCompanyName(new StringFilter());
        }
        return companyName;
    }

    public void setCompanyName(StringFilter companyName) {
        this.companyName = companyName;
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

    public StringFilter getPhone() {
        return phone;
    }

    public Optional<StringFilter> optionalPhone() {
        return Optional.ofNullable(phone);
    }

    public StringFilter phone() {
        if (phone == null) {
            setPhone(new StringFilter());
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getNifNumber() {
        return nifNumber;
    }

    public Optional<StringFilter> optionalNifNumber() {
        return Optional.ofNullable(nifNumber);
    }

    public StringFilter nifNumber() {
        if (nifNumber == null) {
            setNifNumber(new StringFilter());
        }
        return nifNumber;
    }

    public void setNifNumber(StringFilter nifNumber) {
        this.nifNumber = nifNumber;
    }

    public StringFilter getCommercialRegister() {
        return commercialRegister;
    }

    public Optional<StringFilter> optionalCommercialRegister() {
        return Optional.ofNullable(commercialRegister);
    }

    public StringFilter commercialRegister() {
        if (commercialRegister == null) {
            setCommercialRegister(new StringFilter());
        }
        return commercialRegister;
    }

    public void setCommercialRegister(StringFilter commercialRegister) {
        this.commercialRegister = commercialRegister;
    }

    public StringFilter getBankAccount() {
        return bankAccount;
    }

    public Optional<StringFilter> optionalBankAccount() {
        return Optional.ofNullable(bankAccount);
    }

    public StringFilter bankAccount() {
        if (bankAccount == null) {
            setBankAccount(new StringFilter());
        }
        return bankAccount;
    }

    public void setBankAccount(StringFilter bankAccount) {
        this.bankAccount = bankAccount;
    }

    public StringFilter getMandatingEstablishment() {
        return mandatingEstablishment;
    }

    public Optional<StringFilter> optionalMandatingEstablishment() {
        return Optional.ofNullable(mandatingEstablishment);
    }

    public StringFilter mandatingEstablishment() {
        if (mandatingEstablishment == null) {
            setMandatingEstablishment(new StringFilter());
        }
        return mandatingEstablishment;
    }

    public void setMandatingEstablishment(StringFilter mandatingEstablishment) {
        this.mandatingEstablishment = mandatingEstablishment;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public Optional<StringFilter> optionalWebsite() {
        return Optional.ofNullable(website);
    }

    public StringFilter website() {
        if (website == null) {
            setWebsite(new StringFilter());
        }
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getContactFirstname() {
        return contactFirstname;
    }

    public Optional<StringFilter> optionalContactFirstname() {
        return Optional.ofNullable(contactFirstname);
    }

    public StringFilter contactFirstname() {
        if (contactFirstname == null) {
            setContactFirstname(new StringFilter());
        }
        return contactFirstname;
    }

    public void setContactFirstname(StringFilter contactFirstname) {
        this.contactFirstname = contactFirstname;
    }

    public StringFilter getContactlastname() {
        return contactlastname;
    }

    public Optional<StringFilter> optionalContactlastname() {
        return Optional.ofNullable(contactlastname);
    }

    public StringFilter contactlastname() {
        if (contactlastname == null) {
            setContactlastname(new StringFilter());
        }
        return contactlastname;
    }

    public void setContactlastname(StringFilter contactlastname) {
        this.contactlastname = contactlastname;
    }

    public LongFilter getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public Optional<LongFilter> optionalPurchaseOrderId() {
        return Optional.ofNullable(purchaseOrderId);
    }

    public LongFilter purchaseOrderId() {
        if (purchaseOrderId == null) {
            setPurchaseOrderId(new LongFilter());
        }
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(LongFilter purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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
        final SupplierCriteria that = (SupplierCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(nifNumber, that.nifNumber) &&
            Objects.equals(commercialRegister, that.commercialRegister) &&
            Objects.equals(bankAccount, that.bankAccount) &&
            Objects.equals(mandatingEstablishment, that.mandatingEstablishment) &&
            Objects.equals(email, that.email) &&
            Objects.equals(website, that.website) &&
            Objects.equals(description, that.description) &&
            Objects.equals(contactFirstname, that.contactFirstname) &&
            Objects.equals(contactlastname, that.contactlastname) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            companyName,
            address,
            phone,
            nifNumber,
            commercialRegister,
            bankAccount,
            mandatingEstablishment,
            email,
            website,
            description,
            contactFirstname,
            contactlastname,
            purchaseOrderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SupplierCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCompanyName().map(f -> "companyName=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalPhone().map(f -> "phone=" + f + ", ").orElse("") +
            optionalNifNumber().map(f -> "nifNumber=" + f + ", ").orElse("") +
            optionalCommercialRegister().map(f -> "commercialRegister=" + f + ", ").orElse("") +
            optionalBankAccount().map(f -> "bankAccount=" + f + ", ").orElse("") +
            optionalMandatingEstablishment().map(f -> "mandatingEstablishment=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalWebsite().map(f -> "website=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalContactFirstname().map(f -> "contactFirstname=" + f + ", ").orElse("") +
            optionalContactlastname().map(f -> "contactlastname=" + f + ", ").orElse("") +
            optionalPurchaseOrderId().map(f -> "purchaseOrderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
