package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArticleCriteriaTest {

    @Test
    void newArticleCriteriaHasAllFiltersNullTest() {
        var articleCriteria = new ArticleCriteria();
        assertThat(articleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void articleCriteriaFluentMethodsCreatesFiltersTest() {
        var articleCriteria = new ArticleCriteria();

        setAllFilters(articleCriteria);

        assertThat(articleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void articleCriteriaCopyCreatesNullFilterTest() {
        var articleCriteria = new ArticleCriteria();
        var copy = articleCriteria.copy();

        assertThat(articleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(articleCriteria)
        );
    }

    @Test
    void articleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var articleCriteria = new ArticleCriteria();
        setAllFilters(articleCriteria);

        var copy = articleCriteria.copy();

        assertThat(articleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(articleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var articleCriteria = new ArticleCriteria();

        assertThat(articleCriteria).hasToString("ArticleCriteria{}");
    }

    private static void setAllFilters(ArticleCriteria articleCriteria) {
        articleCriteria.id();
        articleCriteria.category();
        articleCriteria.code();
        articleCriteria.designation();
        articleCriteria.accountDiv();
        articleCriteria.codeEnd();
        articleCriteria.paragraph();
        articleCriteria.chapterId();
        articleCriteria.recipeId();
        articleCriteria.expenseId();
        articleCriteria.distinct();
    }

    private static Condition<ArticleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDesignation()) &&
                condition.apply(criteria.getAccountDiv()) &&
                condition.apply(criteria.getCodeEnd()) &&
                condition.apply(criteria.getParagraph()) &&
                condition.apply(criteria.getChapterId()) &&
                condition.apply(criteria.getRecipeId()) &&
                condition.apply(criteria.getExpenseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArticleCriteria> copyFiltersAre(ArticleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDesignation(), copy.getDesignation()) &&
                condition.apply(criteria.getAccountDiv(), copy.getAccountDiv()) &&
                condition.apply(criteria.getCodeEnd(), copy.getCodeEnd()) &&
                condition.apply(criteria.getParagraph(), copy.getParagraph()) &&
                condition.apply(criteria.getChapterId(), copy.getChapterId()) &&
                condition.apply(criteria.getRecipeId(), copy.getRecipeId()) &&
                condition.apply(criteria.getExpenseId(), copy.getExpenseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
