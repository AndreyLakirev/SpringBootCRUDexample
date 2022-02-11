package com.lakirev.customer;


import com.lakirev.application.Application;
import com.lakirev.customer.model.Customer;
import com.lakirev.customer.service.CustomerService;
import com.lakirev.json.parser.CustomJsonParser;
import com.lakirev.util.RandomObjectGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@Transactional
public class CustomerIntegrationTest {
    @Value("${test.customer.insert-count}")
    private int CUSTOMER_COUNT;

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerIntegrationTest.class);

    @Autowired
    private CustomerService service;

    @Autowired
    private CustomJsonParser parser;

    @Autowired
    private RandomObjectGenerator objectGenerator;

    @Test
    public void testLargeInsertSpeed() {
        try {
            LOGGER.info("Started testing speed of inserting " + CUSTOMER_COUNT + " random Customer objects");
            Date date = new Date();
            List<Customer> customers = objectGenerator.generateRandomObjects(Customer.class, CUSTOMER_COUNT);
            LOGGER.info(CUSTOMER_COUNT + " Customers were generated in: " + (new Date().getTime() - date.getTime()) + " milliseconds");
            date = new Date();
            for (Customer customer : customers) {
                service.insert(customer);
            }
            LOGGER.info(CUSTOMER_COUNT + " Customers were successfully inserted in base in: " + (new Date().getTime() - date.getTime()) + " milliseconds");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            fail();
        }
    }

    @Test
    public void testCustomJsonParseSpeed() {
        try {
            LOGGER.info("Started testing speed of parsing JSON " + CUSTOMER_COUNT + " random Customer objects");
            Date date = new Date();
            List<Customer> customers = objectGenerator.generateRandomObjects(Customer.class, CUSTOMER_COUNT);
            LOGGER.info(CUSTOMER_COUNT + " Customers were generated in: " + (new Date().getTime() - date.getTime()) + " milliseconds");
            date = new Date();
            String json = parser.toJson(customers);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed TO JSON in: " + (new Date().getTime() - date.getTime()) + " milliseconds");
            date = new Date();
            parser.fromJsonToList(json, Customer.class);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed FROM JSON in: " + (new Date().getTime() - date.getTime()) + " milliseconds");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            fail();
        }
    }
}
