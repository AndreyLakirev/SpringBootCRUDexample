package com.lakirev.mentor_exercise1.util;

import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.customer.model.Purchase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandomObjectGeneratorTest {
    private static final RandomObjectGenerator generator = new RandomObjectGenerator();

    @Test
    public void generateRandomObject() {
        Customer customer = generator.generateRandomObject(Customer.class);
        assertNotNull(customer.getAge());
        assertNotNull(customer.getName());
        for (Purchase purchase : customer.getPurchases()) {
            assertNotNull(purchase);
        }
    }

    @Test
    public void generateRandomObjects() {
        List<Customer> customers = generator.generateRandomObjects(Customer.class, 3);
        for (Customer customer : customers) {
            assertNotNull(customer.getAge());
            assertNotNull(customer.getName());
            for (Purchase purchase : customer.getPurchases()) {
                assertNotNull(purchase);
            }
        }
    }
}