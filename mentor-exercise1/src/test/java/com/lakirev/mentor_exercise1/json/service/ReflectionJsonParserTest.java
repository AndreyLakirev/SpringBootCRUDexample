package com.lakirev.mentor_exercise1.json.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.data.CustomerTestData;
import com.lakirev.mentor_exercise1.json.util.JsonReaderImpl;
import com.lakirev.mentor_exercise1.json.util.StringObjectParserImpl;
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

    private final ReflectionJsonParser jsonParser = new ReflectionJsonParser(new JsonReaderImpl(), new StringObjectParserImpl());

    private final JsonStreamReader jsonStreamReader = new JsonStreamReader(new JsonReaderImpl());

    private final Logger LOGGER = LoggerFactory.getLogger(ReflectionJsonParserTest.class);

    private static final boolean SPEED_TEST_ENABLED = true;

    private static final int CUSTOMER_COUNT = 1000000;

    private static final int CHAR_BUFFER_SIZE = 1024;

    @Test
    public void testJsonParseSpeed() {
        if (!SPEED_TEST_ENABLED) return;
        try {
            ObjectMapper mapper = new ObjectMapper();
            LOGGER.info("Started testing speed of parsing JSON " + CUSTOMER_COUNT + " random Customer objects");
            long time = System.currentTimeMillis();
            List<Customer> customers = new RandomObjectGenerator().generateRandomObjectsAsync(Customer.class, CUSTOMER_COUNT, 8).get();
            LOGGER.info(CUSTOMER_COUNT + " Customers were generated in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            String json = jsonParser.toJson(customers);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed TO JSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            String jacksonJson = mapper.writeValueAsString(customers);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed TO JSON BY JACKSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            jsonParser.fromJsonToList(json, Customer.class);
            LOGGER.info(CUSTOMER_COUNT + " Customers were parsed FROM JSON in: " + (System.currentTimeMillis() - time) + " milliseconds");
            time = System.currentTimeMillis();
            jsonStreamReader.consumeJsonStream(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)), CHAR_BUFFER_SIZE, (s) -> jsonParser.fromJson(s, Customer.class));
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
        assertEquals(CustomerTestData.CUSTOMER1, jsonParser.fromJson(CustomerTestData.CUSTOMER_JSON1, Customer.class));
    }

    @Test
    public void fromJsonToList() {
        assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), jsonParser.fromJsonToList(CustomerTestData.CUSTOMER_JSON_LIST, Customer.class));
    }

    @Test
    void fromJsonStream() {
        List<Customer> customers = new ArrayList<>();
        jsonStreamReader.consumeJsonStream(new ByteArrayInputStream(CustomerTestData.CUSTOMER_JSON_LIST.getBytes(StandardCharsets.UTF_8)), CHAR_BUFFER_SIZE
                , (s) -> customers.add(jsonParser.fromJson(s, Customer.class)));
        assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), customers);
    }

    @Test
    public void toJson() {
        assertEquals(CustomerTestData.CUSTOMER_JSON1, jsonParser.toJson(CustomerTestData.CUSTOMER1));
        assertEquals(CustomerTestData.CUSTOMER_JSON_LIST, jsonParser.toJson(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2)));
    }

    @Test
    public void testWritingNullValues() {
        jsonParser.setWritingNullValues(true);
        assertEquals(CustomerTestData.CUSTOMER_JSON_WITH_NULLS1, jsonParser.toJson(CustomerTestData.CUSTOMER1));
        jsonParser.setWritingNullValues(false);
    }

    @Test
    public void testCyclicReferenceCase() {
        CyclicReferenceClass reference1 = new CyclicReferenceClass();
        CyclicReferenceClass reference2 = new CyclicReferenceClass();
        reference1.reference = reference2;
        reference2.reference = reference1;
        try {
            jsonParser.toJson(reference1);
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), CYCLIC_REFERENCE_MESSAGE + reference1);
        }
    }

    private static class CyclicReferenceClass {
        private CyclicReferenceClass reference;
    }
}