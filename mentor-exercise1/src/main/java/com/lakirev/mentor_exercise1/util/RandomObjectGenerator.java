package com.lakirev.mentor_exercise1.util;

import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Utility Class, that allows you to generate random Object of specific Type, filling its fields with random values
 */
@Service
public class RandomObjectGenerator {
    public <T> T generateRandomObject(Class<T> type) {
        return generate(type, new ArrayList<>(), false);
    }

    public <T> List<T> generateRandomObjects(Class<T> type, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Negative count of objects to generate");
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(generateRandomObject(type));
        }
        return result;
    }

    public <T> Callable<List<T>> generateRandomObjectsAsync(Class<T> type, int count) {
        return ArrayList::new;
    }

    private <T> T generate(Class<T> type, List<Class> classList, boolean ignoreCycling) {
        try {
            if (classList.contains(type)) {
                throw new IllegalArgumentException("Unacceptable cyclic reference in type hierarchy detected");
            }
            if (!ignoreCycling) {
                classList.add(type);
            }
            Constructor<T> cons = type.getDeclaredConstructor();
            boolean isConstructorAccessible = cons.isAccessible();
            cons.setAccessible(true);
            T result = cons.newInstance();
            cons.setAccessible(isConstructorAccessible);
            for (Field field : result.getClass().getDeclaredFields()) {
                setFieldWithRandomValue(field, result, classList);
            }
            if (!ignoreCycling) {
                classList.remove(type);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private <T> void setFieldWithRandomValue(Field field, T targetObject, List<Class> classList) {
        try {
            if (field.getName().equals("id")) return;
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Class<?> fieldType = field.getType();
            if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
                field.set(targetObject, (short) new Random().nextInt());
            } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                field.set(targetObject, new Random().nextInt());
            } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                field.set(targetObject, new Random().nextLong());
            } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                field.set(targetObject, new Random().nextFloat());
            } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                field.set(targetObject, new Random().nextDouble());
            } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                field.set(targetObject, new Random().nextBoolean());
            } else if (fieldType.equals(String.class)) {
                field.set(targetObject, StringUtility.generateAlphabeticalString(50));
            } else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
                field.set(targetObject, new Random().nextInt(26) + 'a');
            } else if (fieldType.equals(Date.class)) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(new Random().nextLong());
                calendar.set(Calendar.YEAR, new Random().nextInt(2022));
                field.set(targetObject, calendar.getTime());
            } else if (List.class.isAssignableFrom(fieldType)) {
                Class<?> genericType = Class.forName(StringUtility.parseGenericTypeName(field.getGenericType().getTypeName()));
                List<Object> list = new ArrayList<>();
                for (int i = 0; i < new Random().nextInt(1) + 1; i++) {
                    list.add(generate(genericType, classList, true));
                }
                field.set(targetObject, list);
            } else {
                field.set(targetObject, generate(fieldType, classList, false));
            }
            field.setAccessible(isAccessible);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
