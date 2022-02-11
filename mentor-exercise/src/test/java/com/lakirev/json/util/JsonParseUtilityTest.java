package com.lakirev.json.util;

import com.lakirev.json.parser.CustomJsonParser;
import com.lakirev.customer.model.Customer;
import com.lakirev.data.CustomerTestData;
import org.junit.Test;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonParseUtilityTest {
    private final JsonParseUtility parseUtility = new JsonParseUtility();

    @Test
    public void getMajorJsonObjects() {
        List<String> majorObjects = parseUtility.getMajorJsonObjects(CustomerTestData.CUSTOMER_JSON_LIST);
        assertEquals(majorObjects.get(0), CustomerTestData.CUSTOMER_JSON1);
        assertEquals(majorObjects.get(1), CustomerTestData.CUSTOMER_JSON2);
    }

    @Test
    public void getObjectParameters() {
        try {
            CustomJsonParser parser = new CustomJsonParser(parseUtility);
            Map<String, String> parameters = new LinkedHashMap<>();
            Customer customer = CustomerTestData.CUSTOMER1;
            parameters.put("name", customer.getName());
            parameters.put("age", customer.getAge().toString());
            parameters.put("purchases", parser.toJson(customer.getPurchases()));
            assertEquals(parameters, parseUtility.getObjectParameters(parser.toJson(customer)));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void generateObjectFromString() {
        assertEquals((short) 25, parseUtility.generateObjectFromString("25", short.class));
        assertEquals((short) 25, parseUtility.generateObjectFromString("25", Short.class));
        assertEquals(25, parseUtility.generateObjectFromString("25", int.class));
        assertEquals(25, parseUtility.generateObjectFromString("25", Integer.class));
        assertEquals(25L, parseUtility.generateObjectFromString("25", long.class));
        assertEquals(25L, parseUtility.generateObjectFromString("25", Long.class));
        assertEquals(25f, parseUtility.generateObjectFromString("25", float.class));
        assertEquals(25f, parseUtility.generateObjectFromString("25", Float.class));
        assertEquals(25d, parseUtility.generateObjectFromString("25", double.class));
        assertEquals(25d, parseUtility.generateObjectFromString("25", Double.class));
        assertEquals(true, parseUtility.generateObjectFromString("true", boolean.class));
        assertEquals(false, parseUtility.generateObjectFromString("not true", Boolean.class));
        assertEquals('c', parseUtility.generateObjectFromString("c", char.class));
        assertEquals('c', parseUtility.generateObjectFromString("c", Character.class));
        assertEquals("testString", parseUtility.generateObjectFromString("testString", String.class));
        Date expected = new Date();
        Date actual = (Date) parseUtility.generateObjectFromString(parseUtility.FORMATTER.format(expected), Date.class);
        assertEquals(expected.toString(), actual.toString());
    }
}