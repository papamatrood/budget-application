package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MandateCriteriaTest {

    @Test
    void newMandateCriteriaHasAllFiltersNullTest() {
        var mandateCriteria = new MandateCriteria();
        assertThat(mandateCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void mandateCriteriaFluentMethodsCreatesFiltersTest() {
        var mandateCriteria = new MandateCriteria();

        setAllFilters(mandateCriteria);

        assertThat(mandateCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void mandateCriteriaCopyCreatesNullFilterTest() {
        var mandateCriteria = new MandateCriteria();
        var copy = mandateCriteria.copy();

        assertThat(mandateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(mandateCriteria)
        );
    }

    @Test
    void mandateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var mandateCriteria = new MandateCriteria();
        setAllFilters(mandateCriteria);

        var copy = mandateCriteria.copy();

        assertThat(mandateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(mandateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var mandateCriteria = new MandateCriteria();

        assertThat(mandateCriteria).hasToString("MandateCriteria{}");
    }

    private static void setAllFilters(MandateCriteria mandateCriteria) {
        mandateCriteria.id();
        mandateCriteria.mandateNumber();
        mandateCriteria.mandateDate();
        mandateCriteria.issueSlipNumber();
        mandateCriteria.monthAndYearOfIssue();
        mandateCriteria.supportingDocuments();
        mandateCriteria.engagementId();
        mandateCriteria.distinct();
    }

    private static Condition<MandateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMandateNumber()) &&
                condition.apply(criteria.getMandateDate()) &&
                condition.apply(criteria.getIssueSlipNumber()) &&
                condition.apply(criteria.getMonthAndYearOfIssue()) &&
                condition.apply(criteria.getSupportingDocuments()) &&
                condition.apply(criteria.getEngagementId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MandateCriteria> copyFiltersAre(MandateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMandateNumber(), copy.getMandateNumber()) &&
                condition.apply(criteria.getMandateDate(), copy.getMandateDate()) &&
                condition.apply(criteria.getIssueSlipNumber(), copy.getIssueSlipNumber()) &&
                condition.apply(criteria.getMonthAndYearOfIssue(), copy.getMonthAndYearOfIssue()) &&
                condition.apply(criteria.getSupportingDocuments(), copy.getSupportingDocuments()) &&
                condition.apply(criteria.getEngagementId(), copy.getEngagementId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
