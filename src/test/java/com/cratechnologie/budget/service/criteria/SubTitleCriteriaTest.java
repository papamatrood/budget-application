package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.SubTitleCriteria;

class SubTitleCriteriaTest {

    @Test
    void newSubTitleCriteriaHasAllFiltersNullTest() {
        var subTitleCriteria = new SubTitleCriteria();
        assertThat(subTitleCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void subTitleCriteriaFluentMethodsCreatesFiltersTest() {
        var subTitleCriteria = new SubTitleCriteria();

        setAllFilters(subTitleCriteria);

        assertThat(subTitleCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void subTitleCriteriaCopyCreatesNullFilterTest() {
        var subTitleCriteria = new SubTitleCriteria();
        var copy = subTitleCriteria.copy();

        assertThat(subTitleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(subTitleCriteria)
        );
    }

    @Test
    void subTitleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var subTitleCriteria = new SubTitleCriteria();
        setAllFilters(subTitleCriteria);

        var copy = subTitleCriteria.copy();

        assertThat(subTitleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(subTitleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var subTitleCriteria = new SubTitleCriteria();

        assertThat(subTitleCriteria).hasToString("SubTitleCriteria{}");
    }

    private static void setAllFilters(SubTitleCriteria subTitleCriteria) {
        subTitleCriteria.id();
        subTitleCriteria.code();
        subTitleCriteria.designation();
        subTitleCriteria.chapterId();
        subTitleCriteria.distinct();
    }

    private static Condition<SubTitleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getDesignation()) &&
                condition.apply(criteria.getChapterId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SubTitleCriteria> copyFiltersAre(SubTitleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getDesignation(), copy.getDesignation()) &&
                condition.apply(criteria.getChapterId(), copy.getChapterId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
