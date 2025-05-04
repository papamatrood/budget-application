package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FinancialYear.
 */
@Entity
@Table(name = "financial_year")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialYear implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "the_year", nullable = false)
    private Integer theYear;

    @JsonIgnoreProperties(value = { "financialYear", "articles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "financialYear")
    private Recipe recipe;

    @JsonIgnoreProperties(value = { "financialYear", "annexDecision", "articles" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "financialYear")
    private Expense expense;

    @JsonIgnoreProperties(value = { "financialYear", "expense", "purchaseOrders", "decisions" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "financialYear")
    private AnnexDecision annexDecision;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FinancialYear id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTheYear() {
        return this.theYear;
    }

    public FinancialYear theYear(Integer theYear) {
        this.setTheYear(theYear);
        return this;
    }

    public void setTheYear(Integer theYear) {
        this.theYear = theYear;
    }

    public Recipe getRecipe() {
        return this.recipe;
    }

    public void setRecipe(Recipe recipe) {
        if (this.recipe != null) {
            this.recipe.setFinancialYear(null);
        }
        if (recipe != null) {
            recipe.setFinancialYear(this);
        }
        this.recipe = recipe;
    }

    public FinancialYear recipe(Recipe recipe) {
        this.setRecipe(recipe);
        return this;
    }

    public Expense getExpense() {
        return this.expense;
    }

    public void setExpense(Expense expense) {
        if (this.expense != null) {
            this.expense.setFinancialYear(null);
        }
        if (expense != null) {
            expense.setFinancialYear(this);
        }
        this.expense = expense;
    }

    public FinancialYear expense(Expense expense) {
        this.setExpense(expense);
        return this;
    }

    public AnnexDecision getAnnexDecision() {
        return this.annexDecision;
    }

    public void setAnnexDecision(AnnexDecision annexDecision) {
        if (this.annexDecision != null) {
            this.annexDecision.setFinancialYear(null);
        }
        if (annexDecision != null) {
            annexDecision.setFinancialYear(this);
        }
        this.annexDecision = annexDecision;
    }

    public FinancialYear annexDecision(AnnexDecision annexDecision) {
        this.setAnnexDecision(annexDecision);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FinancialYear)) {
            return false;
        }
        return getId() != null && getId().equals(((FinancialYear) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialYear{" +
            "id=" + getId() +
            ", theYear=" + getTheYear() +
            "}";
    }
}
