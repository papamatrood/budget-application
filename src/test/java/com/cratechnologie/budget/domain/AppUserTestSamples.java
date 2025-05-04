package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AppUserTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AppUser getAppUserSample1() {
        return new AppUser()
            .id(1L)
            .firstname("firstname1")
            .lastname("lastname1")
            .phoneNumber("phoneNumber1")
            .birthPlace("birthPlace1")
            .position("position1")
            .address("address1");
    }

    public static AppUser getAppUserSample2() {
        return new AppUser()
            .id(2L)
            .firstname("firstname2")
            .lastname("lastname2")
            .phoneNumber("phoneNumber2")
            .birthPlace("birthPlace2")
            .position("position2")
            .address("address2");
    }

    public static AppUser getAppUserRandomSampleGenerator() {
        return new AppUser()
            .id(longCount.incrementAndGet())
            .firstname(UUID.randomUUID().toString())
            .lastname(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .birthPlace(UUID.randomUUID().toString())
            .position(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
