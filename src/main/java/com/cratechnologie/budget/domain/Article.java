package com.cratechnologie.budget.domain;

import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Article.
 */
@Entity
@Table(name = "article")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private FinancialCategoryEnum category;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "account_div")
    private String accountDiv;

    @Column(name = "code_end")
    private String codeEnd;

    @Column(name = "paragraph")
    private String paragraph;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subTitle", "articles" }, allowSetters = true)
    private Chapter chapter;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_article__recipe",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "financialYear", "articles" }, allowSetters = true)
    private Set<Recipe> recipes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_article__expense",
        joinColumns = @JoinColumn(name = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "expense_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "financialYear", "annexDecision", "articles" }, allowSetters = true)
    private Set<Expense> expenses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Article id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FinancialCategoryEnum getCategory() {
        return this.category;
    }

    public Article category(FinancialCategoryEnum category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(FinancialCategoryEnum category) {
        this.category = category;
    }

    public String getCode() {
        return this.code;
    }

    public Article code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Article designation(String designation) {
        this.setDesignation(designation);
        return this;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getAccountDiv() {
        return this.accountDiv;
    }

    public Article accountDiv(String accountDiv) {
        this.setAccountDiv(accountDiv);
        return this;
    }

    public void setAccountDiv(String accountDiv) {
        this.accountDiv = accountDiv;
    }

    public String getCodeEnd() {
        return this.codeEnd;
    }

    public Article codeEnd(String codeEnd) {
        this.setCodeEnd(codeEnd);
        return this;
    }

    public void setCodeEnd(String codeEnd) {
        this.codeEnd = codeEnd;
    }

    public String getParagraph() {
        return this.paragraph;
    }

    public Article paragraph(String paragraph) {
        this.setParagraph(paragraph);
        return this;
    }

    public void setParagraph(String paragraph) {
        this.paragraph = paragraph;
    }

    public Chapter getChapter() {
        return this.chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Article chapter(Chapter chapter) {
        this.setChapter(chapter);
        return this;
    }

    public Set<Recipe> getRecipes() {
        return this.recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Article recipes(Set<Recipe> recipes) {
        this.setRecipes(recipes);
        return this;
    }

    public Article addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
        return this;
    }

    public Article removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
        return this;
    }

    public Set<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(Set<Expense> expenses) {
        this.expenses = expenses;
    }

    public Article expenses(Set<Expense> expenses) {
        this.setExpenses(expenses);
        return this;
    }

    public Article addExpense(Expense expense) {
        this.expenses.add(expense);
        return this;
    }

    public Article removeExpense(Expense expense) {
        this.expenses.remove(expense);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        return getId() != null && getId().equals(((Article) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Article{" +
            "id=" + getId() +
            ", category='" + getCategory() + "'" +
            ", code='" + getCode() + "'" +
            ", designation='" + getDesignation() + "'" +
            ", accountDiv='" + getAccountDiv() + "'" +
            ", codeEnd='" + getCodeEnd() + "'" +
            ", paragraph='" + getParagraph() + "'" +
            "}";
    }
}
