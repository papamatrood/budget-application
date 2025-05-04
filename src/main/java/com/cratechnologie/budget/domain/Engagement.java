package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Engagement.
 */
@Entity
@Table(name = "engagement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Engagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "engagement_number", nullable = false)
    private String engagementNumber;

    @NotNull
    @Column(name = "engagement_date", nullable = false)
    private Instant engagementDate;

    @Column(name = "object_of_expense")
    private String objectOfExpense;

    @Column(name = "notified_credits")
    private String notifiedCredits;

    @Column(name = "credit_committed")
    private String creditCommitted;

    @Column(name = "credits_available")
    private String creditsAvailable;

    @Column(name = "amount_proposed_commitment")
    private String amountProposedCommitment;

    @Column(name = "head_daf")
    private String headDaf;

    @Column(name = "financial_controller")
    private String financialController;

    @Column(name = "general_manager")
    private String generalManager;

    @JsonIgnoreProperties(value = { "engagement", "annexDecision", "decisionItems" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "engagement")
    private Decision decision;

    @JsonIgnoreProperties(value = { "engagement" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "engagement")
    private Mandate mandate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "engagement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annexDecision", "supplier", "engagement", "purchaseOrderItems" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Engagement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEngagementNumber() {
        return this.engagementNumber;
    }

    public Engagement engagementNumber(String engagementNumber) {
        this.setEngagementNumber(engagementNumber);
        return this;
    }

    public void setEngagementNumber(String engagementNumber) {
        this.engagementNumber = engagementNumber;
    }

    public Instant getEngagementDate() {
        return this.engagementDate;
    }

    public Engagement engagementDate(Instant engagementDate) {
        this.setEngagementDate(engagementDate);
        return this;
    }

    public void setEngagementDate(Instant engagementDate) {
        this.engagementDate = engagementDate;
    }

    public String getObjectOfExpense() {
        return this.objectOfExpense;
    }

    public Engagement objectOfExpense(String objectOfExpense) {
        this.setObjectOfExpense(objectOfExpense);
        return this;
    }

    public void setObjectOfExpense(String objectOfExpense) {
        this.objectOfExpense = objectOfExpense;
    }

    public String getNotifiedCredits() {
        return this.notifiedCredits;
    }

    public Engagement notifiedCredits(String notifiedCredits) {
        this.setNotifiedCredits(notifiedCredits);
        return this;
    }

    public void setNotifiedCredits(String notifiedCredits) {
        this.notifiedCredits = notifiedCredits;
    }

    public String getCreditCommitted() {
        return this.creditCommitted;
    }

    public Engagement creditCommitted(String creditCommitted) {
        this.setCreditCommitted(creditCommitted);
        return this;
    }

    public void setCreditCommitted(String creditCommitted) {
        this.creditCommitted = creditCommitted;
    }

    public String getCreditsAvailable() {
        return this.creditsAvailable;
    }

    public Engagement creditsAvailable(String creditsAvailable) {
        this.setCreditsAvailable(creditsAvailable);
        return this;
    }

    public void setCreditsAvailable(String creditsAvailable) {
        this.creditsAvailable = creditsAvailable;
    }

    public String getAmountProposedCommitment() {
        return this.amountProposedCommitment;
    }

    public Engagement amountProposedCommitment(String amountProposedCommitment) {
        this.setAmountProposedCommitment(amountProposedCommitment);
        return this;
    }

    public void setAmountProposedCommitment(String amountProposedCommitment) {
        this.amountProposedCommitment = amountProposedCommitment;
    }

    public String getHeadDaf() {
        return this.headDaf;
    }

    public Engagement headDaf(String headDaf) {
        this.setHeadDaf(headDaf);
        return this;
    }

    public void setHeadDaf(String headDaf) {
        this.headDaf = headDaf;
    }

    public String getFinancialController() {
        return this.financialController;
    }

    public Engagement financialController(String financialController) {
        this.setFinancialController(financialController);
        return this;
    }

    public void setFinancialController(String financialController) {
        this.financialController = financialController;
    }

    public String getGeneralManager() {
        return this.generalManager;
    }

    public Engagement generalManager(String generalManager) {
        this.setGeneralManager(generalManager);
        return this;
    }

    public void setGeneralManager(String generalManager) {
        this.generalManager = generalManager;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public void setDecision(Decision decision) {
        if (this.decision != null) {
            this.decision.setEngagement(null);
        }
        if (decision != null) {
            decision.setEngagement(this);
        }
        this.decision = decision;
    }

    public Engagement decision(Decision decision) {
        this.setDecision(decision);
        return this;
    }

    public Mandate getMandate() {
        return this.mandate;
    }

    public void setMandate(Mandate mandate) {
        if (this.mandate != null) {
            this.mandate.setEngagement(null);
        }
        if (mandate != null) {
            mandate.setEngagement(this);
        }
        this.mandate = mandate;
    }

    public Engagement mandate(Mandate mandate) {
        this.setMandate(mandate);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        if (this.purchaseOrders != null) {
            this.purchaseOrders.forEach(i -> i.setEngagement(null));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.setEngagement(this));
        }
        this.purchaseOrders = purchaseOrders;
    }

    public Engagement purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public Engagement addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.setEngagement(this);
        return this;
    }

    public Engagement removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.setEngagement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Engagement)) {
            return false;
        }
        return getId() != null && getId().equals(((Engagement) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Engagement{" +
            "id=" + getId() +
            ", engagementNumber='" + getEngagementNumber() + "'" +
            ", engagementDate='" + getEngagementDate() + "'" +
            ", objectOfExpense='" + getObjectOfExpense() + "'" +
            ", notifiedCredits='" + getNotifiedCredits() + "'" +
            ", creditCommitted='" + getCreditCommitted() + "'" +
            ", creditsAvailable='" + getCreditsAvailable() + "'" +
            ", amountProposedCommitment='" + getAmountProposedCommitment() + "'" +
            ", headDaf='" + getHeadDaf() + "'" +
            ", financialController='" + getFinancialController() + "'" +
            ", generalManager='" + getGeneralManager() + "'" +
            "}";
    }
}
