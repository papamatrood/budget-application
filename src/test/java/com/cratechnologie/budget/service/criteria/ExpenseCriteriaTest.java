package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.ExpenseCriteria;

class ExpenseCriteriaTest {

    @Test
    void newExpenseCriteriaHasAllFiltersNullTest() {
        var expenseCriteria = new ExpenseCriteria();
        assertThat(expenseCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void expenseCriteriaFluentMethodsCreatesFiltersTest() {
        var expenseCriteria = new ExpenseCriteria();

        setAllFilters(expenseCriteria);

        assertThat(expenseCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void expenseCriteriaCopyCreatesNullFilterTest() {
        var expenseCriteria = new ExpenseCriteria();
        var copy = expenseCriteria.copy();

        assertThat(expenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCriteria)
        );
    }

    @Test
    void expenseCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var expenseCriteria = new ExpenseCriteria();
        setAllFilters(expenseCriteria);

        var copy = expenseCriteria.copy();

        assertThat(expenseCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(expenseCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var expenseCriteria = new ExpenseCriteria();

        assertThat(expenseCriteria).hasToString("ExpenseCriteria{}");
    }

    private static void setAllFilters(ExpenseCriteria expenseCriteria) {
        expenseCriteria.id();
        expenseCriteria.achievementsInThePastYear();
        expenseCriteria.newYearForecast();
        expenseCriteria.category();
        expenseCriteria.financialYearId();
        expenseCriteria.annexDecisionId();
        expenseCriteria.articleId();
        expenseCriteria.distinct();
    }

    private static Condition<ExpenseCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAchievementsInThePastYear()) &&
                condition.apply(criteria.getNewYearForecast()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getFinancialYearId()) &&
                condition.apply(criteria.getAnnexDecisionId()) &&
                condition.apply(criteria.getArticleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ExpenseCriteria> copyFiltersAre(ExpenseCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAchievementsInThePastYear(), copy.getAchievementsInThePastYear()) &&
                condition.apply(criteria.getNewYearForecast(), copy.getNewYearForecast()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getFinancialYearId(), copy.getFinancialYearId()) &&
                condition.apply(criteria.getAnnexDecisionId(), copy.getAnnexDecisionId()) &&
                condition.apply(criteria.getArticleId(), copy.getArticleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
