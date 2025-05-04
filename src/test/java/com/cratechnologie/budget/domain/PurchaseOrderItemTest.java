package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.PurchaseOrderItemTestSamples.*;
import static com.cratechnologie.budget.domain.PurchaseOrderTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PurchaseOrderItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseOrderItem.class);
        PurchaseOrderItem purchaseOrderItem1 = getPurchaseOrderItemSample1();
        PurchaseOrderItem purchaseOrderItem2 = new PurchaseOrderItem();
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);

        purchaseOrderItem2.setId(purchaseOrderItem1.getId());
        assertThat(purchaseOrderItem1).isEqualTo(purchaseOrderItem2);

        purchaseOrderItem2 = getPurchaseOrderItemSample2();
        assertThat(purchaseOrderItem1).isNotEqualTo(purchaseOrderItem2);
    }

    @Test
    void purchaseOrderTest() {
        PurchaseOrderItem purchaseOrderItem = getPurchaseOrderItemRandomSampleGenerator();
        PurchaseOrder purchaseOrderBack = getPurchaseOrderRandomSampleGenerator();

        purchaseOrderItem.setPurchaseOrder(purchaseOrderBack);
        assertThat(purchaseOrderItem.getPurchaseOrder()).isEqualTo(purchaseOrderBack);

        purchaseOrderItem.purchaseOrder(null);
        assertThat(purchaseOrderItem.getPurchaseOrder()).isNull();
    }
}
