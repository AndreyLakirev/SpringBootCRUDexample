package com.lakirev.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapUtility {
    public static <K> Map<K, String> sortByValueSymbolCount(Map<K, String> map) {
        Map<K, String> result = new LinkedHashMap<>();
        List<Map.Entry<K, String>> list = new ArrayList<>(map.entrySet()).stream().sorted(Comparator.comparing(a -> a.getValue().length())).collect(Collectors.toList());
        for (Map.Entry<K, String> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet()).stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toList());
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet()).stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
