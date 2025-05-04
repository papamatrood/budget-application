package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DecisionItemCriteriaTest {

    @Test
    void newDecisionItemCriteriaHasAllFiltersNullTest() {
        var decisionItemCriteria = new DecisionItemCriteria();
        assertThat(decisionItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void decisionItemCriteriaFluentMethodsCreatesFiltersTest() {
        var decisionItemCriteria = new DecisionItemCriteria();

        setAllFilters(decisionItemCriteria);

        assertThat(decisionItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void decisionItemCriteriaCopyCreatesNullFilterTest() {
        var decisionItemCriteria = new DecisionItemCriteria();
        var copy = decisionItemCriteria.copy();

        assertThat(decisionItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(decisionItemCriteria)
        );
    }

    @Test
    void decisionItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var decisionItemCriteria = new DecisionItemCriteria();
        setAllFilters(decisionItemCriteria);

        var copy = decisionItemCriteria.copy();

        assertThat(decisionItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(decisionItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var decisionItemCriteria = new DecisionItemCriteria();

        assertThat(decisionItemCriteria).hasToString("DecisionItemCriteria{}");
    }

    private static void setAllFilters(DecisionItemCriteria decisionItemCriteria) {
        decisionItemCriteria.id();
        decisionItemCriteria.beneficiary();
        decisionItemCriteria.amount();
        decisionItemCriteria.observation();
        decisionItemCriteria.decisionId();
        decisionItemCriteria.distinct();
    }

    private static Condition<DecisionItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBeneficiary()) &&
                condition.apply(criteria.getAmount()) &&
                condition.apply(criteria.getObservation()) &&
                condition.apply(criteria.getDecisionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DecisionItemCriteria> copyFiltersAre(
        DecisionItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBeneficiary(), copy.getBeneficiary()) &&
                condition.apply(criteria.getAmount(), copy.getAmount()) &&
                condition.apply(criteria.getObservation(), copy.getObservation()) &&
                condition.apply(criteria.getDecisionId(), copy.getDecisionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
