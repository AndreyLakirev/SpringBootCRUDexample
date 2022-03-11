package com.lakirev.mentor_exercise1.json.service;

import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.data.CustomerTestData;
import com.lakirev.mentor_exercise1.json.util.JsonReaderImpl;
import com.lakirev.mentor_exercise1.json.util.StringObjectParserImpl;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class JsonStreamReaderTest {
    private final JsonParser parser = new ReflectionJsonParser(new JsonReaderImpl(), new StringObjectParserImpl());

    private final JsonStreamReader jsonStreamReader = new JsonStreamReader(new JsonReaderImpl());

    @Test
    void consumeJsonStream() {
        List<Customer> customers = new ArrayList<>();
        jsonStreamReader.consumeJsonStream(new ByteArrayInputStream(CustomerTestData.CUSTOMER_JSON_LIST.getBytes(StandardCharsets.UTF_8)), 1024, (s) -> customers.add(parser.fromJson(s, Customer.class)));
        assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), customers);
    }
}