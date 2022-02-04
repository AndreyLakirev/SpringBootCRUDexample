package com.lakirev.json.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class JsonParseUtil {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static List<String> getMajorJsonObjects(String json) {
        char[] chars = json.toCharArray();
        List<String> list = new ArrayList<>();
        int startIndex = 0;
        int bracketCount = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '{') {
                if (bracketCount == 0) {
                    startIndex = i;
                }
                bracketCount++;
            }
            if (chars[i] == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    list.add(String.valueOf(chars, startIndex, i - startIndex));
                }
            }
        }
        return list;
    }

    public static HashMap<String, String> getObjectParameters(String json) {
        char[] chars = json.toCharArray();
        HashMap<String, String> parameters = new HashMap<>();
        String key = null;
        int startIndex = 0;
        int bracketCount = 0;
        int valueType = 0;
        for (int i = 0; i < chars.length; i++) {
            if (key != null) {
                if (valueType == 0 && (chars[i] == ',' || chars[i] == '}')) {
                    parameters.put(key, String.valueOf(chars, startIndex, i - startIndex));
                    key = null;
                    bracketCount = 0;
                    continue;
                }
                if (valueType == 0 && chars[i] == '[') {
                    startIndex = i;
                    valueType = 3;
                    continue;
                }
                if (valueType == 3 && chars[i] == ']') {
                    parameters.put(key, String.valueOf(chars, startIndex, i - startIndex + 1));
                    key = null;
                    bracketCount = 0;
                    valueType = 0;
                    continue;
                }
                if (valueType == 0 && chars[i] == '{') {
                    startIndex = i;
                    valueType = 2;
                    continue;
                }
                if (valueType == 2 && chars[i] == '}') {
                    parameters.put(key, String.valueOf(chars, startIndex, i - startIndex + 1));
                    key = null;
                    bracketCount = 0;
                    valueType = 0;
                    continue;
                }
                if (chars[i] == '"') {
                    if (valueType == 0) {
                        valueType = 1;
                        startIndex = i + 1;
                        continue;
                    }
                    if (valueType == 1) {
                        parameters.put(key, String.valueOf(chars, startIndex, i - startIndex));
                        key = null;
                        bracketCount = 0;
                        valueType = 0;
                        continue;
                    }
                }
            }
            if (chars[i] == '"') {
                if (bracketCount == 0) {
                    startIndex = i + 1;
                    bracketCount++;
                    continue;
                }
                if (bracketCount == 1) { ;
                    key = String.valueOf(chars, startIndex, i - startIndex);
                    bracketCount++;
                    i++;
                    startIndex = i + 1;
                    continue;
                }
            }
        }
        return parameters;
    }

    public static Object generateObjectFromString(String value, Class<?> type) {
        if (type.equals(short.class) || type.equals(Short.class)) {
            return Short.parseShort(value.replaceAll("\\s+",""));
        }
        if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value.replaceAll("\\s+",""));
        }
        if (type.equals(long.class) || type.equals(Long.class)) {
            return Long.parseLong(value.replaceAll("\\s+",""));
        }
        if (type.equals(float.class) || type.equals(Float.class)) {
            return Float.parseFloat(value.replaceAll("\\s+",""));
        }
        if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.parseDouble(value.replaceAll("\\s+",""));
        }
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.parseBoolean(value.replaceAll("\\s+",""));
        }
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(char.class) || type.equals(Character.class)) {
            return value.toCharArray()[0];
        }
        if (type.equals(Date.class)) {
            try {
                return FORMATTER.parse(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }
}
