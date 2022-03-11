package com.lakirev.mentor_exercise1.json.util;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class StringObjectParserImpl implements StringObjectParser {
    @Override
    public Object parseObject(String value, Class<?> type) {
        if (type.equals(short.class) || type.equals(Short.class)) {
            return Short.parseShort(value);
        } else if (type.equals(int.class) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        } else if (type.equals(long.class) || type.equals(Long.class)) {
            return Long.parseLong(value);
        } else if (type.equals(float.class) || type.equals(Float.class)) {
            return Float.parseFloat(value);
        } else if (type.equals(double.class) || type.equals(Double.class)) {
            return Double.parseDouble(value);
        } else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (type.equals(String.class)) {
            return value;
        } else if (type.equals(char.class) || type.equals(Character.class)) {
            return value.toCharArray()[0];
        } else if (type.equals(Date.class)) {
            try {
                return FORMATTER.parse(value);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return value;
    }
}
