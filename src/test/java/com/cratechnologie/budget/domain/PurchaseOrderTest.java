package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.AnnexDecisionTestSamples.*;
import static com.cratechnologie.budget.domain.EngagementTestSamples.*;
import static com.cratechnologie.budget.domain.PurchaseOrderItemTestSamples.*;
import static com.cratechnologie.budget.domain.PurchaseOrderTestSamples.*;
import static com.cratechnologie.budget.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PurchaseOrderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrder.class);
        PurchaseOrder purchaseOrder1 = getPurchaseOrderSample1();
        PurchaseOrder purchaseOrder2 = new PurchaseOrder();
        assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);

        purchaseOrder2.setId(purchaseOrder1.getId());
        assertThat(purchaseOrder1).isEqualTo(purchaseOrder2);

        purchaseOrder2 = getPurchaseOrderSample2();
        assertThat(purchaseOrder1).isNotEqualTo(purchaseOrder2);
    }

    @Test
    void annexDecisionTest() {
        PurchaseOrder purchaseOrder = getPurchaseOrderRandomSampleGenerator();
        AnnexDecision annexDecisionBack = getAnnexDecisionRandomSampleGenerator();

        purchaseOrder.setAnnexDecision(annexDecisionBack);
        assertThat(purchaseOrder.getAnnexDecision()).isEqualTo(annexDecisionBack);

        purchaseOrder.annexDecision(null);
        assertThat(purchaseOrder.getAnnexDecision()).isNull();
    }

    @Test
    void supplierTest() {
        PurchaseOrder purchaseOrder = getPurchaseOrderRandomSampleGenerator();
        Supplier supplierBack = getSupplierRandomSampleGenerator();

        purchaseOrder.setSupplier(supplierBack);
        assertThat(purchaseOrder.getSupplier()).isEqualTo(supplierBack);

        purchaseOrder.supplier(null);
        assertThat(purchaseOrder.getSupplier()).isNull();
    }

    @Test
    void engagementTest() {
        PurchaseOrder purchaseOrder = getPurchaseOrderRandomSampleGenerator();
        Engagement engagementBack = getEngagementRandomSampleGenerator();

        purchaseOrder.setEngagement(engagementBack);
        assertThat(purchaseOrder.getEngagement()).isEqualTo(engagementBack);

        purchaseOrder.engagement(null);
        assertThat(purchaseOrder.getEngagement()).isNull();
    }

    @Test
    void purchaseOrderItemTest() {
        PurchaseOrder purchaseOrder = getPurchaseOrderRandomSampleGenerator();
        PurchaseOrderItem purchaseOrderItemBack = getPurchaseOrderItemRandomSampleGenerator();

        purchaseOrder.addPurchaseOrderItem(purchaseOrderItemBack);
        assertThat(purchaseOrder.getPurchaseOrderItems()).containsOnly(purchaseOrderItemBack);
        assertThat(purchaseOrderItemBack.getPurchaseOrder()).isEqualTo(purchaseOrder);

        purchaseOrder.removePurchaseOrderItem(purchaseOrderItemBack);
        assertThat(purchaseOrder.getPurchaseOrderItems()).doesNotContain(purchaseOrderItemBack);
        assertThat(purchaseOrderItemBack.getPurchaseOrder()).isNull();

        purchaseOrder.purchaseOrderItems(new HashSet<>(Set.of(purchaseOrderItemBack)));
        assertThat(purchaseOrder.getPurchaseOrderItems()).containsOnly(purchaseOrderItemBack);
        assertThat(purchaseOrderItemBack.getPurchaseOrder()).isEqualTo(purchaseOrder);

        purchaseOrder.setPurchaseOrderItems(new HashSet<>());
        assertThat(purchaseOrder.getPurchaseOrderItems()).doesNotContain(purchaseOrderItemBack);
        assertThat(purchaseOrderItemBack.getPurchaseOrder()).isNull();
    }
}
