package com.cratechnologie.budget.domain;

import static com.cratechnologie.budget.domain.PurchaseOrderTestSamples.*;
import static com.cratechnologie.budget.domain.SupplierTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.budget.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SupplierTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Supplier.class);
        Supplier supplier1 = getSupplierSample1();
        Supplier supplier2 = new Supplier();
        assertThat(supplier1).isNotEqualTo(supplier2);

        supplier2.setId(supplier1.getId());
        assertThat(supplier1).isEqualTo(supplier2);

        supplier2 = getSupplierSample2();
        assertThat(supplier1).isNotEqualTo(supplier2);
    }

    @Test
    void purchaseOrderTest() {
        Supplier supplier = getSupplierRandomSampleGenerator();
        PurchaseOrder purchaseOrderBack = getPurchaseOrderRandomSampleGenerator();

        supplier.addPurchaseOrder(purchaseOrderBack);
        assertThat(supplier.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getSupplier()).isEqualTo(supplier);

        supplier.removePurchaseOrder(purchaseOrderBack);
        assertThat(supplier.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getSupplier()).isNull();

        supplier.purchaseOrders(new HashSet<>(Set.of(purchaseOrderBack)));
        assertThat(supplier.getPurchaseOrders()).containsOnly(purchaseOrderBack);
        assertThat(purchaseOrderBack.getSupplier()).isEqualTo(supplier);

        supplier.setPurchaseOrders(new HashSet<>());
        assertThat(supplier.getPurchaseOrders()).doesNotContain(purchaseOrderBack);
        assertThat(purchaseOrderBack.getSupplier()).isNull();
    }
}
