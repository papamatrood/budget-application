package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.DecisionItem} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.DecisionItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /decision-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter beneficiary;

    private IntegerFilter amount;

    private BigDecimalFilter observation;

    private LongFilter decisionId;

    private Boolean distinct;

    public DecisionItemCriteria() {}

    public DecisionItemCriteria(DecisionItemCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.beneficiary = other.optionalBeneficiary().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(IntegerFilter::copy).orElse(null);
        this.observation = other.optionalObservation().map(BigDecimalFilter::copy).orElse(null);
        this.decisionId = other.optionalDecisionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DecisionItemCriteria copy() {
        return new DecisionItemCriteria(this);
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

    public StringFilter getBeneficiary() {
        return beneficiary;
    }

    public Optional<StringFilter> optionalBeneficiary() {
        return Optional.ofNullable(beneficiary);
    }

    public StringFilter beneficiary() {
        if (beneficiary == null) {
            setBeneficiary(new StringFilter());
        }
        return beneficiary;
    }

    public void setBeneficiary(StringFilter beneficiary) {
        this.beneficiary = beneficiary;
    }

    public IntegerFilter getAmount() {
        return amount;
    }

    public Optional<IntegerFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public IntegerFilter amount() {
        if (amount == null) {
            setAmount(new IntegerFilter());
        }
        return amount;
    }

    public void setAmount(IntegerFilter amount) {
        this.amount = amount;
    }

    public BigDecimalFilter getObservation() {
        return observation;
    }

    public Optional<BigDecimalFilter> optionalObservation() {
        return Optional.ofNullable(observation);
    }

    public BigDecimalFilter observation() {
        if (observation == null) {
            setObservation(new BigDecimalFilter());
        }
        return observation;
    }

    public void setObservation(BigDecimalFilter observation) {
        this.observation = observation;
    }

    public LongFilter getDecisionId() {
        return decisionId;
    }

    public Optional<LongFilter> optionalDecisionId() {
        return Optional.ofNullable(decisionId);
    }

    public LongFilter decisionId() {
        if (decisionId == null) {
            setDecisionId(new LongFilter());
        }
        return decisionId;
    }

    public void setDecisionId(LongFilter decisionId) {
        this.decisionId = decisionId;
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
        final DecisionItemCriteria that = (DecisionItemCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(beneficiary, that.beneficiary) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(observation, that.observation) &&
            Objects.equals(decisionId, that.decisionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, beneficiary, amount, observation, decisionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionItemCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBeneficiary().map(f -> "beneficiary=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalObservation().map(f -> "observation=" + f + ", ").orElse("") +
            optionalDecisionId().map(f -> "decisionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
