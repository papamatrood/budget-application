package com.cratechnologie.budget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SupplierTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Supplier getSupplierSample1() {
        return new Supplier()
            .id(1L)
            .companyName("companyName1")
            .address("address1")
            .phone("phone1")
            .nifNumber("nifNumber1")
            .commercialRegister("commercialRegister1")
            .bankAccount("bankAccount1")
            .mandatingEstablishment("mandatingEstablishment1")
            .email("email1")
            .website("website1")
            .description("description1")
            .contactFirstname("contactFirstname1")
            .contactlastname("contactlastname1");
    }

    public static Supplier getSupplierSample2() {
        return new Supplier()
            .id(2L)
            .companyName("companyName2")
            .address("address2")
            .phone("phone2")
            .nifNumber("nifNumber2")
            .commercialRegister("commercialRegister2")
            .bankAccount("bankAccount2")
            .mandatingEstablishment("mandatingEstablishment2")
            .email("email2")
            .website("website2")
            .description("description2")
            .contactFirstname("contactFirstname2")
            .contactlastname("contactlastname2");
    }

    public static Supplier getSupplierRandomSampleGenerator() {
        return new Supplier()
            .id(longCount.incrementAndGet())
            .companyName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .nifNumber(UUID.randomUUID().toString())
            .commercialRegister(UUID.randomUUID().toString())
            .bankAccount(UUID.randomUUID().toString())
            .mandatingEstablishment(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .website(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .contactFirstname(UUID.randomUUID().toString())
            .contactlastname(UUID.randomUUID().toString());
    }
}
