package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.PurchaseOrderItemCriteria;

class PurchaseOrderItemCriteriaTest {

    @Test
    void newPurchaseOrderItemCriteriaHasAllFiltersNullTest() {
        var purchaseOrderItemCriteria = new PurchaseOrderItemCriteria();
        assertThat(purchaseOrderItemCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void purchaseOrderItemCriteriaFluentMethodsCreatesFiltersTest() {
        var purchaseOrderItemCriteria = new PurchaseOrderItemCriteria();

        setAllFilters(purchaseOrderItemCriteria);

        assertThat(purchaseOrderItemCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void purchaseOrderItemCriteriaCopyCreatesNullFilterTest() {
        var purchaseOrderItemCriteria = new PurchaseOrderItemCriteria();
        var copy = purchaseOrderItemCriteria.copy();

        assertThat(purchaseOrderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(purchaseOrderItemCriteria)
        );
    }

    @Test
    void purchaseOrderItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var purchaseOrderItemCriteria = new PurchaseOrderItemCriteria();
        setAllFilters(purchaseOrderItemCriteria);

        var copy = purchaseOrderItemCriteria.copy();

        assertThat(purchaseOrderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(purchaseOrderItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var purchaseOrderItemCriteria = new PurchaseOrderItemCriteria();

        assertThat(purchaseOrderItemCriteria).hasToString("PurchaseOrderItemCriteria{}");
    }

    private static void setAllFilters(PurchaseOrderItemCriteria purchaseOrderItemCriteria) {
        purchaseOrderItemCriteria.id();
        purchaseOrderItemCriteria.productName();
        purchaseOrderItemCriteria.quantity();
        purchaseOrderItemCriteria.unitPrice();
        purchaseOrderItemCriteria.totalAmount();
        purchaseOrderItemCriteria.purchaseOrderId();
        purchaseOrderItemCriteria.distinct();
    }

    private static Condition<PurchaseOrderItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProductName()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getTotalAmount()) &&
                condition.apply(criteria.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PurchaseOrderItemCriteria> copyFiltersAre(
        PurchaseOrderItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProductName(), copy.getProductName()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getTotalAmount(), copy.getTotalAmount()) &&
                condition.apply(criteria.getPurchaseOrderId(), copy.getPurchaseOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
