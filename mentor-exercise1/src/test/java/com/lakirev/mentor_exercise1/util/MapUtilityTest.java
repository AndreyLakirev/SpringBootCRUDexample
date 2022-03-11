package com.lakirev.mentor_exercise1.util;

import com.lakirev.mentor_exercise1.data.CustomerTestData;
import com.lakirev.mentor_exercise1.json.service.JsonParser;
import com.lakirev.mentor_exercise1.json.service.ReflectionJsonParser;
import com.lakirev.mentor_exercise1.json.util.JsonReader;
import com.lakirev.mentor_exercise1.json.util.JsonReaderImpl;
import com.lakirev.mentor_exercise1.customer.model.Purchase;
import com.lakirev.mentor_exercise1.json.util.StringObjectParserImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MapUtilityTest {
    private final JsonReader jsonReader = new JsonReaderImpl();
    private final JsonParser parser = new ReflectionJsonParser(jsonReader, new StringObjectParserImpl());

    @Test
    public void sortByValueSymbolCount() {
        try {
            Map<String, String> map = jsonReader.readObjectParameters(CustomerTestData.CUSTOMER_JSON1.toCharArray());
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
            Map<String, String> map = jsonReader.readObjectParameters(CustomerTestData.CUSTOMER_JSON1.toCharArray());
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
        Map<String, String> map = jsonReader.readObjectParameters(CustomerTestData.CUSTOMER_JSON1.toCharArray());
        map = MapUtility.sortByKey(map);
        List<Map.Entry<String, String>> list = new ArrayList<>(map.entrySet());
        assertEquals("age", list.get(0).getKey());
        assertEquals("name", list.get(1).getKey());
        assertEquals("purchases", list.get(2).getKey());
    }
}