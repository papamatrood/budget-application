package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SubTitleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SubTitle getSubTitleSample1() {
        return new SubTitle().id(1L).code("code1").designation("designation1");
    }

    public static SubTitle getSubTitleSample2() {
        return new SubTitle().id(2L).code("code2").designation("designation2");
    }

    public static SubTitle getSubTitleRandomSampleGenerator() {
        return new SubTitle().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).designation(UUID.randomUUID().toString());
    }
}
