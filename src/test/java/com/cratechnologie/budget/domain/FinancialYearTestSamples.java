package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FinancialYearTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FinancialYear getFinancialYearSample1() {
        return new FinancialYear().id(1L).theYear(1);
    }

    public static FinancialYear getFinancialYearSample2() {
        return new FinancialYear().id(2L).theYear(2);
    }

    public static FinancialYear getFinancialYearRandomSampleGenerator() {
        return new FinancialYear().id(longCount.incrementAndGet()).theYear(intCount.incrementAndGet());
    }
}
