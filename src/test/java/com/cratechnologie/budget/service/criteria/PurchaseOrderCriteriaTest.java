package com.cratechnologie.budget.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import com.cratechnologie.budget.service.criteria.PurchaseOrderCriteria;

class PurchaseOrderCriteriaTest {

    @Test
    void newPurchaseOrderCriteriaHasAllFiltersNullTest() {
        var purchaseOrderCriteria = new PurchaseOrderCriteria();
        assertThat(purchaseOrderCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void purchaseOrderCriteriaFluentMethodsCreatesFiltersTest() {
        var purchaseOrderCriteria = new PurchaseOrderCriteria();

        setAllFilters(purchaseOrderCriteria);

        assertThat(purchaseOrderCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void purchaseOrderCriteriaCopyCreatesNullFilterTest() {
        var purchaseOrderCriteria = new PurchaseOrderCriteria();
        var copy = purchaseOrderCriteria.copy();

        assertThat(purchaseOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(purchaseOrderCriteria)
        );
    }

    @Test
    void purchaseOrderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var purchaseOrderCriteria = new PurchaseOrderCriteria();
        setAllFilters(purchaseOrderCriteria);

        var copy = purchaseOrderCriteria.copy();

        assertThat(purchaseOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(purchaseOrderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var purchaseOrderCriteria = new PurchaseOrderCriteria();

        assertThat(purchaseOrderCriteria).hasToString("PurchaseOrderCriteria{}");
    }

    private static void setAllFilters(PurchaseOrderCriteria purchaseOrderCriteria) {
        purchaseOrderCriteria.id();
        purchaseOrderCriteria.nameOfTheMinistry();
        purchaseOrderCriteria.orderNumber();
        purchaseOrderCriteria.orderDate();
        purchaseOrderCriteria.totalAmountWithoutTax();
        purchaseOrderCriteria.taxRate();
        purchaseOrderCriteria.totalTaxAmount();
        purchaseOrderCriteria.prepaidTaxAmount();
        purchaseOrderCriteria.totalAmountWithTax();
        purchaseOrderCriteria.authExpenditureNumber();
        purchaseOrderCriteria.allocatedCredits();
        purchaseOrderCriteria.committedExpenditures();
        purchaseOrderCriteria.availableBalance();
        purchaseOrderCriteria.annexDecisionId();
        purchaseOrderCriteria.supplierId();
        purchaseOrderCriteria.engagementId();
        purchaseOrderCriteria.purchaseOrderItemId();
        purchaseOrderCriteria.distinct();
    }

    private static Condition<PurchaseOrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNameOfTheMinistry()) &&
                condition.apply(criteria.getOrderNumber()) &&
                condition.apply(criteria.getOrderDate()) &&
                condition.apply(criteria.getTotalAmountWithoutTax()) &&
                condition.apply(criteria.getTaxRate()) &&
                condition.apply(criteria.getTotalTaxAmount()) &&
                condition.apply(criteria.getPrepaidTaxAmount()) &&
                condition.apply(criteria.getTotalAmountWithTax()) &&
                condition.apply(criteria.getAuthExpenditureNumber()) &&
                condition.apply(criteria.getAllocatedCredits()) &&
                condition.apply(criteria.getCommittedExpenditures()) &&
                condition.apply(criteria.getAvailableBalance()) &&
                condition.apply(criteria.getAnnexDecisionId()) &&
                condition.apply(criteria.getSupplierId()) &&
                condition.apply(criteria.getEngagementId()) &&
                condition.apply(criteria.getPurchaseOrderItemId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PurchaseOrderCriteria> copyFiltersAre(
        PurchaseOrderCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNameOfTheMinistry(), copy.getNameOfTheMinistry()) &&
                condition.apply(criteria.getOrderNumber(), copy.getOrderNumber()) &&
                condition.apply(criteria.getOrderDate(), copy.getOrderDate()) &&
                condition.apply(criteria.getTotalAmountWithoutTax(), copy.getTotalAmountWithoutTax()) &&
                condition.apply(criteria.getTaxRate(), copy.getTaxRate()) &&
                condition.apply(criteria.getTotalTaxAmount(), copy.getTotalTaxAmount()) &&
                condition.apply(criteria.getPrepaidTaxAmount(), copy.getPrepaidTaxAmount()) &&
                condition.apply(criteria.getTotalAmountWithTax(), copy.getTotalAmountWithTax()) &&
                condition.apply(criteria.getAuthExpenditureNumber(), copy.getAuthExpenditureNumber()) &&
                condition.apply(criteria.getAllocatedCredits(), copy.getAllocatedCredits()) &&
                condition.apply(criteria.getCommittedExpenditures(), copy.getCommittedExpenditures()) &&
                condition.apply(criteria.getAvailableBalance(), copy.getAvailableBalance()) &&
                condition.apply(criteria.getAnnexDecisionId(), copy.getAnnexDecisionId()) &&
                condition.apply(criteria.getSupplierId(), copy.getSupplierId()) &&
                condition.apply(criteria.getEngagementId(), copy.getEngagementId()) &&
                condition.apply(criteria.getPurchaseOrderItemId(), copy.getPurchaseOrderItemId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
