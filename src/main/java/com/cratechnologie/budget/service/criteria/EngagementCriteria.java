package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Engagement} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.EngagementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /engagements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EngagementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter engagementNumber;

    private InstantFilter engagementDate;

    private StringFilter objectOfExpense;

    private StringFilter notifiedCredits;

    private StringFilter creditCommitted;

    private StringFilter creditsAvailable;

    private StringFilter amountProposedCommitment;

    private StringFilter headDaf;

    private StringFilter financialController;

    private StringFilter generalManager;

    private LongFilter decisionId;

    private LongFilter mandateId;

    private LongFilter purchaseOrderId;

    private Boolean distinct;

    public EngagementCriteria() {}

    public EngagementCriteria(EngagementCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.engagementNumber = other.optionalEngagementNumber().map(StringFilter::copy).orElse(null);
        this.engagementDate = other.optionalEngagementDate().map(InstantFilter::copy).orElse(null);
        this.objectOfExpense = other.optionalObjectOfExpense().map(StringFilter::copy).orElse(null);
        this.notifiedCredits = other.optionalNotifiedCredits().map(StringFilter::copy).orElse(null);
        this.creditCommitted = other.optionalCreditCommitted().map(StringFilter::copy).orElse(null);
        this.creditsAvailable = other.optionalCreditsAvailable().map(StringFilter::copy).orElse(null);
        this.amountProposedCommitment = other.optionalAmountProposedCommitment().map(StringFilter::copy).orElse(null);
        this.headDaf = other.optionalHeadDaf().map(StringFilter::copy).orElse(null);
        this.financialController = other.optionalFinancialController().map(StringFilter::copy).orElse(null);
        this.generalManager = other.optionalGeneralManager().map(StringFilter::copy).orElse(null);
        this.decisionId = other.optionalDecisionId().map(LongFilter::copy).orElse(null);
        this.mandateId = other.optionalMandateId().map(LongFilter::copy).orElse(null);
        this.purchaseOrderId = other.optionalPurchaseOrderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public EngagementCriteria copy() {
        return new EngagementCriteria(this);
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

    public StringFilter getEngagementNumber() {
        return engagementNumber;
    }

    public Optional<StringFilter> optionalEngagementNumber() {
        return Optional.ofNullable(engagementNumber);
    }

    public StringFilter engagementNumber() {
        if (engagementNumber == null) {
            setEngagementNumber(new StringFilter());
        }
        return engagementNumber;
    }

    public void setEngagementNumber(StringFilter engagementNumber) {
        this.engagementNumber = engagementNumber;
    }

    public InstantFilter getEngagementDate() {
        return engagementDate;
    }

    public Optional<InstantFilter> optionalEngagementDate() {
        return Optional.ofNullable(engagementDate);
    }

    public InstantFilter engagementDate() {
        if (engagementDate == null) {
            setEngagementDate(new InstantFilter());
        }
        return engagementDate;
    }

    public void setEngagementDate(InstantFilter engagementDate) {
        this.engagementDate = engagementDate;
    }

    public StringFilter getObjectOfExpense() {
        return objectOfExpense;
    }

    public Optional<StringFilter> optionalObjectOfExpense() {
        return Optional.ofNullable(objectOfExpense);
    }

    public StringFilter objectOfExpense() {
        if (objectOfExpense == null) {
            setObjectOfExpense(new StringFilter());
        }
        return objectOfExpense;
    }

    public void setObjectOfExpense(StringFilter objectOfExpense) {
        this.objectOfExpense = objectOfExpense;
    }

    public StringFilter getNotifiedCredits() {
        return notifiedCredits;
    }

    public Optional<StringFilter> optionalNotifiedCredits() {
        return Optional.ofNullable(notifiedCredits);
    }

    public StringFilter notifiedCredits() {
        if (notifiedCredits == null) {
            setNotifiedCredits(new StringFilter());
        }
        return notifiedCredits;
    }

    public void setNotifiedCredits(StringFilter notifiedCredits) {
        this.notifiedCredits = notifiedCredits;
    }

    public StringFilter getCreditCommitted() {
        return creditCommitted;
    }

    public Optional<StringFilter> optionalCreditCommitted() {
        return Optional.ofNullable(creditCommitted);
    }

    public StringFilter creditCommitted() {
        if (creditCommitted == null) {
            setCreditCommitted(new StringFilter());
        }
        return creditCommitted;
    }

    public void setCreditCommitted(StringFilter creditCommitted) {
        this.creditCommitted = creditCommitted;
    }

    public StringFilter getCreditsAvailable() {
        return creditsAvailable;
    }

    public Optional<StringFilter> optionalCreditsAvailable() {
        return Optional.ofNullable(creditsAvailable);
    }

    public StringFilter creditsAvailable() {
        if (creditsAvailable == null) {
            setCreditsAvailable(new StringFilter());
        }
        return creditsAvailable;
    }

    public void setCreditsAvailable(StringFilter creditsAvailable) {
        this.creditsAvailable = creditsAvailable;
    }

    public StringFilter getAmountProposedCommitment() {
        return amountProposedCommitment;
    }

    public Optional<StringFilter> optionalAmountProposedCommitment() {
        return Optional.ofNullable(amountProposedCommitment);
    }

    public StringFilter amountProposedCommitment() {
        if (amountProposedCommitment == null) {
            setAmountProposedCommitment(new StringFilter());
        }
        return amountProposedCommitment;
    }

    public void setAmountProposedCommitment(StringFilter amountProposedCommitment) {
        this.amountProposedCommitment = amountProposedCommitment;
    }

    public StringFilter getHeadDaf() {
        return headDaf;
    }

    public Optional<StringFilter> optionalHeadDaf() {
        return Optional.ofNullable(headDaf);
    }

    public StringFilter headDaf() {
        if (headDaf == null) {
            setHeadDaf(new StringFilter());
        }
        return headDaf;
    }

    public void setHeadDaf(StringFilter headDaf) {
        this.headDaf = headDaf;
    }

    public StringFilter getFinancialController() {
        return financialController;
    }

    public Optional<StringFilter> optionalFinancialController() {
        return Optional.ofNullable(financialController);
    }

    public StringFilter financialController() {
        if (financialController == null) {
            setFinancialController(new StringFilter());
        }
        return financialController;
    }

    public void setFinancialController(StringFilter financialController) {
        this.financialController = financialController;
    }

    public StringFilter getGeneralManager() {
        return generalManager;
    }

    public Optional<StringFilter> optionalGeneralManager() {
        return Optional.ofNullable(generalManager);
    }

    public StringFilter generalManager() {
        if (generalManager == null) {
            setGeneralManager(new StringFilter());
        }
        return generalManager;
    }

    public void setGeneralManager(StringFilter generalManager) {
        this.generalManager = generalManager;
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

    public LongFilter getMandateId() {
        return mandateId;
    }

    public Optional<LongFilter> optionalMandateId() {
        return Optional.ofNullable(mandateId);
    }

    public LongFilter mandateId() {
        if (mandateId == null) {
            setMandateId(new LongFilter());
        }
        return mandateId;
    }

    public void setMandateId(LongFilter mandateId) {
        this.mandateId = mandateId;
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
        final EngagementCriteria that = (EngagementCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(engagementNumber, that.engagementNumber) &&
            Objects.equals(engagementDate, that.engagementDate) &&
            Objects.equals(objectOfExpense, that.objectOfExpense) &&
            Objects.equals(notifiedCredits, that.notifiedCredits) &&
            Objects.equals(creditCommitted, that.creditCommitted) &&
            Objects.equals(creditsAvailable, that.creditsAvailable) &&
            Objects.equals(amountProposedCommitment, that.amountProposedCommitment) &&
            Objects.equals(headDaf, that.headDaf) &&
            Objects.equals(financialController, that.financialController) &&
            Objects.equals(generalManager, that.generalManager) &&
            Objects.equals(decisionId, that.decisionId) &&
            Objects.equals(mandateId, that.mandateId) &&
            Objects.equals(purchaseOrderId, that.purchaseOrderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            engagementNumber,
            engagementDate,
            objectOfExpense,
            notifiedCredits,
            creditCommitted,
            creditsAvailable,
            amountProposedCommitment,
            headDaf,
            financialController,
            generalManager,
            decisionId,
            mandateId,
            purchaseOrderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EngagementCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEngagementNumber().map(f -> "engagementNumber=" + f + ", ").orElse("") +
            optionalEngagementDate().map(f -> "engagementDate=" + f + ", ").orElse("") +
            optionalObjectOfExpense().map(f -> "objectOfExpense=" + f + ", ").orElse("") +
            optionalNotifiedCredits().map(f -> "notifiedCredits=" + f + ", ").orElse("") +
            optionalCreditCommitted().map(f -> "creditCommitted=" + f + ", ").orElse("") +
            optionalCreditsAvailable().map(f -> "creditsAvailable=" + f + ", ").orElse("") +
            optionalAmountProposedCommitment().map(f -> "amountProposedCommitment=" + f + ", ").orElse("") +
            optionalHeadDaf().map(f -> "headDaf=" + f + ", ").orElse("") +
            optionalFinancialController().map(f -> "financialController=" + f + ", ").orElse("") +
            optionalGeneralManager().map(f -> "generalManager=" + f + ", ").orElse("") +
            optionalDecisionId().map(f -> "decisionId=" + f + ", ").orElse("") +
            optionalMandateId().map(f -> "mandateId=" + f + ", ").orElse("") +
            optionalPurchaseOrderId().map(f -> "purchaseOrderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
