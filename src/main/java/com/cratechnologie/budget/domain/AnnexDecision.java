package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AnnexDecision.
 */
@Entity
@Table(name = "annex_decision")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnnexDecision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "designation")
    private String designation;

    @Column(name = "expense_amount")
    private String expenseAmount;

    @Column(name = "credits_already_open")
    private String creditsAlreadyOpen;

    @Column(name = "credits_open")
    private String creditsOpen;

    @JsonIgnoreProperties(value = { "annexDecision", "recipes", "expenses" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private FinancialYear financialYear;

    @JsonIgnoreProperties(value = { "annexDecision", "financialYear", "articles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "annexDecision")
    private Expense expense;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "annexDecision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annexDecision", "supplier", "engagement", "purchaseOrderItems" }, allowSetters = true)
    private Set<PurchaseOrder> purchaseOrders = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "annexDecision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "engagement", "annexDecision", "decisionItems" }, allowSetters = true)
    private Set<Decision> decisions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnnexDecision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesignation() {
        return this.designation;
    }

    public AnnexDecision designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getExpenseAmount() {
        return this.expenseAmount;
    }

    public AnnexDecision expenseAmount(String expenseAmount) {
        this.setExpenseAmount(expenseAmount);
        return this;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getCreditsAlreadyOpen() {
        return this.creditsAlreadyOpen;
    }

    public AnnexDecision creditsAlreadyOpen(String creditsAlreadyOpen) {
        this.setCreditsAlreadyOpen(creditsAlreadyOpen);
        return this;
    }

    public void setCreditsAlreadyOpen(String creditsAlreadyOpen) {
        this.creditsAlreadyOpen = creditsAlreadyOpen;
    }

    public String getCreditsOpen() {
        return this.creditsOpen;
    }

    public AnnexDecision creditsOpen(String creditsOpen) {
        this.setCreditsOpen(creditsOpen);
        return this;
    }

    public void setCreditsOpen(String creditsOpen) {
        this.creditsOpen = creditsOpen;
    }

    public FinancialYear getFinancialYear() {
        return this.financialYear;
    }

    public void setFinancialYear(FinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public AnnexDecision financialYear(FinancialYear financialYear) {
        this.setFinancialYear(financialYear);
        return this;
    }

    public Expense getExpense() {
        return this.expense;
    }

    public void setExpense(Expense expense) {
        if (this.expense != null) {
            this.expense.setAnnexDecision(null);
        }
        if (expense != null) {
            expense.setAnnexDecision(this);
        }
        this.expense = expense;
    }

    public AnnexDecision expense(Expense expense) {
        this.setExpense(expense);
        return this;
    }

    public Set<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrders;
    }

    public void setPurchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        if (this.purchaseOrders != null) {
            this.purchaseOrders.forEach(i -> i.setAnnexDecision(null));
        }
        if (purchaseOrders != null) {
            purchaseOrders.forEach(i -> i.setAnnexDecision(this));
        }
        this.purchaseOrders = purchaseOrders;
    }

    public AnnexDecision purchaseOrders(Set<PurchaseOrder> purchaseOrders) {
        this.setPurchaseOrders(purchaseOrders);
        return this;
    }

    public AnnexDecision addPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.add(purchaseOrder);
        purchaseOrder.setAnnexDecision(this);
        return this;
    }

    public AnnexDecision removePurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrders.remove(purchaseOrder);
        purchaseOrder.setAnnexDecision(null);
        return this;
    }

    public Set<Decision> getDecisions() {
        return this.decisions;
    }

    public void setDecisions(Set<Decision> decisions) {
        if (this.decisions != null) {
            this.decisions.forEach(i -> i.setAnnexDecision(null));
        }
        if (decisions != null) {
            decisions.forEach(i -> i.setAnnexDecision(this));
        }
        this.decisions = decisions;
    }

    public AnnexDecision decisions(Set<Decision> decisions) {
        this.setDecisions(decisions);
        return this;
    }

    public AnnexDecision addDecision(Decision decision) {
        this.decisions.add(decision);
        decision.setAnnexDecision(this);
        return this;
    }

    public AnnexDecision removeDecision(Decision decision) {
        this.decisions.remove(decision);
        decision.setAnnexDecision(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnexDecision)) {
            return false;
        }
        return getId() != null && getId().equals(((AnnexDecision) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnnexDecision{" +
            "id=" + getId() +
            ", designation='" + getDesignation() + "'" +
            ", expenseAmount='" + getExpenseAmount() + "'" +
            ", creditsAlreadyOpen='" + getCreditsAlreadyOpen() + "'" +
            ", creditsOpen='" + getCreditsOpen() + "'" +
            "}";
    }
}
