package com.lakirev.util;

import com.lakirev.customer.model.Customer;
import com.lakirev.customer.model.Purchase;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RandomObjectGeneratorTest {
    private static final RandomObjectGenerator generator = new RandomObjectGenerator();

    @Test
    public void generateRandomObject() {
        Customer customer = generator.generateRandomObject(Customer.class);
        Assert.assertNotNull(customer.getAge());
        Assert.assertNotNull(customer.getName());
        for (Purchase purchase : customer.getPurchases()) {
            Assert.assertNotNull(purchase);
        }
    }

    @Test
    public void generateRandomObjects() {
        List<Customer> customers = generator.generateRandomObjects(Customer.class, 3);
        for (Customer customer : customers) {
            Assert.assertNotNull(customer.getAge());
            Assert.assertNotNull(customer.getName());
            for (Purchase purchase : customer.getPurchases()) {
                Assert.assertNotNull(purchase);
            }
        }
    }
}