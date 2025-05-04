package com.cratechnologie.budget.service.criteria;

import com.cratechnologie.budget.domain.enumeration.FinancialCategoryEnum;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.cratechnologie.budget.domain.Article} entity. This class is used
 * in {@link com.cratechnologie.budget.web.rest.ArticleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /articles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArticleCriteria implements Serializable, Criteria {

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

    private FinancialCategoryEnumFilter category;

    private StringFilter code;

    private StringFilter designation;

    private StringFilter accountDiv;

    private StringFilter codeEnd;

    private StringFilter paragraph;

    private LongFilter chapterId;

    private LongFilter recipeId;

    private LongFilter expenseId;

    private Boolean distinct;

    public ArticleCriteria() {}

    public ArticleCriteria(ArticleCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.category = other.optionalCategory().map(FinancialCategoryEnumFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.designation = other.optionalDesignation().map(StringFilter::copy).orElse(null);
        this.accountDiv = other.optionalAccountDiv().map(StringFilter::copy).orElse(null);
        this.codeEnd = other.optionalCodeEnd().map(StringFilter::copy).orElse(null);
        this.paragraph = other.optionalParagraph().map(StringFilter::copy).orElse(null);
        this.chapterId = other.optionalChapterId().map(LongFilter::copy).orElse(null);
        this.recipeId = other.optionalRecipeId().map(LongFilter::copy).orElse(null);
        this.expenseId = other.optionalExpenseId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ArticleCriteria copy() {
        return new ArticleCriteria(this);
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

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getDesignation() {
        return designation;
    }

    public Optional<StringFilter> optionalDesignation() {
        return Optional.ofNullable(designation);
    }

    public StringFilter designation() {
        if (designation == null) {
            setDesignation(new StringFilter());
        }
        return designation;
    }

    public void setDesignation(StringFilter designation) {
        this.designation = designation;
    }

    public StringFilter getAccountDiv() {
        return accountDiv;
    }

    public Optional<StringFilter> optionalAccountDiv() {
        return Optional.ofNullable(accountDiv);
    }

    public StringFilter accountDiv() {
        if (accountDiv == null) {
            setAccountDiv(new StringFilter());
        }
        return accountDiv;
    }

    public void setAccountDiv(StringFilter accountDiv) {
        this.accountDiv = accountDiv;
    }

    public StringFilter getCodeEnd() {
        return codeEnd;
    }

    public Optional<StringFilter> optionalCodeEnd() {
        return Optional.ofNullable(codeEnd);
    }

    public StringFilter codeEnd() {
        if (codeEnd == null) {
            setCodeEnd(new StringFilter());
        }
        return codeEnd;
    }

    public void setCodeEnd(StringFilter codeEnd) {
        this.codeEnd = codeEnd;
    }

    public StringFilter getParagraph() {
        return paragraph;
    }

    public Optional<StringFilter> optionalParagraph() {
        return Optional.ofNullable(paragraph);
    }

    public StringFilter paragraph() {
        if (paragraph == null) {
            setParagraph(new StringFilter());
        }
        return paragraph;
    }

    public void setParagraph(StringFilter paragraph) {
        this.paragraph = paragraph;
    }

    public LongFilter getChapterId() {
        return chapterId;
    }

    public Optional<LongFilter> optionalChapterId() {
        return Optional.ofNullable(chapterId);
    }

    public LongFilter chapterId() {
        if (chapterId == null) {
            setChapterId(new LongFilter());
        }
        return chapterId;
    }

    public void setChapterId(LongFilter chapterId) {
        this.chapterId = chapterId;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public Optional<LongFilter> optionalRecipeId() {
        return Optional.ofNullable(recipeId);
    }

    public LongFilter recipeId() {
        if (recipeId == null) {
            setRecipeId(new LongFilter());
        }
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getExpenseId() {
        return expenseId;
    }

    public Optional<LongFilter> optionalExpenseId() {
        return Optional.ofNullable(expenseId);
    }

    public LongFilter expenseId() {
        if (expenseId == null) {
            setExpenseId(new LongFilter());
        }
        return expenseId;
    }

    public void setExpenseId(LongFilter expenseId) {
        this.expenseId = expenseId;
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
        final ArticleCriteria that = (ArticleCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(category, that.category) &&
            Objects.equals(code, that.code) &&
            Objects.equals(designation, that.designation) &&
            Objects.equals(accountDiv, that.accountDiv) &&
            Objects.equals(codeEnd, that.codeEnd) &&
            Objects.equals(paragraph, that.paragraph) &&
            Objects.equals(chapterId, that.chapterId) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(expenseId, that.expenseId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, code, designation, accountDiv, codeEnd, paragraph, chapterId, recipeId, expenseId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArticleCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCategory().map(f -> "category=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalDesignation().map(f -> "designation=" + f + ", ").orElse("") +
            optionalAccountDiv().map(f -> "accountDiv=" + f + ", ").orElse("") +
            optionalCodeEnd().map(f -> "codeEnd=" + f + ", ").orElse("") +
            optionalParagraph().map(f -> "paragraph=" + f + ", ").orElse("") +
            optionalChapterId().map(f -> "chapterId=" + f + ", ").orElse("") +
            optionalRecipeId().map(f -> "recipeId=" + f + ", ").orElse("") +
            optionalExpenseId().map(f -> "expenseId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
