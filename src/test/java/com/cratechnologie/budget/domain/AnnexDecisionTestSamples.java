package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnnexDecisionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AnnexDecision getAnnexDecisionSample1() {
        return new AnnexDecision()
            .id(1L)
            .designation("designation1")
            .expenseAmount("expenseAmount1")
            .creditsAlreadyOpen("creditsAlreadyOpen1")
            .creditsOpen("creditsOpen1");
    }

    public static AnnexDecision getAnnexDecisionSample2() {
        return new AnnexDecision()
            .id(2L)
            .designation("designation2")
            .expenseAmount("expenseAmount2")
            .creditsAlreadyOpen("creditsAlreadyOpen2")
            .creditsOpen("creditsOpen2");
    }

    public static AnnexDecision getAnnexDecisionRandomSampleGenerator() {
        return new AnnexDecision()
            .id(longCount.incrementAndGet())
            .designation(UUID.randomUUID().toString())
            .expenseAmount(UUID.randomUUID().toString())
            .creditsAlreadyOpen(UUID.randomUUID().toString())
            .creditsOpen(UUID.randomUUID().toString());
    }
}
