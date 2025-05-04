package com.cratechnologie.budget.domain;

import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "achievements_in_the_past_year")
    private Integer achievementsInThePastYear;

    @Column(name = "new_year_forecast")
    private Integer newYearForecast;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private FinancialCategoryEnum category;

    @JsonIgnoreProperties(value = { "recipe", "expense", "annexDecision" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private FinancialYear financialYear;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "recipes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "chapter", "recipes", "expenses" }, allowSetters = true)
    private Set<Article> articles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAchievementsInThePastYear() {
        return this.achievementsInThePastYear;
    }

    public Recipe achievementsInThePastYear(Integer achievementsInThePastYear) {
        this.setAchievementsInThePastYear(achievementsInThePastYear);
        return this;
    }

    public void setAchievementsInThePastYear(Integer achievementsInThePastYear) {
        this.achievementsInThePastYear = achievementsInThePastYear;
    }

    public Integer getNewYearForecast() {
        return this.newYearForecast;
    }

    public Recipe newYearForecast(Integer newYearForecast) {
        this.setNewYearForecast(newYearForecast);
        return this;
    }

    public void setNewYearForecast(Integer newYearForecast) {
        this.newYearForecast = newYearForecast;
    }

    public FinancialCategoryEnum getCategory() {
        return this.category;
    }

    public Recipe category(FinancialCategoryEnum category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(FinancialCategoryEnum category) {
        this.category = category;
    }

    public FinancialYear getFinancialYear() {
        return this.financialYear;
    }

    public void setFinancialYear(FinancialYear financialYear) {
        this.financialYear = financialYear;
    }

    public Recipe financialYear(FinancialYear financialYear) {
        this.setFinancialYear(financialYear);
        return this;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        if (this.articles != null) {
            this.articles.forEach(i -> i.removeRecipe(this));
        }
        if (articles != null) {
            articles.forEach(i -> i.addRecipe(this));
        }
        this.articles = articles;
    }

    public Recipe articles(Set<Article> articles) {
        this.setArticles(articles);
        return this;
    }

    public Recipe addArticle(Article article) {
        this.articles.add(article);
        article.getRecipes().add(this);
        return this;
    }

    public Recipe removeArticle(Article article) {
        this.articles.remove(article);
        article.getRecipes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipe)) {
            return false;
        }
        return getId() != null && getId().equals(((Recipe) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", achievementsInThePastYear=" + getAchievementsInThePastYear() +
            ", newYearForecast=" + getNewYearForecast() +
            ", category='" + getCategory() + "'" +
            "}";
    }
}
