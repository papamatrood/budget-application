package com.cratechnologie.budget.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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

    @JsonIgnoreProperties(value = { "financialYear", "expense", "purchaseOrders", "decisions" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "financialYear")
    private AnnexDecision annexDecision;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "financialYear")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "financialYear", "articles" }, allowSetters = true)
    private Set<Recipe> recipes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "financialYear")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annexDecision", "financialYear", "articles" }, allowSetters = true)
    private Set<Expense> expenses = new HashSet<>();

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

    public Set<Recipe> getRecipes() {
        return this.recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        if (this.recipes != null) {
            this.recipes.forEach(i -> i.setFinancialYear(null));
        }
        if (recipes != null) {
            recipes.forEach(i -> i.setFinancialYear(this));
        }
        this.recipes = recipes;
    }

    public FinancialYear recipes(Set<Recipe> recipes) {
        this.setRecipes(recipes);
        return this;
    }

    public FinancialYear addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        recipe.setFinancialYear(this);
        return this;
    }

    public FinancialYear removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.setFinancialYear(null);
        return this;
    }

    public Set<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        if (this.expenses != null) {
            this.expenses.forEach(i -> i.setFinancialYear(null));
        }
        if (expenses != null) {
            expenses.forEach(i -> i.setFinancialYear(this));
        }
        this.expenses = expenses;
    }

    public FinancialYear expenses(Set<Expense> expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public FinancialYear addExpense(Expense expense) {
        this.expenses.add(expense);
        expense.setFinancialYear(this);
        return this;
    }

    public FinancialYear removeExpense(Expense expense) {
        this.expenses.remove(expense);
        expense.setFinancialYear(null);
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
