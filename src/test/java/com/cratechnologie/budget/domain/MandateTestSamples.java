package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MandateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Mandate getMandateSample1() {
        return new Mandate()
            .id(1L)
            .mandateNumber("mandateNumber1")
            .issueSlipNumber("issueSlipNumber1")
            .monthAndYearOfIssue("monthAndYearOfIssue1")
            .supportingDocuments("supportingDocuments1");
    }

    public static Mandate getMandateSample2() {
        return new Mandate()
            .id(2L)
            .mandateNumber("mandateNumber2")
            .issueSlipNumber("issueSlipNumber2")
            .monthAndYearOfIssue("monthAndYearOfIssue2")
            .supportingDocuments("supportingDocuments2");
    }

    public static Mandate getMandateRandomSampleGenerator() {
        return new Mandate()
            .id(longCount.incrementAndGet())
            .mandateNumber(UUID.randomUUID().toString())
            .issueSlipNumber(UUID.randomUUID().toString())
            .monthAndYearOfIssue(UUID.randomUUID().toString())
            .supportingDocuments(UUID.randomUUID().toString());
    }
}
