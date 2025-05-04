package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ChapterTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Chapter getChapterSample1() {
        return new Chapter().id(1L).code("code1").designation("designation1");
    }

    public static Chapter getChapterSample2() {
        return new Chapter().id(2L).code("code2").designation("designation2");
    }

    public static Chapter getChapterRandomSampleGenerator() {
        return new Chapter().id(longCount.incrementAndGet()).code(UUID.randomUUID().toString()).designation(UUID.randomUUID().toString());
    }
}
