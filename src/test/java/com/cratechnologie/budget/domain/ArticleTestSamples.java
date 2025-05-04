package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArticleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Article getArticleSample1() {
        return new Article()
            .id(1L)
            .code("code1")
            .designation("designation1")
            .accountDiv("accountDiv1")
            .codeEnd("codeEnd1")
            .paragraph("paragraph1");
    }

    public static Article getArticleSample2() {
        return new Article()
            .id(2L)
            .code("code2")
            .designation("designation2")
            .accountDiv("accountDiv2")
            .codeEnd("codeEnd2")
            .paragraph("paragraph2");
    }

    public static Article getArticleRandomSampleGenerator() {
        return new Article()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .designation(UUID.randomUUID().toString())
            .accountDiv(UUID.randomUUID().toString())
            .codeEnd(UUID.randomUUID().toString())
            .paragraph(UUID.randomUUID().toString());
    }
}
