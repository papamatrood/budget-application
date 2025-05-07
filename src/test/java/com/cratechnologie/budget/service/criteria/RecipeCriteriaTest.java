package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.RecipeCriteria;

class RecipeCriteriaTest {

    @Test
    void newRecipeCriteriaHasAllFiltersNullTest() {
        var recipeCriteria = new RecipeCriteria();
        assertThat(recipeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void recipeCriteriaFluentMethodsCreatesFiltersTest() {
        var recipeCriteria = new RecipeCriteria();

        setAllFilters(recipeCriteria);

        assertThat(recipeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void recipeCriteriaCopyCreatesNullFilterTest() {
        var recipeCriteria = new RecipeCriteria();
        var copy = recipeCriteria.copy();

        assertThat(recipeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(recipeCriteria)
        );
    }

    @Test
    void recipeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var recipeCriteria = new RecipeCriteria();
        setAllFilters(recipeCriteria);

        var copy = recipeCriteria.copy();

        assertThat(recipeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(recipeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var recipeCriteria = new RecipeCriteria();

        assertThat(recipeCriteria).hasToString("RecipeCriteria{}");
    }

    private static void setAllFilters(RecipeCriteria recipeCriteria) {
        recipeCriteria.id();
        recipeCriteria.achievementsInThePastYear();
        recipeCriteria.newYearForecast();
        recipeCriteria.category();
        recipeCriteria.financialYearId();
        recipeCriteria.articleId();
        recipeCriteria.distinct();
    }

    private static Condition<RecipeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAchievementsInThePastYear()) &&
                condition.apply(criteria.getNewYearForecast()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getFinancialYearId()) &&
                condition.apply(criteria.getArticleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RecipeCriteria> copyFiltersAre(RecipeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAchievementsInThePastYear(), copy.getAchievementsInThePastYear()) &&
                condition.apply(criteria.getNewYearForecast(), copy.getNewYearForecast()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getFinancialYearId(), copy.getFinancialYearId()) &&
                condition.apply(criteria.getArticleId(), copy.getArticleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
