package com.lakirev.mentor_exercise1.json.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.lakirev.mentor_exercise1.json.util.JsonTypeValidator.isSuitableType;

public class JsonObjectValidator {
    public static <T> boolean hasCyclicReference(T object) {
        return hasCyclicReference(object, new ArrayList<>());
    }

    private static <T> boolean hasCyclicReference(T object, List<Object> list) {
        if (object == null) {
            return false;
        }
        if (object instanceof Iterable) {
            for (Object iterable : (Iterable<?>) object) {
                if (!isSuitableType(iterable.getClass()) && hasCyclicReference(iterable, list)) {
                    return true;
                }
            }
            return false;
        }
        if (isSuitableType(object.getClass())) {
            return false;
        }
        if (list.contains(object)) {
            return true;
        }
        list.add(object);
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (!isSuitableType(field.getType())) {
                try {
                    boolean isAccessible = field.isAccessible();
                    field.setAccessible(true);
                    Object complexObject = field.get(object);
                    field.setAccessible(isAccessible);
                    if (hasCyclicReference(complexObject, list)) {
                        return true;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Illegal access exception", e);
                }
            }
        }
        list.remove(object);
        return false;
    }
}
