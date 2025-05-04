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
 * A Decision.
 */
@Entity
@Table(name = "decision")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Decision implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "decision_number", nullable = false)
    private String decisionNumber;

    @NotNull
    @Column(name = "decision_date", nullable = false)
    private Instant decisionDate;

    @JsonIgnoreProperties(value = { "decision", "mandate", "purchaseOrders" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Engagement engagement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "financialYear", "expense", "purchaseOrders", "decisions" }, allowSetters = true)
    private AnnexDecision annexDecision;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "decision")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "decision" }, allowSetters = true)
    private Set<DecisionItem> decisionItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Decision id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDecisionNumber() {
        return this.decisionNumber;
    }

    public Decision decisionNumber(String decisionNumber) {
        this.setDecisionNumber(decisionNumber);
        return this;
    }

    public void setDecisionNumber(String decisionNumber) {
        this.decisionNumber = decisionNumber;
    }

    public Instant getDecisionDate() {
        return this.decisionDate;
    }

    public Decision decisionDate(Instant decisionDate) {
        this.setDecisionDate(decisionDate);
        return this;
    }

    public void setDecisionDate(Instant decisionDate) {
        this.decisionDate = decisionDate;
    }

    public Engagement getEngagement() {
        return this.engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Decision engagement(Engagement engagement) {
        this.setEngagement(engagement);
        return this;
    }

    public AnnexDecision getAnnexDecision() {
        return this.annexDecision;
    }

    public void setAnnexDecision(AnnexDecision annexDecision) {
        this.annexDecision = annexDecision;
    }

    public Decision annexDecision(AnnexDecision annexDecision) {
        this.setAnnexDecision(annexDecision);
        return this;
    }

    public Set<DecisionItem> getDecisionItems() {
        return this.decisionItems;
    }

    public void setDecisionItems(Set<DecisionItem> decisionItems) {
        if (this.decisionItems != null) {
            this.decisionItems.forEach(i -> i.setDecision(null));
        }
        if (decisionItems != null) {
            decisionItems.forEach(i -> i.setDecision(this));
        }
        this.decisionItems = decisionItems;
    }

    public Decision decisionItems(Set<DecisionItem> decisionItems) {
        this.setDecisionItems(decisionItems);
        return this;
    }

    public Decision addDecisionItem(DecisionItem decisionItem) {
        this.decisionItems.add(decisionItem);
        decisionItem.setDecision(this);
        return this;
    }

    public Decision removeDecisionItem(DecisionItem decisionItem) {
        this.decisionItems.remove(decisionItem);
        decisionItem.setDecision(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Decision)) {
            return false;
        }
        return getId() != null && getId().equals(((Decision) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Decision{" +
            "id=" + getId() +
            ", decisionNumber='" + getDecisionNumber() + "'" +
            ", decisionDate='" + getDecisionDate() + "'" +
            "}";
    }
}
