package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PurchaseOrderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PurchaseOrder getPurchaseOrderSample1() {
        return new PurchaseOrder()
            .id(1L)
            .nameOfTheMinistry("nameOfTheMinistry1")
            .orderNumber("orderNumber1")
            .authExpenditureNumber("authExpenditureNumber1");
    }

    public static PurchaseOrder getPurchaseOrderSample2() {
        return new PurchaseOrder()
            .id(2L)
            .nameOfTheMinistry("nameOfTheMinistry2")
            .orderNumber("orderNumber2")
            .authExpenditureNumber("authExpenditureNumber2");
    }

    public static PurchaseOrder getPurchaseOrderRandomSampleGenerator() {
        return new PurchaseOrder()
            .id(longCount.incrementAndGet())
            .nameOfTheMinistry(UUID.randomUUID().toString())
            .orderNumber(UUID.randomUUID().toString())
            .authExpenditureNumber(UUID.randomUUID().toString());
    }
}
