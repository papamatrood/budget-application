package com.cratechnologie.budget.service.criteria;

import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Recipe} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.RecipeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /recipes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RecipeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering FinancialCategoryEnum
     */
    public static class FinancialCategoryEnumFilter extends Filter<FinancialCategoryEnum> {

        public FinancialCategoryEnumFilter() {}

        public FinancialCategoryEnumFilter(FinancialCategoryEnumFilter filter) {
            super(filter);
        }

        @Override
        public FinancialCategoryEnumFilter copy() {
            return new FinancialCategoryEnumFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter achievementsInThePastYear;

    private IntegerFilter newYearForecast;

    private FinancialCategoryEnumFilter category;

    private LongFilter financialYearId;

    private LongFilter articleId;

    private Boolean distinct;

    public RecipeCriteria() {}

    public RecipeCriteria(RecipeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.achievementsInThePastYear = other.optionalAchievementsInThePastYear().map(IntegerFilter::copy).orElse(null);
        this.newYearForecast = other.optionalNewYearForecast().map(IntegerFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(FinancialCategoryEnumFilter::copy).orElse(null);
        this.financialYearId = other.optionalFinancialYearId().map(LongFilter::copy).orElse(null);
        this.articleId = other.optionalArticleId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RecipeCriteria copy() {
        return new RecipeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getAchievementsInThePastYear() {
        return achievementsInThePastYear;
    }

    public Optional<IntegerFilter> optionalAchievementsInThePastYear() {
        return Optional.ofNullable(achievementsInThePastYear);
    }

    public IntegerFilter achievementsInThePastYear() {
        if (achievementsInThePastYear == null) {
            setAchievementsInThePastYear(new IntegerFilter());
        }
        return achievementsInThePastYear;
    }

    public void setAchievementsInThePastYear(IntegerFilter achievementsInThePastYear) {
        this.achievementsInThePastYear = achievementsInThePastYear;
    }

    public IntegerFilter getNewYearForecast() {
        return newYearForecast;
    }

    public Optional<IntegerFilter> optionalNewYearForecast() {
        return Optional.ofNullable(newYearForecast);
    }

    public IntegerFilter newYearForecast() {
        if (newYearForecast == null) {
            setNewYearForecast(new IntegerFilter());
        }
        return newYearForecast;
    }

    public void setNewYearForecast(IntegerFilter newYearForecast) {
        this.newYearForecast = newYearForecast;
    }

    public FinancialCategoryEnumFilter getCategory() {
        return category;
    }

    public Optional<FinancialCategoryEnumFilter> optionalCategory() {
        return Optional.ofNullable(category);
    }

    public FinancialCategoryEnumFilter category() {
        if (category == null) {
            setCategory(new FinancialCategoryEnumFilter());
        }
        return category;
    }

    public void setCategory(FinancialCategoryEnumFilter category) {
        this.category = category;
    }

    public LongFilter getFinancialYearId() {
        return financialYearId;
    }

    public Optional<LongFilter> optionalFinancialYearId() {
        return Optional.ofNullable(financialYearId);
    }

    public LongFilter financialYearId() {
        if (financialYearId == null) {
            setFinancialYearId(new LongFilter());
        }
        return financialYearId;
    }

    public void setFinancialYearId(LongFilter financialYearId) {
        this.financialYearId = financialYearId;
    }

    public LongFilter getArticleId() {
        return articleId;
    }

    public Optional<LongFilter> optionalArticleId() {
        return Optional.ofNullable(articleId);
    }

    public LongFilter articleId() {
        if (articleId == null) {
            setArticleId(new LongFilter());
        }
        return articleId;
    }

    public void setArticleId(LongFilter articleId) {
        this.articleId = articleId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RecipeCriteria that = (RecipeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(achievementsInThePastYear, that.achievementsInThePastYear) &&
            Objects.equals(newYearForecast, that.newYearForecast) &&
            Objects.equals(category, that.category) &&
            Objects.equals(financialYearId, that.financialYearId) &&
            Objects.equals(articleId, that.articleId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, achievementsInThePastYear, newYearForecast, category, financialYearId, articleId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecipeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalAchievementsInThePastYear().map(f -> "achievementsInThePastYear=" + f + ", ").orElse("") +
            optionalNewYearForecast().map(f -> "newYearForecast=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalFinancialYearId().map(f -> "financialYearId=" + f + ", ").orElse("") +
            optionalArticleId().map(f -> "articleId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
