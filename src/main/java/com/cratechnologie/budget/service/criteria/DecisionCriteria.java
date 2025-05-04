package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Decision} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.DecisionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter decisionNumber;

    private InstantFilter decisionDate;

    private LongFilter engagementId;

    private LongFilter annexDecisionId;

    private LongFilter decisionItemId;

    private Boolean distinct;

    public DecisionCriteria() {}

    public DecisionCriteria(DecisionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.decisionNumber = other.optionalDecisionNumber().map(StringFilter::copy).orElse(null);
        this.decisionDate = other.optionalDecisionDate().map(InstantFilter::copy).orElse(null);
        this.engagementId = other.optionalEngagementId().map(LongFilter::copy).orElse(null);
        this.annexDecisionId = other.optionalAnnexDecisionId().map(LongFilter::copy).orElse(null);
        this.decisionItemId = other.optionalDecisionItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DecisionCriteria copy() {
        return new DecisionCriteria(this);
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

    public StringFilter getDecisionNumber() {
        return decisionNumber;
    }

    public Optional<StringFilter> optionalDecisionNumber() {
        return Optional.ofNullable(decisionNumber);
    }

    public StringFilter decisionNumber() {
        if (decisionNumber == null) {
            setDecisionNumber(new StringFilter());
        }
        return decisionNumber;
    }

    public void setDecisionNumber(StringFilter decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public InstantFilter getDecisionDate() {
        return decisionDate;
    }

    public Optional<InstantFilter> optionalDecisionDate() {
        return Optional.ofNullable(decisionDate);
    }

    public InstantFilter decisionDate() {
        if (decisionDate == null) {
            setDecisionDate(new InstantFilter());
        }
        return decisionDate;
    }

    public void setDecisionDate(InstantFilter decisionDate) {
        this.decisionDate = decisionDate;
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

    public LongFilter getAnnexDecisionId() {
        return annexDecisionId;
    }

    public Optional<LongFilter> optionalAnnexDecisionId() {
        return Optional.ofNullable(annexDecisionId);
    }

    public LongFilter annexDecisionId() {
        if (annexDecisionId == null) {
            setAnnexDecisionId(new LongFilter());
        }
        return annexDecisionId;
    }

    public void setAnnexDecisionId(LongFilter annexDecisionId) {
        this.annexDecisionId = annexDecisionId;
    }

    public LongFilter getDecisionItemId() {
        return decisionItemId;
    }

    public Optional<LongFilter> optionalDecisionItemId() {
        return Optional.ofNullable(decisionItemId);
    }

    public LongFilter decisionItemId() {
        if (decisionItemId == null) {
            setDecisionItemId(new LongFilter());
        }
        return decisionItemId;
    }

    public void setDecisionItemId(LongFilter decisionItemId) {
        this.decisionItemId = decisionItemId;
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
        final DecisionCriteria that = (DecisionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(decisionNumber, that.decisionNumber) &&
            Objects.equals(decisionDate, that.decisionDate) &&
            Objects.equals(engagementId, that.engagementId) &&
            Objects.equals(annexDecisionId, that.annexDecisionId) &&
            Objects.equals(decisionItemId, that.decisionItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, decisionNumber, decisionDate, engagementId, annexDecisionId, decisionItemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDecisionNumber().map(f -> "decisionNumber=" + f + ", ").orElse("") +
            optionalDecisionDate().map(f -> "decisionDate=" + f + ", ").orElse("") +
            optionalEngagementId().map(f -> "engagementId=" + f + ", ").orElse("") +
            optionalAnnexDecisionId().map(f -> "annexDecisionId=" + f + ", ").orElse("") +
            optionalDecisionItemId().map(f -> "decisionItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
