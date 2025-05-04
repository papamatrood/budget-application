package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DecisionItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DecisionItem getDecisionItemSample1() {
        return new DecisionItem().id(1L).beneficiary("beneficiary1").amount(1);
    }

    public static DecisionItem getDecisionItemSample2() {
        return new DecisionItem().id(2L).beneficiary("beneficiary2").amount(2);
    }

    public static DecisionItem getDecisionItemRandomSampleGenerator() {
        return new DecisionItem()
            .id(longCount.incrementAndGet())
            .beneficiary(UUID.randomUUID().toString())
            .amount(intCount.incrementAndGet());
    }
}
