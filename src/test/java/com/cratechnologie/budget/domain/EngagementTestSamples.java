package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EngagementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Engagement getEngagementSample1() {
        return new Engagement()
            .id(1L)
            .engagementNumber("engagementNumber1")
            .objectOfExpense("objectOfExpense1")
            .notifiedCredits("notifiedCredits1")
            .creditCommitted("creditCommitted1")
            .creditsAvailable("creditsAvailable1")
            .amountProposedCommitment("amountProposedCommitment1")
            .headDaf("headDaf1")
            .financialController("financialController1")
            .generalManager("generalManager1");
    }

    public static Engagement getEngagementSample2() {
        return new Engagement()
            .id(2L)
            .engagementNumber("engagementNumber2")
            .objectOfExpense("objectOfExpense2")
            .notifiedCredits("notifiedCredits2")
            .creditCommitted("creditCommitted2")
            .creditsAvailable("creditsAvailable2")
            .amountProposedCommitment("amountProposedCommitment2")
            .headDaf("headDaf2")
            .financialController("financialController2")
            .generalManager("generalManager2");
    }

    public static Engagement getEngagementRandomSampleGenerator() {
        return new Engagement()
            .id(longCount.incrementAndGet())
            .engagementNumber(UUID.randomUUID().toString())
            .objectOfExpense(UUID.randomUUID().toString())
            .notifiedCredits(UUID.randomUUID().toString())
            .creditCommitted(UUID.randomUUID().toString())
            .creditsAvailable(UUID.randomUUID().toString())
            .amountProposedCommitment(UUID.randomUUID().toString())
            .headDaf(UUID.randomUUID().toString())
            .financialController(UUID.randomUUID().toString())
            .generalManager(UUID.randomUUID().toString());
    }
}
