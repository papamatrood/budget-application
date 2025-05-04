package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PurchaseOrderItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PurchaseOrderItem getPurchaseOrderItemSample1() {
        return new PurchaseOrderItem().id(1L).productName("productName1").quantity(1);
    }

    public static PurchaseOrderItem getPurchaseOrderItemSample2() {
        return new PurchaseOrderItem().id(2L).productName("productName2").quantity(2);
    }

    public static PurchaseOrderItem getPurchaseOrderItemRandomSampleGenerator() {
        return new PurchaseOrderItem()
            .id(longCount.incrementAndGet())
            .productName(UUID.randomUUID().toString())
            .quantity(intCount.incrementAndGet());
    }
}
