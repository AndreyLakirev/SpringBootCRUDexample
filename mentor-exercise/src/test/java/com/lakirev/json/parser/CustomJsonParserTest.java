package com.lakirev.json.parser;

import com.lakirev.json.exception.JsonParseException;
import com.lakirev.customer.model.Customer;
import com.lakirev.data.CustomerTestData;
import com.lakirev.json.util.JsonParseUtility;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CustomJsonParserTest {
    private static final String CYCLIC_REFERENCE_MESSAGE = "Could not parse json: Unacceptable cyclic reference in target object: ";

    private static final CustomJsonParser parser = new CustomJsonParser(new JsonParseUtility());

    @Test
    public void fromJson() {
        try {
            assertEquals(CustomerTestData.CUSTOMER1, parser.fromJson(CustomerTestData.CUSTOMER_JSON1, Customer.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void fromJsonToList() {
        try {
            assertEquals(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2), parser.fromJsonToList(CustomerTestData.CUSTOMER_JSON_LIST, Customer.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void toJson() {
        try {
            assertEquals(CustomerTestData.CUSTOMER_JSON1, parser.toJson(CustomerTestData.CUSTOMER1));
            assertEquals(CustomerTestData.CUSTOMER_JSON_LIST, parser.toJson(Arrays.asList(CustomerTestData.CUSTOMER1, CustomerTestData.CUSTOMER2)));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testWritingNullValues() {
        parser.setWritingNullValues(true);
        try {
            assertEquals(CustomerTestData.CUSTOMER_JSON_WITH_NULLS1, parser.toJson(CustomerTestData.CUSTOMER1));
            parser.setWritingNullValues(false);
        } catch (Exception e) {
            fail();
        }
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