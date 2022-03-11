package com.lakirev.mentor_exercise1.json.util;

import java.util.Date;

public class JsonTypeValidator {
    public static boolean isSuitableType(Class<?> type) {
        return isSuitableQuotedType(type) || isSuitableUnquotedType(type);
    }

    public static boolean isSuitableUnquotedType(Class<?> type) {
        return short.class.equals(type) || Short.class.equals(type) || int.class.equals(type)
                || Integer.class.equals(type) || long.class.equals(type) || Long.class.equals(type)
                || float.class.equals(type) || Float.class.equals(type) || double.class.equals(type)
                || Double.class.equals(type) || boolean.class.equals(type) || Boolean.class.equals(type);
    }

    public static boolean isSuitableQuotedType(Class<?> type) {
        return String.class.equals(type) || char.class.equals(type) || Character.class.equals(type)
                || Date.class.equals(type);
    }
}
