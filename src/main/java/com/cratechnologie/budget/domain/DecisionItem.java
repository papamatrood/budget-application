package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DecisionItem.
 */
@Entity
@Table(name = "decision_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DecisionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "beneficiary", nullable = false)
    private String beneficiary;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "observation", precision = 21, scale = 2)
    private BigDecimal observation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "engagement", "annexDecision", "decisionItems" }, allowSetters = true)
    private Decision decision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DecisionItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBeneficiary() {
        return this.beneficiary;
    }

    public DecisionItem beneficiary(String beneficiary) {
        this.setBeneficiary(beneficiary);
        return this;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public DecisionItem amount(Integer amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getObservation() {
        return this.observation;
    }

    public DecisionItem observation(BigDecimal observation) {
        this.setObservation(observation);
        return this;
    }

    public void setObservation(BigDecimal observation) {
        this.observation = observation;
    }

    public Decision getDecision() {
        return this.decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public DecisionItem decision(Decision decision) {
        this.setDecision(decision);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DecisionItem)) {
            return false;
        }
        return getId() != null && getId().equals(((DecisionItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DecisionItem{" +
            "id=" + getId() +
            ", beneficiary='" + getBeneficiary() + "'" +
            ", amount=" + getAmount() +
            ", observation=" + getObservation() +
            "}";
    }
}
