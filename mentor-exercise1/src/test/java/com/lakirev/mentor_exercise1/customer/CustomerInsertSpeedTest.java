package com.lakirev.mentor_exercise1.customer;


import com.lakirev.mentor_exercise1.MentorExercise1Application;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.customer.service.CustomerService;
import com.lakirev.mentor_exercise1.util.RandomObjectGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MentorExercise1Application.class)
@Transactional
public class CustomerInsertSpeedTest {
    @Value("${test.customer.insert-count}")
    private int CUSTOMER_COUNT;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerInsertSpeedTest.class);

    @Autowired
    private CustomerService service;

    @Autowired
    private RandomObjectGenerator objectGenerator;

    @Test
    public void testLargeInsertSpeed() {
        try {
            LOGGER.info("Started testing speed of inserting " + CUSTOMER_COUNT + " random Customer objects");
            long time = System.currentTimeMillis();
            List<Customer> customers = objectGenerator.generateRandomObjects(Customer.class, CUSTOMER_COUNT);
            LOGGER.info(CUSTOMER_COUNT + " Customers were generated in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            for (Customer customer : customers) {
                service.insert(customer);
            }
            LOGGER.info(CUSTOMER_COUNT + " Customers were successfully inserted in base in: " + (System.currentTimeMillis() - time) + " milliseconds");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            fail();
        }
    }
}
