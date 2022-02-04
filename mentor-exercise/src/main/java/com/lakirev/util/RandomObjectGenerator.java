package com.lakirev.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomObjectGenerator {
    public static <T> T generateRandomObject(Class<T> type) {
        return generate(type, new ArrayList<>(), false);
    }

    public static <T> List<T> generateRandomObjects(Class<T> type, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Negative count of objects to generate");
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            result.add(generateRandomObject(type));
        }
        return result;
    }

    private static <T> T generate(Class<T> type, List<Class> classList, boolean ignoreCycling) {
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
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (fieldType.equals(short.class) || fieldType.equals(Short.class)) {
                    field.set(result, (short) new Random().nextInt());
                } else if (fieldType.equals(int.class) || fieldType.equals(Integer.class)) {
                    field.set(result, new Random().nextInt());
                } else if (fieldType.equals(long.class) || fieldType.equals(Long.class)) {
                    field.set(result, new Random().nextLong());
                } else if (fieldType.equals(float.class) || fieldType.equals(Float.class)) {
                    field.set(result, new Random().nextFloat());
                } else if (fieldType.equals(double.class) || fieldType.equals(Double.class)) {
                    field.set(result, new Random().nextDouble());
                } else if (fieldType.equals(boolean.class) || fieldType.equals(Boolean.class)) {
                    field.set(result, new Random().nextBoolean());
                } else if (fieldType.equals(String.class)) {
                    field.set(result, StringUtility.generateAlphabeticalString(50));
                } else if (fieldType.equals(char.class) || fieldType.equals(Character.class)) {
                    field.set(result, new Random().nextInt(26) + 'a');
                } else if (fieldType.equals(Date.class)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(new Random().nextLong());
                    calendar.set(Calendar.YEAR, new Random().nextInt(2022));
                    field.set(result, calendar.getTime());
                } else if (List.class.isAssignableFrom(fieldType)) {
                    Class<?> genericType = Class.forName(StringUtility.parseGenericTypeName(field.getGenericType().getTypeName()));
                    List list = new ArrayList();
                    for (int i = 0; i < new Random().nextInt(1) + 1; i++) {
                        list.add(generate(genericType, classList, true));
                    }
                    field.set(result, list);
                } else {
                    field.set(result, generate(fieldType, classList, false));
                }
                field.setAccessible(isAccessible);
            }
            if (!ignoreCycling) {
                classList.remove(type);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
