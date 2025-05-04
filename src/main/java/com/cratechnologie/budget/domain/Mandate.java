package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Mandate.
 */
@Entity
@Table(name = "mandate")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Mandate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "mandate_number", nullable = false)
    private String mandateNumber;

    @NotNull
    @Column(name = "mandate_date", nullable = false)
    private Instant mandateDate;

    @Column(name = "issue_slip_number")
    private String issueSlipNumber;

    @Column(name = "month_and_year_of_issue")
    private String monthAndYearOfIssue;

    @Column(name = "supporting_documents")
    private String supportingDocuments;

    @JsonIgnoreProperties(value = { "decision", "mandate", "purchaseOrders" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Engagement engagement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Mandate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMandateNumber() {
        return this.mandateNumber;
    }

    public Mandate mandateNumber(String mandateNumber) {
        this.setMandateNumber(mandateNumber);
        return this;
    }

    public void setMandateNumber(String mandateNumber) {
        this.mandateNumber = mandateNumber;
    }

    public Instant getMandateDate() {
        return this.mandateDate;
    }

    public Mandate mandateDate(Instant mandateDate) {
        this.setMandateDate(mandateDate);
        return this;
    }

    public void setMandateDate(Instant mandateDate) {
        this.mandateDate = mandateDate;
    }

    public String getIssueSlipNumber() {
        return this.issueSlipNumber;
    }

    public Mandate issueSlipNumber(String issueSlipNumber) {
        this.setIssueSlipNumber(issueSlipNumber);
        return this;
    }

    public void setIssueSlipNumber(String issueSlipNumber) {
        this.issueSlipNumber = issueSlipNumber;
    }

    public String getMonthAndYearOfIssue() {
        return this.monthAndYearOfIssue;
    }

    public Mandate monthAndYearOfIssue(String monthAndYearOfIssue) {
        this.setMonthAndYearOfIssue(monthAndYearOfIssue);
        return this;
    }

    public void setMonthAndYearOfIssue(String monthAndYearOfIssue) {
        this.monthAndYearOfIssue = monthAndYearOfIssue;
    }

    public String getSupportingDocuments() {
        return this.supportingDocuments;
    }

    public Mandate supportingDocuments(String supportingDocuments) {
        this.setSupportingDocuments(supportingDocuments);
        return this;
    }

    public void setSupportingDocuments(String supportingDocuments) {
        this.supportingDocuments = supportingDocuments;
    }

    public Engagement getEngagement() {
        return this.engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

    public Mandate engagement(Engagement engagement) {
        this.setEngagement(engagement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mandate)) {
            return false;
        }
        return getId() != null && getId().equals(((Mandate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Mandate{" +
            "id=" + getId() +
            ", mandateNumber='" + getMandateNumber() + "'" +
            ", mandateDate='" + getMandateDate() + "'" +
            ", issueSlipNumber='" + getIssueSlipNumber() + "'" +
            ", monthAndYearOfIssue='" + getMonthAndYearOfIssue() + "'" +
            ", supportingDocuments='" + getSupportingDocuments() + "'" +
            "}";
    }
}
