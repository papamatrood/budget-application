package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Mandate} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.MandateResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mandates?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MandateCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter mandateNumber;

    private InstantFilter mandateDate;

    private StringFilter issueSlipNumber;

    private StringFilter monthAndYearOfIssue;

    private StringFilter supportingDocuments;

    private LongFilter engagementId;

    private Boolean distinct;

    public MandateCriteria() {}

    public MandateCriteria(MandateCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.mandateNumber = other.optionalMandateNumber().map(StringFilter::copy).orElse(null);
        this.mandateDate = other.optionalMandateDate().map(InstantFilter::copy).orElse(null);
        this.issueSlipNumber = other.optionalIssueSlipNumber().map(StringFilter::copy).orElse(null);
        this.monthAndYearOfIssue = other.optionalMonthAndYearOfIssue().map(StringFilter::copy).orElse(null);
        this.supportingDocuments = other.optionalSupportingDocuments().map(StringFilter::copy).orElse(null);
        this.engagementId = other.optionalEngagementId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MandateCriteria copy() {
        return new MandateCriteria(this);
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

    public StringFilter getMandateNumber() {
        return mandateNumber;
    }

    public Optional<StringFilter> optionalMandateNumber() {
        return Optional.ofNullable(mandateNumber);
    }

    public StringFilter mandateNumber() {
        if (mandateNumber == null) {
            setMandateNumber(new StringFilter());
        }
        return mandateNumber;
    }

    public void setMandateNumber(StringFilter mandateNumber) {
        this.mandateNumber = mandateNumber;
    }

    public InstantFilter getMandateDate() {
        return mandateDate;
    }

    public Optional<InstantFilter> optionalMandateDate() {
        return Optional.ofNullable(mandateDate);
    }

    public InstantFilter mandateDate() {
        if (mandateDate == null) {
            setMandateDate(new InstantFilter());
        }
        return mandateDate;
    }

    public void setMandateDate(InstantFilter mandateDate) {
        this.mandateDate = mandateDate;
    }

    public StringFilter getIssueSlipNumber() {
        return issueSlipNumber;
    }

    public Optional<StringFilter> optionalIssueSlipNumber() {
        return Optional.ofNullable(issueSlipNumber);
    }

    public StringFilter issueSlipNumber() {
        if (issueSlipNumber == null) {
            setIssueSlipNumber(new StringFilter());
        }
        return issueSlipNumber;
    }

    public void setIssueSlipNumber(StringFilter issueSlipNumber) {
        this.issueSlipNumber = issueSlipNumber;
    }

    public StringFilter getMonthAndYearOfIssue() {
        return monthAndYearOfIssue;
    }

    public Optional<StringFilter> optionalMonthAndYearOfIssue() {
        return Optional.ofNullable(monthAndYearOfIssue);
    }

    public StringFilter monthAndYearOfIssue() {
        if (monthAndYearOfIssue == null) {
            setMonthAndYearOfIssue(new StringFilter());
        }
        return monthAndYearOfIssue;
    }

    public void setMonthAndYearOfIssue(StringFilter monthAndYearOfIssue) {
        this.monthAndYearOfIssue = monthAndYearOfIssue;
    }

    public StringFilter getSupportingDocuments() {
        return supportingDocuments;
    }

    public Optional<StringFilter> optionalSupportingDocuments() {
        return Optional.ofNullable(supportingDocuments);
    }

    public StringFilter supportingDocuments() {
        if (supportingDocuments == null) {
            setSupportingDocuments(new StringFilter());
        }
        return supportingDocuments;
    }

    public void setSupportingDocuments(StringFilter supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public LongFilter getEngagementId() {
        return engagementId;
    }

    public Optional<LongFilter> optionalEngagementId() {
        return Optional.ofNullable(engagementId);
    }

    public LongFilter engagementId() {
        if (engagementId == null) {
            setEngagementId(new LongFilter());
        }
        return engagementId;
    }

    public void setEngagementId(LongFilter engagementId) {
        this.engagementId = engagementId;
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
        final MandateCriteria that = (MandateCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(mandateNumber, that.mandateNumber) &&
            Objects.equals(mandateDate, that.mandateDate) &&
            Objects.equals(issueSlipNumber, that.issueSlipNumber) &&
            Objects.equals(monthAndYearOfIssue, that.monthAndYearOfIssue) &&
            Objects.equals(supportingDocuments, that.supportingDocuments) &&
            Objects.equals(engagementId, that.engagementId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            mandateNumber,
            mandateDate,
            issueSlipNumber,
            monthAndYearOfIssue,
            supportingDocuments,
            engagementId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MandateCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalMandateNumber().map(f -> "mandateNumber=" + f + ", ").orElse("") +
            optionalMandateDate().map(f -> "mandateDate=" + f + ", ").orElse("") +
            optionalIssueSlipNumber().map(f -> "issueSlipNumber=" + f + ", ").orElse("") +
            optionalMonthAndYearOfIssue().map(f -> "monthAndYearOfIssue=" + f + ", ").orElse("") +
            optionalSupportingDocuments().map(f -> "supportingDocuments=" + f + ", ").orElse("") +
            optionalEngagementId().map(f -> "engagementId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
