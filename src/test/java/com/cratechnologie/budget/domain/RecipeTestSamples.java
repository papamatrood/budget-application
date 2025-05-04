package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RecipeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Recipe getRecipeSample1() {
        return new Recipe().id(1L).achievementsInThePastYear(1).newYearForecast(1);
    }

    public static Recipe getRecipeSample2() {
        return new Recipe().id(2L).achievementsInThePastYear(2).newYearForecast(2);
    }

    public static Recipe getRecipeRandomSampleGenerator() {
        return new Recipe()
            .id(longCount.incrementAndGet())
            .achievementsInThePastYear(intCount.incrementAndGet())
            .newYearForecast(intCount.incrementAndGet());
    }
}
