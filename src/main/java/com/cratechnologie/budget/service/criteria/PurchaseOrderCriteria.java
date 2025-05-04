package com.cratechnologie.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.PurchaseOrder} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.PurchaseOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /purchase-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseOrderCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nameOfTheMinistry;

    private StringFilter orderNumber;

    private InstantFilter orderDate;

    private BigDecimalFilter totalAmountWithoutTax;

    private BigDecimalFilter taxRate;

    private BigDecimalFilter totalTaxAmount;

    private BigDecimalFilter prepaidTaxAmount;

    private BigDecimalFilter totalAmountWithTax;

    private StringFilter authExpenditureNumber;

    private BigDecimalFilter allocatedCredits;

    private BigDecimalFilter committedExpenditures;

    private BigDecimalFilter availableBalance;

    private LongFilter annexDecisionId;

    private LongFilter supplierId;

    private LongFilter engagementId;

    private LongFilter purchaseOrderItemId;

    private Boolean distinct;

    public PurchaseOrderCriteria() {}

    public PurchaseOrderCriteria(PurchaseOrderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nameOfTheMinistry = other.optionalNameOfTheMinistry().map(StringFilter::copy).orElse(null);
        this.orderNumber = other.optionalOrderNumber().map(StringFilter::copy).orElse(null);
        this.orderDate = other.optionalOrderDate().map(InstantFilter::copy).orElse(null);
        this.totalAmountWithoutTax = other.optionalTotalAmountWithoutTax().map(BigDecimalFilter::copy).orElse(null);
        this.taxRate = other.optionalTaxRate().map(BigDecimalFilter::copy).orElse(null);
        this.totalTaxAmount = other.optionalTotalTaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.prepaidTaxAmount = other.optionalPrepaidTaxAmount().map(BigDecimalFilter::copy).orElse(null);
        this.totalAmountWithTax = other.optionalTotalAmountWithTax().map(BigDecimalFilter::copy).orElse(null);
        this.authExpenditureNumber = other.optionalAuthExpenditureNumber().map(StringFilter::copy).orElse(null);
        this.allocatedCredits = other.optionalAllocatedCredits().map(BigDecimalFilter::copy).orElse(null);
        this.committedExpenditures = other.optionalCommittedExpenditures().map(BigDecimalFilter::copy).orElse(null);
        this.availableBalance = other.optionalAvailableBalance().map(BigDecimalFilter::copy).orElse(null);
        this.annexDecisionId = other.optionalAnnexDecisionId().map(LongFilter::copy).orElse(null);
        this.supplierId = other.optionalSupplierId().map(LongFilter::copy).orElse(null);
        this.engagementId = other.optionalEngagementId().map(LongFilter::copy).orElse(null);
        this.purchaseOrderItemId = other.optionalPurchaseOrderItemId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PurchaseOrderCriteria copy() {
        return new PurchaseOrderCriteria(this);
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

    public StringFilter getNameOfTheMinistry() {
        return nameOfTheMinistry;
    }

    public Optional<StringFilter> optionalNameOfTheMinistry() {
        return Optional.ofNullable(nameOfTheMinistry);
    }

    public StringFilter nameOfTheMinistry() {
        if (nameOfTheMinistry == null) {
            setNameOfTheMinistry(new StringFilter());
        }
        return nameOfTheMinistry;
    }

    public void setNameOfTheMinistry(StringFilter nameOfTheMinistry) {
        this.nameOfTheMinistry = nameOfTheMinistry;
    }

    public StringFilter getOrderNumber() {
        return orderNumber;
    }

    public Optional<StringFilter> optionalOrderNumber() {
        return Optional.ofNullable(orderNumber);
    }

    public StringFilter orderNumber() {
        if (orderNumber == null) {
            setOrderNumber(new StringFilter());
        }
        return orderNumber;
    }

    public void setOrderNumber(StringFilter orderNumber) {
        this.orderNumber = orderNumber;
    }

    public InstantFilter getOrderDate() {
        return orderDate;
    }

    public Optional<InstantFilter> optionalOrderDate() {
        return Optional.ofNullable(orderDate);
    }

    public InstantFilter orderDate() {
        if (orderDate == null) {
            setOrderDate(new InstantFilter());
        }
        return orderDate;
    }

    public void setOrderDate(InstantFilter orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimalFilter getTotalAmountWithoutTax() {
        return totalAmountWithoutTax;
    }

    public Optional<BigDecimalFilter> optionalTotalAmountWithoutTax() {
        return Optional.ofNullable(totalAmountWithoutTax);
    }

    public BigDecimalFilter totalAmountWithoutTax() {
        if (totalAmountWithoutTax == null) {
            setTotalAmountWithoutTax(new BigDecimalFilter());
        }
        return totalAmountWithoutTax;
    }

    public void setTotalAmountWithoutTax(BigDecimalFilter totalAmountWithoutTax) {
        this.totalAmountWithoutTax = totalAmountWithoutTax;
    }

    public BigDecimalFilter getTaxRate() {
        return taxRate;
    }

    public Optional<BigDecimalFilter> optionalTaxRate() {
        return Optional.ofNullable(taxRate);
    }

    public BigDecimalFilter taxRate() {
        if (taxRate == null) {
            setTaxRate(new BigDecimalFilter());
        }
        return taxRate;
    }

    public void setTaxRate(BigDecimalFilter taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimalFilter getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public Optional<BigDecimalFilter> optionalTotalTaxAmount() {
        return Optional.ofNullable(totalTaxAmount);
    }

    public BigDecimalFilter totalTaxAmount() {
        if (totalTaxAmount == null) {
            setTotalTaxAmount(new BigDecimalFilter());
        }
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimalFilter totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimalFilter getPrepaidTaxAmount() {
        return prepaidTaxAmount;
    }

    public Optional<BigDecimalFilter> optionalPrepaidTaxAmount() {
        return Optional.ofNullable(prepaidTaxAmount);
    }

    public BigDecimalFilter prepaidTaxAmount() {
        if (prepaidTaxAmount == null) {
            setPrepaidTaxAmount(new BigDecimalFilter());
        }
        return prepaidTaxAmount;
    }

    public void setPrepaidTaxAmount(BigDecimalFilter prepaidTaxAmount) {
        this.prepaidTaxAmount = prepaidTaxAmount;
    }

    public BigDecimalFilter getTotalAmountWithTax() {
        return totalAmountWithTax;
    }

    public Optional<BigDecimalFilter> optionalTotalAmountWithTax() {
        return Optional.ofNullable(totalAmountWithTax);
    }

    public BigDecimalFilter totalAmountWithTax() {
        if (totalAmountWithTax == null) {
            setTotalAmountWithTax(new BigDecimalFilter());
        }
        return totalAmountWithTax;
    }

    public void setTotalAmountWithTax(BigDecimalFilter totalAmountWithTax) {
        this.totalAmountWithTax = totalAmountWithTax;
    }

    public StringFilter getAuthExpenditureNumber() {
        return authExpenditureNumber;
    }

    public Optional<StringFilter> optionalAuthExpenditureNumber() {
        return Optional.ofNullable(authExpenditureNumber);
    }

    public StringFilter authExpenditureNumber() {
        if (authExpenditureNumber == null) {
            setAuthExpenditureNumber(new StringFilter());
        }
        return authExpenditureNumber;
    }

    public void setAuthExpenditureNumber(StringFilter authExpenditureNumber) {
        this.authExpenditureNumber = authExpenditureNumber;
    }

    public BigDecimalFilter getAllocatedCredits() {
        return allocatedCredits;
    }

    public Optional<BigDecimalFilter> optionalAllocatedCredits() {
        return Optional.ofNullable(allocatedCredits);
    }

    public BigDecimalFilter allocatedCredits() {
        if (allocatedCredits == null) {
            setAllocatedCredits(new BigDecimalFilter());
        }
        return allocatedCredits;
    }

    public void setAllocatedCredits(BigDecimalFilter allocatedCredits) {
        this.allocatedCredits = allocatedCredits;
    }

    public BigDecimalFilter getCommittedExpenditures() {
        return committedExpenditures;
    }

    public Optional<BigDecimalFilter> optionalCommittedExpenditures() {
        return Optional.ofNullable(committedExpenditures);
    }

    public BigDecimalFilter committedExpenditures() {
        if (committedExpenditures == null) {
            setCommittedExpenditures(new BigDecimalFilter());
        }
        return committedExpenditures;
    }

    public void setCommittedExpenditures(BigDecimalFilter committedExpenditures) {
        this.committedExpenditures = committedExpenditures;
    }

    public BigDecimalFilter getAvailableBalance() {
        return availableBalance;
    }

    public Optional<BigDecimalFilter> optionalAvailableBalance() {
        return Optional.ofNullable(availableBalance);
    }

    public BigDecimalFilter availableBalance() {
        if (availableBalance == null) {
            setAvailableBalance(new BigDecimalFilter());
        }
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimalFilter availableBalance) {
        this.availableBalance = availableBalance;
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

    public LongFilter getSupplierId() {
        return supplierId;
    }

    public Optional<LongFilter> optionalSupplierId() {
        return Optional.ofNullable(supplierId);
    }

    public LongFilter supplierId() {
        if (supplierId == null) {
            setSupplierId(new LongFilter());
        }
        return supplierId;
    }

    public void setSupplierId(LongFilter supplierId) {
        this.supplierId = supplierId;
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

    public LongFilter getPurchaseOrderItemId() {
        return purchaseOrderItemId;
    }

    public Optional<LongFilter> optionalPurchaseOrderItemId() {
        return Optional.ofNullable(purchaseOrderItemId);
    }

    public LongFilter purchaseOrderItemId() {
        if (purchaseOrderItemId == null) {
            setPurchaseOrderItemId(new LongFilter());
        }
        return purchaseOrderItemId;
    }

    public void setPurchaseOrderItemId(LongFilter purchaseOrderItemId) {
        this.purchaseOrderItemId = purchaseOrderItemId;
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
        final PurchaseOrderCriteria that = (PurchaseOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nameOfTheMinistry, that.nameOfTheMinistry) &&
            Objects.equals(orderNumber, that.orderNumber) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(totalAmountWithoutTax, that.totalAmountWithoutTax) &&
            Objects.equals(taxRate, that.taxRate) &&
            Objects.equals(totalTaxAmount, that.totalTaxAmount) &&
            Objects.equals(prepaidTaxAmount, that.prepaidTaxAmount) &&
            Objects.equals(totalAmountWithTax, that.totalAmountWithTax) &&
            Objects.equals(authExpenditureNumber, that.authExpenditureNumber) &&
            Objects.equals(allocatedCredits, that.allocatedCredits) &&
            Objects.equals(committedExpenditures, that.committedExpenditures) &&
            Objects.equals(availableBalance, that.availableBalance) &&
            Objects.equals(annexDecisionId, that.annexDecisionId) &&
            Objects.equals(supplierId, that.supplierId) &&
            Objects.equals(engagementId, that.engagementId) &&
            Objects.equals(purchaseOrderItemId, that.purchaseOrderItemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            nameOfTheMinistry,
            orderNumber,
            orderDate,
            totalAmountWithoutTax,
            taxRate,
            totalTaxAmount,
            prepaidTaxAmount,
            totalAmountWithTax,
            authExpenditureNumber,
            allocatedCredits,
            committedExpenditures,
            availableBalance,
            annexDecisionId,
            supplierId,
            engagementId,
            purchaseOrderItemId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNameOfTheMinistry().map(f -> "nameOfTheMinistry=" + f + ", ").orElse("") +
            optionalOrderNumber().map(f -> "orderNumber=" + f + ", ").orElse("") +
            optionalOrderDate().map(f -> "orderDate=" + f + ", ").orElse("") +
            optionalTotalAmountWithoutTax().map(f -> "totalAmountWithoutTax=" + f + ", ").orElse("") +
            optionalTaxRate().map(f -> "taxRate=" + f + ", ").orElse("") +
            optionalTotalTaxAmount().map(f -> "totalTaxAmount=" + f + ", ").orElse("") +
            optionalPrepaidTaxAmount().map(f -> "prepaidTaxAmount=" + f + ", ").orElse("") +
            optionalTotalAmountWithTax().map(f -> "totalAmountWithTax=" + f + ", ").orElse("") +
            optionalAuthExpenditureNumber().map(f -> "authExpenditureNumber=" + f + ", ").orElse("") +
            optionalAllocatedCredits().map(f -> "allocatedCredits=" + f + ", ").orElse("") +
            optionalCommittedExpenditures().map(f -> "committedExpenditures=" + f + ", ").orElse("") +
            optionalAvailableBalance().map(f -> "availableBalance=" + f + ", ").orElse("") +
            optionalAnnexDecisionId().map(f -> "annexDecisionId=" + f + ", ").orElse("") +
            optionalSupplierId().map(f -> "supplierId=" + f + ", ").orElse("") +
            optionalEngagementId().map(f -> "engagementId=" + f + ", ").orElse("") +
            optionalPurchaseOrderItemId().map(f -> "purchaseOrderItemId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
