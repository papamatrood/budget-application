package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FinancialYearCriteriaTest {

    @Test
    void newFinancialYearCriteriaHasAllFiltersNullTest() {
        var financialYearCriteria = new FinancialYearCriteria();
        assertThat(financialYearCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void financialYearCriteriaFluentMethodsCreatesFiltersTest() {
        var financialYearCriteria = new FinancialYearCriteria();

        setAllFilters(financialYearCriteria);

        assertThat(financialYearCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void financialYearCriteriaCopyCreatesNullFilterTest() {
        var financialYearCriteria = new FinancialYearCriteria();
        var copy = financialYearCriteria.copy();

        assertThat(financialYearCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(financialYearCriteria)
        );
    }

    @Test
    void financialYearCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var financialYearCriteria = new FinancialYearCriteria();
        setAllFilters(financialYearCriteria);

        var copy = financialYearCriteria.copy();

        assertThat(financialYearCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(financialYearCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var financialYearCriteria = new FinancialYearCriteria();

        assertThat(financialYearCriteria).hasToString("FinancialYearCriteria{}");
    }

    private static void setAllFilters(FinancialYearCriteria financialYearCriteria) {
        financialYearCriteria.id();
        financialYearCriteria.theYear();
        financialYearCriteria.annexDecisionId();
        financialYearCriteria.recipeId();
        financialYearCriteria.expenseId();
        financialYearCriteria.distinct();
    }

    private static Condition<FinancialYearCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTheYear()) &&
                condition.apply(criteria.getAnnexDecisionId()) &&
                condition.apply(criteria.getRecipeId()) &&
                condition.apply(criteria.getExpenseId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FinancialYearCriteria> copyFiltersAre(
        FinancialYearCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTheYear(), copy.getTheYear()) &&
                condition.apply(criteria.getAnnexDecisionId(), copy.getAnnexDecisionId()) &&
                condition.apply(criteria.getRecipeId(), copy.getRecipeId()) &&
                condition.apply(criteria.getExpenseId(), copy.getExpenseId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
