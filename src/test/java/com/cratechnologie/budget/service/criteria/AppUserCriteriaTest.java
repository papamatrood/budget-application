package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppUserCriteriaTest {

    @Test
    void newAppUserCriteriaHasAllFiltersNullTest() {
        var appUserCriteria = new AppUserCriteria();
        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void appUserCriteriaFluentMethodsCreatesFiltersTest() {
        var appUserCriteria = new AppUserCriteria();

        setAllFilters(appUserCriteria);

        assertThat(appUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void appUserCriteriaCopyCreatesNullFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void appUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appUserCriteria = new AppUserCriteria();
        setAllFilters(appUserCriteria);

        var copy = appUserCriteria.copy();

        assertThat(appUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(appUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appUserCriteria = new AppUserCriteria();

        assertThat(appUserCriteria).hasToString("AppUserCriteria{}");
    }

    private static void setAllFilters(AppUserCriteria appUserCriteria) {
        appUserCriteria.id();
        appUserCriteria.accountStatus();
        appUserCriteria.lastDateUpdate();
        appUserCriteria.dateCreated();
        appUserCriteria.firstname();
        appUserCriteria.lastname();
        appUserCriteria.phoneNumber();
        appUserCriteria.birthDate();
        appUserCriteria.birthPlace();
        appUserCriteria.gender();
        appUserCriteria.familySituation();
        appUserCriteria.position();
        appUserCriteria.address();
        appUserCriteria.userId();
        appUserCriteria.distinct();
    }

    private static Condition<AppUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAccountStatus()) &&
                condition.apply(criteria.getLastDateUpdate()) &&
                condition.apply(criteria.getDateCreated()) &&
                condition.apply(criteria.getFirstname()) &&
                condition.apply(criteria.getLastname()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getFamilySituation()) &&
                condition.apply(criteria.getPosition()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppUserCriteria> copyFiltersAre(AppUserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAccountStatus(), copy.getAccountStatus()) &&
                condition.apply(criteria.getLastDateUpdate(), copy.getLastDateUpdate()) &&
                condition.apply(criteria.getDateCreated(), copy.getDateCreated()) &&
                condition.apply(criteria.getFirstname(), copy.getFirstname()) &&
                condition.apply(criteria.getLastname(), copy.getLastname()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace(), copy.getBirthPlace()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getFamilySituation(), copy.getFamilySituation()) &&
                condition.apply(criteria.getPosition(), copy.getPosition()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
