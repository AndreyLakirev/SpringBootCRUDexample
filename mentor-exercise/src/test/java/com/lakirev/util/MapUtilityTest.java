package com.lakirev.util;

import com.lakirev.data.CustomerTestData;
import com.lakirev.json.parser.CustomJsonParser;
import com.lakirev.json.util.JsonParseUtility;
import com.lakirev.customer.model.Purchase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class MapUtilityTest {
    private final JsonParseUtility parseUtility = new JsonParseUtility();
    private final CustomJsonParser parser = new CustomJsonParser(parseUtility);

    @Test
    public void sortByValueSymbolCount() {
        try {
            Map<String, String> map = parseUtility.getObjectParameters(CustomerTestData.CUSTOMER_JSON1);
            map = MapUtility.sortByValueSymbolCount(map);
            List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
            assertEquals(CustomerTestData.CUSTOMER1.getAge().toString(), list.get(0).getValue());
            assertEquals(CustomerTestData.CUSTOMER1.getName(), list.get(1).getValue());
            assertEquals(CustomerTestData.CUSTOMER1.getPurchases(), parser.fromJsonToList(list.get(2).getValue(), Purchase.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void sortByValue() {
        try {
            Map<String, String> map = parseUtility.getObjectParameters(CustomerTestData.CUSTOMER_JSON1);
            map = MapUtility.sortByValue(map);
            List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
            assertEquals(CustomerTestData.CUSTOMER1.getAge().toString(), list.get(0).getValue());
            assertEquals(CustomerTestData.CUSTOMER1.getName(), list.get(1).getValue());
            assertEquals(CustomerTestData.CUSTOMER1.getPurchases(), parser.fromJsonToList(list.get(2).getValue(), Purchase.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void sortByKey() {
        Map<String, String> map = parseUtility.getObjectParameters(CustomerTestData.CUSTOMER_JSON1);
        map = MapUtility.sortByKey(map);
        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        assertEquals("age", list.get(0).getKey());
        assertEquals("name", list.get(1).getKey());
        assertEquals("purchases", list.get(2).getKey());
    }
}