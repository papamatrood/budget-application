package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DecisionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Decision getDecisionSample1() {
        return new Decision().id(1L).decisionNumber("decisionNumber1");
    }

    public static Decision getDecisionSample2() {
        return new Decision().id(2L).decisionNumber("decisionNumber2");
    }

    public static Decision getDecisionRandomSampleGenerator() {
        return new Decision().id(longCount.incrementAndGet()).decisionNumber(UUID.randomUUID().toString());
    }
}
