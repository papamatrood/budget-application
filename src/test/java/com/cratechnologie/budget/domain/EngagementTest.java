package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.DecisionTestSamples.*;
import static com.cratechnologie.budget.domain.EngagementTestSamples.*;
import static com.cratechnologie.budget.domain.MandateTestSamples.*;
import static com.cratechnologie.budget.domain.PurchaseOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class EngagementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Engagement.class);
        Engagement engagement1 = getEngagementSample1();
        Engagement engagement2 = new Engagement();
        assertThat(engagement1).isNotEqualTo(engagement2);

        engagement2.setId(engagement1.getId());
        assertThat(engagement1).isEqualTo(engagement2);

        engagement2 = getEngagementSample2();
        assertThat(engagement1).isNotEqualTo(engagement2);
    }

    @Test
    void decisionTest() {
        Engagement engagement = getEngagementRandomSampleGenerator();
        Decision decisionBack = getDecisionRandomSampleGenerator();

        engagement.setDecision(decisionBack);
        assertThat(engagement.getDecision()).isEqualTo(decisionBack);
        assertThat(decisionBack.getEngagement()).isEqualTo(engagement);

        engagement.decision(null);
        assertThat(engagement.getDecision()).isNull();
        assertThat(decisionBack.getEngagement()).isNull();
    }

    @Test
    void mandateTest() {
        Engagement engagement = getEngagementRandomSampleGenerator();
        Mandate mandateBack = getMandateRandomSampleGenerator();

        engagement.setMandate(mandateBack);
        assertThat(engagement.getMandate()).isEqualTo(mandateBack);
        assertThat(mandateBack.getEngagement()).isEqualTo(engagement);

        engagement.mandate(null);
        assertThat(engagement.getMandate()).isNull();
        assertThat(mandateBack.getEngagement()).isNull();
    }

    @Test
    void purchaseOrderTest() {
        Engagement engagement = getEngagementRandomSampleGenerator();
        PurchaseOrder purchaseOrderBack = getPurchaseOrderRandomSampleGenerator();

        engagement.addPurchaseOrder(purchaseOrderBack);
        assertThat(engagement.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getEngagement()).isEqualTo(engagement);

        engagement.removePurchaseOrder(purchaseOrderBack);
        assertThat(engagement.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getEngagement()).isNull();

        engagement.purchaseOrders(new HashSet<>(Set.of(purchaseOrderBack)));
        assertThat(engagement.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getEngagement()).isEqualTo(engagement);

        engagement.setPurchaseOrders(new HashSet<>());
        assertThat(engagement.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getEngagement()).isNull();
    }
}
