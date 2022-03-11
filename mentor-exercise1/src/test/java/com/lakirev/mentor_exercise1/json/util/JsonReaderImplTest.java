package com.lakirev.mentor_exercise1.json.util;

import com.lakirev.mentor_exercise1.json.service.ReflectionJsonParser;
import com.lakirev.mentor_exercise1.customer.model.Customer;
import com.lakirev.mentor_exercise1.data.CustomerTestData;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderImplTest {
    private final JsonReader jsonReader = new JsonReaderImpl();

    @Test
    public void getMajorJsonObjects() {
        List<String> majorObjects = jsonReader.readMajorJsonObjects(CustomerTestData.CUSTOMER_JSON_LIST.toCharArray());
        assertEquals(majorObjects.get(0), CustomerTestData.CUSTOMER_JSON1);
        assertEquals(majorObjects.get(1), CustomerTestData.CUSTOMER_JSON2);
    }

    @Test
    public void getObjectParameters() {
        try {
            ReflectionJsonParser parser = new ReflectionJsonParser(jsonReader, new StringObjectParserImpl());
            Map<String, String> parameters = new LinkedHashMap<>();
            Customer customer = CustomerTestData.CUSTOMER1;
            parameters.put("name", customer.getName());
            parameters.put("age", customer.getAge().toString());
            parameters.put("purchases", parser.toJson(customer.getPurchases()));
            assertEquals(parameters, jsonReader.readObjectParameters(parser.toJson(customer).toCharArray()));
        } catch (Exception e) {
            fail();
        }
    }
}