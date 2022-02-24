package com.lakirev.mentor_exercise1.json.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.data.CustomerTestData;
import com.lakirev.mentor_exercise1.json.util.JsonParseUtility;
import com.lakirev.mentor_exercise1.util.RandomObjectGenerator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ReflectionJsonParserTest {
    private static final String CYCLIC_REFERENCE_MESSAGE = "Could not parse json: Unacceptable cyclic reference in target object: ";

    private static final ReflectionJsonParser parser = new ReflectionJsonParser(new JsonParseUtility());

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionJsonParserTest.class);

    private static final int CUSTOMER_COUNT = 1000000;

    private static final int CHAR_BUFFER_SIZE = 1024;
    @Test
    public void testJsonParseSpeed() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info("Started testing speed of parsing JSON " + CUSTOMER_COUNT + " random Customer objects");
            long time = System.currentTimeMillis();
            List<Customer> customers = new RandomObjectGenerator().generateRandomObjects(Customer.class, CUSTOMER_COUNT);
            LOGGER.info(CUSTOMER_COUNT + " Customers were generated in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            String json = parser.toJson(customers);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed TO JSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            String jacksonJson = mapper.writeValueAsString(customers);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed TO JSON BY JACKSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            parser.fromJsonToList(json, Customer.class);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed FROM JSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            parser.fromJsonStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), CHAR_BUFFER_SIZE, Customer.class, (c) -> {});
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed FROM JSON BY STREAM in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            mapper.readValue(jacksonJson, new TypeReference<List<Customer>>(){});
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed FROM JSON BY JACKSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            fail();
        }
    }

    @Test
    public void fromJson() {
        assertEquals(CustomerTestData.CUSTOMER1, parser.fromJson(CustomerTestData.CUSTOMER_JSON1, Customer.class));
    }

    @Test
    public void fromJsonToList() {
        assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), parser.fromJsonToList(CustomerTestData.CUSTOMER_JSON_LIST, Customer.class));
    }

    @Test
    void fromJsonStream() {
        List<Customer> customers = new ArrayList<>();
        parser.fromJsonStream(new ByteArrayInputStream(CustomerTestData.CUSTOMER_JSON_LIST.getBytes(StandardCharsets.UTF_8)), CHAR_BUFFER_SIZE, Customer.class, customers::add);
        assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), customers);
    }

    @Test
    public void toJson() {
        assertEquals(CustomerTestData.CUSTOMER_JSON1, parser.toJson(CustomerTestData.CUSTOMER1));
        assertEquals(CustomerTestData.CUSTOMER_JSON_LIST, parser.toJson(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2)));
    }

    @Test
    public void testWritingNullValues() {
        parser.setWritingNullValues(true);
        assertEquals(CustomerTestData.CUSTOMER_JSON_WITH_NULLS1, parser.toJson(CustomerTestData.CUSTOMER1));
        parser.setWritingNullValues(false);
    }

    @Test
    public void testCyclicReferenceCase() {
        CyclicReferenceClass reference1 = new CyclicReferenceClass();
        CyclicReferenceClass reference2 = new CyclicReferenceClass();
        reference1.reference = reference2;
        reference2.reference = reference1;
        try {
            parser.toJson(reference1);
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), CYCLIC_REFERENCE_MESSAGE + reference1);
        }
    }

    private static class CyclicReferenceClass {
        private CyclicReferenceClass reference;
    }
}