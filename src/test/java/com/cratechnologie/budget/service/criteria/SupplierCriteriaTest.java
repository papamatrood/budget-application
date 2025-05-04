package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SupplierCriteriaTest {

    @Test
    void newSupplierCriteriaHasAllFiltersNullTest() {
        var supplierCriteria = new SupplierCriteria();
        assertThat(supplierCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void supplierCriteriaFluentMethodsCreatesFiltersTest() {
        var supplierCriteria = new SupplierCriteria();

        setAllFilters(supplierCriteria);

        assertThat(supplierCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void supplierCriteriaCopyCreatesNullFilterTest() {
        var supplierCriteria = new SupplierCriteria();
        var copy = supplierCriteria.copy();

        assertThat(supplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(supplierCriteria)
        );
    }

    @Test
    void supplierCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var supplierCriteria = new SupplierCriteria();
        setAllFilters(supplierCriteria);

        var copy = supplierCriteria.copy();

        assertThat(supplierCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(supplierCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var supplierCriteria = new SupplierCriteria();

        assertThat(supplierCriteria).hasToString("SupplierCriteria{}");
    }

    private static void setAllFilters(SupplierCriteria supplierCriteria) {
        supplierCriteria.id();
        supplierCriteria.companyName();
        supplierCriteria.address();
        supplierCriteria.phone();
        supplierCriteria.nifNumber();
        supplierCriteria.commercialRegister();
        supplierCriteria.bankAccount();
        supplierCriteria.mandatingEstablishment();
        supplierCriteria.email();
        supplierCriteria.website();
        supplierCriteria.description();
        supplierCriteria.contactFirstname();
        supplierCriteria.contactlastname();
        supplierCriteria.purchaseOrderId();
        supplierCriteria.distinct();
    }

    private static Condition<SupplierCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCompanyName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getNifNumber()) &&
                condition.apply(criteria.getCommercialRegister()) &&
                condition.apply(criteria.getBankAccount()) &&
                condition.apply(criteria.getMandatingEstablishment()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getContactFirstname()) &&
                condition.apply(criteria.getContactlastname()) &&
                condition.apply(criteria.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SupplierCriteria> copyFiltersAre(SupplierCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCompanyName(), copy.getCompanyName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getNifNumber(), copy.getNifNumber()) &&
                condition.apply(criteria.getCommercialRegister(), copy.getCommercialRegister()) &&
                condition.apply(criteria.getBankAccount(), copy.getBankAccount()) &&
                condition.apply(criteria.getMandatingEstablishment(), copy.getMandatingEstablishment()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getContactFirstname(), copy.getContactFirstname()) &&
                condition.apply(criteria.getContactlastname(), copy.getContactlastname()) &&
                condition.apply(criteria.getPurchaseOrderId(), copy.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
