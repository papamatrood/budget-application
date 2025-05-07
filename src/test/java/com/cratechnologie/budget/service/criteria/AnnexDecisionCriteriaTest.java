package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.AnnexDecisionCriteria;

class AnnexDecisionCriteriaTest {

    @Test
    void newAnnexDecisionCriteriaHasAllFiltersNullTest() {
        var annexDecisionCriteria = new AnnexDecisionCriteria();
        assertThat(annexDecisionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void annexDecisionCriteriaFluentMethodsCreatesFiltersTest() {
        var annexDecisionCriteria = new AnnexDecisionCriteria();

        setAllFilters(annexDecisionCriteria);

        assertThat(annexDecisionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void annexDecisionCriteriaCopyCreatesNullFilterTest() {
        var annexDecisionCriteria = new AnnexDecisionCriteria();
        var copy = annexDecisionCriteria.copy();

        assertThat(annexDecisionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(annexDecisionCriteria)
        );
    }

    @Test
    void annexDecisionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var annexDecisionCriteria = new AnnexDecisionCriteria();
        setAllFilters(annexDecisionCriteria);

        var copy = annexDecisionCriteria.copy();

        assertThat(annexDecisionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(annexDecisionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var annexDecisionCriteria = new AnnexDecisionCriteria();

        assertThat(annexDecisionCriteria).hasToString("AnnexDecisionCriteria{}");
    }

    private static void setAllFilters(AnnexDecisionCriteria annexDecisionCriteria) {
        annexDecisionCriteria.id();
        annexDecisionCriteria.designation();
        annexDecisionCriteria.expenseAmount();
        annexDecisionCriteria.creditsAlreadyOpen();
        annexDecisionCriteria.creditsOpen();
        annexDecisionCriteria.financialYearId();
        annexDecisionCriteria.expenseId();
        annexDecisionCriteria.purchaseOrderId();
        annexDecisionCriteria.decisionId();
        annexDecisionCriteria.distinct();
    }

    private static Condition<AnnexDecisionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDesignation()) &&
                condition.apply(criteria.getExpenseAmount()) &&
                condition.apply(criteria.getCreditsAlreadyOpen()) &&
                condition.apply(criteria.getCreditsOpen()) &&
                condition.apply(criteria.getFinancialYearId()) &&
                condition.apply(criteria.getExpenseId()) &&
                condition.apply(criteria.getPurchaseOrderId()) &&
                condition.apply(criteria.getDecisionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AnnexDecisionCriteria> copyFiltersAre(
        AnnexDecisionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDesignation(), copy.getDesignation()) &&
                condition.apply(criteria.getExpenseAmount(), copy.getExpenseAmount()) &&
                condition.apply(criteria.getCreditsAlreadyOpen(), copy.getCreditsAlreadyOpen()) &&
                condition.apply(criteria.getCreditsOpen(), copy.getCreditsOpen()) &&
                condition.apply(criteria.getFinancialYearId(), copy.getFinancialYearId()) &&
                condition.apply(criteria.getExpenseId(), copy.getExpenseId()) &&
                condition.apply(criteria.getPurchaseOrderId(), copy.getPurchaseOrderId()) &&
                condition.apply(criteria.getDecisionId(), copy.getDecisionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
