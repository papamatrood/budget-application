package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EngagementCriteriaTest {

    @Test
    void newEngagementCriteriaHasAllFiltersNullTest() {
        var engagementCriteria = new EngagementCriteria();
        assertThat(engagementCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void engagementCriteriaFluentMethodsCreatesFiltersTest() {
        var engagementCriteria = new EngagementCriteria();

        setAllFilters(engagementCriteria);

        assertThat(engagementCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void engagementCriteriaCopyCreatesNullFilterTest() {
        var engagementCriteria = new EngagementCriteria();
        var copy = engagementCriteria.copy();

        assertThat(engagementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(engagementCriteria)
        );
    }

    @Test
    void engagementCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var engagementCriteria = new EngagementCriteria();
        setAllFilters(engagementCriteria);

        var copy = engagementCriteria.copy();

        assertThat(engagementCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(engagementCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var engagementCriteria = new EngagementCriteria();

        assertThat(engagementCriteria).hasToString("EngagementCriteria{}");
    }

    private static void setAllFilters(EngagementCriteria engagementCriteria) {
        engagementCriteria.id();
        engagementCriteria.engagementNumber();
        engagementCriteria.engagementDate();
        engagementCriteria.objectOfExpense();
        engagementCriteria.notifiedCredits();
        engagementCriteria.creditCommitted();
        engagementCriteria.creditsAvailable();
        engagementCriteria.amountProposedCommitment();
        engagementCriteria.headDaf();
        engagementCriteria.financialController();
        engagementCriteria.generalManager();
        engagementCriteria.decisionId();
        engagementCriteria.mandateId();
        engagementCriteria.purchaseOrderId();
        engagementCriteria.distinct();
    }

    private static Condition<EngagementCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEngagementNumber()) &&
                condition.apply(criteria.getEngagementDate()) &&
                condition.apply(criteria.getObjectOfExpense()) &&
                condition.apply(criteria.getNotifiedCredits()) &&
                condition.apply(criteria.getCreditCommitted()) &&
                condition.apply(criteria.getCreditsAvailable()) &&
                condition.apply(criteria.getAmountProposedCommitment()) &&
                condition.apply(criteria.getHeadDaf()) &&
                condition.apply(criteria.getFinancialController()) &&
                condition.apply(criteria.getGeneralManager()) &&
                condition.apply(criteria.getDecisionId()) &&
                condition.apply(criteria.getMandateId()) &&
                condition.apply(criteria.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EngagementCriteria> copyFiltersAre(EngagementCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEngagementNumber(), copy.getEngagementNumber()) &&
                condition.apply(criteria.getEngagementDate(), copy.getEngagementDate()) &&
                condition.apply(criteria.getObjectOfExpense(), copy.getObjectOfExpense()) &&
                condition.apply(criteria.getNotifiedCredits(), copy.getNotifiedCredits()) &&
                condition.apply(criteria.getCreditCommitted(), copy.getCreditCommitted()) &&
                condition.apply(criteria.getCreditsAvailable(), copy.getCreditsAvailable()) &&
                condition.apply(criteria.getAmountProposedCommitment(), copy.getAmountProposedCommitment()) &&
                condition.apply(criteria.getHeadDaf(), copy.getHeadDaf()) &&
                condition.apply(criteria.getFinancialController(), copy.getFinancialController()) &&
                condition.apply(criteria.getGeneralManager(), copy.getGeneralManager()) &&
                condition.apply(criteria.getDecisionId(), copy.getDecisionId()) &&
                condition.apply(criteria.getMandateId(), copy.getMandateId()) &&
                condition.apply(criteria.getPurchaseOrderId(), copy.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
