package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PurchaseOrder.
 */
@Entity
@Table(name = "purchase_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name_of_the_ministry", nullable = false)
    private String nameOfTheMinistry;

    @NotNull
    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "total_amount_without_tax", precision = 21, scale = 2)
    private BigDecimal totalAmountWithoutTax;

    @Column(name = "tax_rate", precision = 21, scale = 2)
    private BigDecimal taxRate;

    @Column(name = "total_tax_amount", precision = 21, scale = 2)
    private BigDecimal totalTaxAmount;

    @Column(name = "prepaid_tax_amount", precision = 21, scale = 2)
    private BigDecimal prepaidTaxAmount;

    @Column(name = "total_amount_with_tax", precision = 21, scale = 2)
    private BigDecimal totalAmountWithTax;

    @Column(name = "auth_expenditure_number")
    private String authExpenditureNumber;

    @Column(name = "allocated_credits", precision = 21, scale = 2)
    private BigDecimal allocatedCredits;

    @Column(name = "committed_expenditures", precision = 21, scale = 2)
    private BigDecimal committedExpenditures;

    @Column(name = "available_balance", precision = 21, scale = 2)
    private BigDecimal availableBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialYear", "expense", "purchaseOrders", "decisions" }, allowSetters = true)
    private AnnexDecision annexDecision;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "purchaseOrders" }, allowSetters = true)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "decision", "mandate", "purchaseOrders" }, allowSetters = true)
    private Engagement engagement;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "purchaseOrder" }, allowSetters = true)
    private Set<PurchaseOrderItem> purchaseOrderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PurchaseOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameOfTheMinistry() {
        return this.nameOfTheMinistry;
    }

    public PurchaseOrder nameOfTheMinistry(String nameOfTheMinistry) {
        this.setNameOfTheMinistry(nameOfTheMinistry);
        return this;
    }

    public void setNameOfTheMinistry(String nameOfTheMinistry) {
        this.nameOfTheMinistry = nameOfTheMinistry;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public PurchaseOrder orderNumber(String orderNumber) {
        this.setOrderNumber(orderNumber);
        return this;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public PurchaseOrder orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmountWithoutTax() {
        return this.totalAmountWithoutTax;
    }

    public PurchaseOrder totalAmountWithoutTax(BigDecimal totalAmountWithoutTax) {
        this.setTotalAmountWithoutTax(totalAmountWithoutTax);
        return this;
    }

    public void setTotalAmountWithoutTax(BigDecimal totalAmountWithoutTax) {
        this.totalAmountWithoutTax = totalAmountWithoutTax;
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public PurchaseOrder taxRate(BigDecimal taxRate) {
        this.setTaxRate(taxRate);
        return this;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTotalTaxAmount() {
        return this.totalTaxAmount;
    }

    public PurchaseOrder totalTaxAmount(BigDecimal totalTaxAmount) {
        this.setTotalTaxAmount(totalTaxAmount);
        return this;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getPrepaidTaxAmount() {
        return this.prepaidTaxAmount;
    }

    public PurchaseOrder prepaidTaxAmount(BigDecimal prepaidTaxAmount) {
        this.setPrepaidTaxAmount(prepaidTaxAmount);
        return this;
    }

    public void setPrepaidTaxAmount(BigDecimal prepaidTaxAmount) {
        this.prepaidTaxAmount = prepaidTaxAmount;
    }

    public BigDecimal getTotalAmountWithTax() {
        return this.totalAmountWithTax;
    }

    public PurchaseOrder totalAmountWithTax(BigDecimal totalAmountWithTax) {
        this.setTotalAmountWithTax(totalAmountWithTax);
        return this;
    }

    public void setTotalAmountWithTax(BigDecimal totalAmountWithTax) {
        this.totalAmountWithTax = totalAmountWithTax;
    }

    public String getAuthExpenditureNumber() {
        return this.authExpenditureNumber;
    }

    public PurchaseOrder authExpenditureNumber(String authExpenditureNumber) {
        this.setAuthExpenditureNumber(authExpenditureNumber);
        return this;
    }

    public void setAuthExpenditureNumber(String authExpenditureNumber) {
        this.authExpenditureNumber = authExpenditureNumber;
    }

    public BigDecimal getAllocatedCredits() {
        return this.allocatedCredits;
    }

    public PurchaseOrder allocatedCredits(BigDecimal allocatedCredits) {
        this.setAllocatedCredits(allocatedCredits);
        return this;
    }

    public void setAllocatedCredits(BigDecimal allocatedCredits) {
        this.allocatedCredits = allocatedCredits;
    }

    public BigDecimal getCommittedExpenditures() {
        return this.committedExpenditures;
    }

    public PurchaseOrder committedExpenditures(BigDecimal committedExpenditures) {
        this.setCommittedExpenditures(committedExpenditures);
        return this;
    }

    public void setCommittedExpenditures(BigDecimal committedExpenditures) {
        this.committedExpenditures = committedExpenditures;
    }

    public BigDecimal getAvailableBalance() {
        return this.availableBalance;
    }

    public PurchaseOrder availableBalance(BigDecimal availableBalance) {
        this.setAvailableBalance(availableBalance);
        return this;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public AnnexDecision getAnnexDecision() {
        return this.annexDecision;
    }

    public void setAnnexDecision(AnnexDecision annexDecision) {
        this.annexDecision = annexDecision;
    }

    public PurchaseOrder annexDecision(AnnexDecision annexDecision) {
        this.setAnnexDecision(annexDecision);
        return this;
    }

    public Supplier getSupplier() {
        return this.supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public PurchaseOrder supplier(Supplier supplier) {
        this.setSupplier(supplier);
        return this;
    }

    public Engagement getEngagement() {
        return this.engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public PurchaseOrder engagement(Engagement engagement) {
        this.setEngagement(engagement);
        return this;
    }

    public Set<PurchaseOrderItem> getPurchaseOrderItems() {
        return this.purchaseOrderItems;
    }

    public void setPurchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        if (this.purchaseOrderItems != null) {
            this.purchaseOrderItems.forEach(i -> i.setPurchaseOrder(null));
        }
        if (purchaseOrderItems != null) {
            purchaseOrderItems.forEach(i -> i.setPurchaseOrder(this));
        }
        this.purchaseOrderItems = purchaseOrderItems;
    }

    public PurchaseOrder purchaseOrderItems(Set<PurchaseOrderItem> purchaseOrderItems) {
        this.setPurchaseOrderItems(purchaseOrderItems);
        return this;
    }

    public PurchaseOrder addPurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItems.add(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(this);
        return this;
    }

    public PurchaseOrder removePurchaseOrderItem(PurchaseOrderItem purchaseOrderItem) {
        this.purchaseOrderItems.remove(purchaseOrderItem);
        purchaseOrderItem.setPurchaseOrder(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PurchaseOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((PurchaseOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + getId() +
            ", nameOfTheMinistry='" + getNameOfTheMinistry() + "'" +
            ", orderNumber='" + getOrderNumber() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", totalAmountWithoutTax=" + getTotalAmountWithoutTax() +
            ", taxRate=" + getTaxRate() +
            ", totalTaxAmount=" + getTotalTaxAmount() +
            ", prepaidTaxAmount=" + getPrepaidTaxAmount() +
            ", totalAmountWithTax=" + getTotalAmountWithTax() +
            ", authExpenditureNumber='" + getAuthExpenditureNumber() + "'" +
            ", allocatedCredits=" + getAllocatedCredits() +
            ", committedExpenditures=" + getCommittedExpenditures() +
            ", availableBalance=" + getAvailableBalance() +
            "}";
    }
}
