package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.AnnexDecision} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.AnnexDecisionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /annex-decisions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnexDecisionCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter designation;

    private StringFilter expenseAmount;

    private StringFilter creditsAlreadyOpen;

    private StringFilter creditsOpen;

    private LongFilter financialYearId;

    private LongFilter expenseId;

    private LongFilter purchaseOrderId;

    private LongFilter decisionId;

    private Boolean distinct;

    public AnnexDecisionCriteria() {}

    public AnnexDecisionCriteria(AnnexDecisionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(StringFilter::copy).orElse(null);
        this.expenseAmount = other.optionalExpenseAmount().map(StringFilter::copy).orElse(null);
        this.creditsAlreadyOpen = other.optionalCreditsAlreadyOpen().map(StringFilter::copy).orElse(null);
        this.creditsOpen = other.optionalCreditsOpen().map(StringFilter::copy).orElse(null);
        this.financialYearId = other.optionalFinancialYearId().map(LongFilter::copy).orElse(null);
        this.expenseId = other.optionalExpenseId().map(LongFilter::copy).orElse(null);
        this.purchaseOrderId = other.optionalPurchaseOrderId().map(LongFilter::copy).orElse(null);
        this.decisionId = other.optionalDecisionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AnnexDecisionCriteria copy() {
        return new AnnexDecisionCriteria(this);
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

    public StringFilter getDesignation() {
        return designation;
    }

    public Optional<StringFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public StringFilter designation() {
        if (designation == null) {
            setDesignation(new StringFilter());
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getExpenseAmount() {
        return expenseAmount;
    }

    public Optional<StringFilter> optionalExpenseAmount() {
        return Optional.ofNullable(expenseAmount);
    }

    public StringFilter expenseAmount() {
        if (expenseAmount == null) {
            setExpenseAmount(new StringFilter());
        }
        return expenseAmount;
    }

    public void setExpenseAmount(StringFilter expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public StringFilter getCreditsAlreadyOpen() {
        return creditsAlreadyOpen;
    }

    public Optional<StringFilter> optionalCreditsAlreadyOpen() {
        return Optional.ofNullable(creditsAlreadyOpen);
    }

    public StringFilter creditsAlreadyOpen() {
        if (creditsAlreadyOpen == null) {
            setCreditsAlreadyOpen(new StringFilter());
        }
        return creditsAlreadyOpen;
    }

    public void setCreditsAlreadyOpen(StringFilter creditsAlreadyOpen) {
        this.creditsAlreadyOpen = creditsAlreadyOpen;
    }

    public StringFilter getCreditsOpen() {
        return creditsOpen;
    }

    public Optional<StringFilter> optionalCreditsOpen() {
        return Optional.ofNullable(creditsOpen);
    }

    public StringFilter creditsOpen() {
        if (creditsOpen == null) {
            setCreditsOpen(new StringFilter());
        }
        return creditsOpen;
    }

    public void setCreditsOpen(StringFilter creditsOpen) {
        this.creditsOpen = creditsOpen;
    }

    public LongFilter getFinancialYearId() {
        return financialYearId;
    }

    public Optional<LongFilter> optionalFinancialYearId() {
        return Optional.ofNullable(financialYearId);
    }

    public LongFilter financialYearId() {
        if (financialYearId == null) {
            setFinancialYearId(new LongFilter());
        }
        return financialYearId;
    }

    public void setFinancialYearId(LongFilter financialYearId) {
        this.financialYearId = financialYearId;
    }

    public LongFilter getExpenseId() {
        return expenseId;
    }

    public Optional<LongFilter> optionalExpenseId() {
        return Optional.ofNullable(expenseId);
    }

    public LongFilter expenseId() {
        if (expenseId == null) {
            setExpenseId(new LongFilter());
        }
        return expenseId;
    }

    public void setExpenseId(LongFilter expenseId) {
        this.expenseId = expenseId;
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
        final AnnexDecisionCriteria that = (AnnexDecisionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(expenseAmount, that.expenseAmount) &&
            Objects.equals(creditsAlreadyOpen, that.creditsAlreadyOpen) &&
            Objects.equals(creditsOpen, that.creditsOpen) &&
            Objects.equals(financialYearId, that.financialYearId) &&
            Objects.equals(expenseId, that.expenseId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(decisionId, that.decisionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            designation,
            expenseAmount,
            creditsAlreadyOpen,
            creditsOpen,
            financialYearId,
            expenseId,
            purchaseOrderId,
            decisionId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnexDecisionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalExpenseAmount().map(f -> "expenseAmount=" + f + ", ").orElse("") +
            optionalCreditsAlreadyOpen().map(f -> "creditsAlreadyOpen=" + f + ", ").orElse("") +
            optionalCreditsOpen().map(f -> "creditsOpen=" + f + ", ").orElse("") +
            optionalFinancialYearId().map(f -> "financialYearId=" + f + ", ").orElse("") +
            optionalExpenseId().map(f -> "expenseId=" + f + ", ").orElse("") +
            optionalPurchaseOrderId().map(f -> "purchaseOrderId=" + f + ", ").orElse("") +
            optionalDecisionId().map(f -> "decisionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
