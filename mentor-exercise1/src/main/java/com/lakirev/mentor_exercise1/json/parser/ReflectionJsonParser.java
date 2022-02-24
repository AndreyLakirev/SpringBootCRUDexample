package com.lakirev.mentor_exercise1.json.parser;

import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.json.util.JsonParseUtility;
import com.lakirev.mentor_exercise1.util.StringUtility;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class for parsing Java objects to JSON and from JSON
 * Does not accept cyclic reference in objects, also does not support Arrays, only Lists
 */
@Service
public class ReflectionJsonParser {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String CYCLIC_REFERENCE_MESSAGE = "Could not parse json: Unacceptable cyclic reference in target object: ";

    private final JsonParseUtility parseUtility;

    private boolean writingNullValues = false;

    public ReflectionJsonParser(JsonParseUtility parseUtility) {
        this.parseUtility = parseUtility;
    }

    public boolean isWritingNullValues() {
        return writingNullValues;
    }

    public void setWritingNullValues(boolean writingNullValues) {
        this.writingNullValues = writingNullValues;
    }

    public <T> T fromJson(String json, Class<T> type) {
        try {
            Constructor<T> cons = type.getDeclaredConstructor();
            boolean isConstructorAccessible = cons.isAccessible();
            cons.setAccessible(true);
            T result = cons.newInstance();
            cons.setAccessible(isConstructorAccessible);
            Map<String, String> parameters = parseUtility.getObjectParameters(json);
            for (Map.Entry<String, String> kvp : parameters.entrySet()) {
                Field field = type.getDeclaredField(kvp.getKey());
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (isSuitableType(fieldType)) {
                    field.set(result, parseUtility.generateObjectFromString(kvp.getValue(), fieldType));
                } else if (List.class.isAssignableFrom(fieldType)) {
                    field.set(result, fromJsonToList(kvp.getValue(), Class.forName(StringUtility.parseGenericTypeName(field.getGenericType().getTypeName()))));
                } else {
                    field.set(result, fromJson(kvp.getValue(), fieldType));
                }
                field.setAccessible(isAccessible);
            }
            return result;
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    public <T> List<T> fromJsonToList(String json, Class<T> type) {
        List<T> list = new ArrayList<>();
        for (String jsonObject : parseUtility.getMajorJsonObjects(json.toCharArray())) {
            list.add(fromJson(jsonObject, type));
        }
        return list;
    }

    public <T> void fromJsonStream(InputStream stream, int bufferSize, Class<T> type, ObjectConsumer<T> consumer) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            int c;
            char[] charArray = new char[bufferSize];
            List<String> list = new ArrayList<>();
            int index;
            for (index = 0; index < bufferSize; index++) {
                c = reader.read();
                if (c == -1) break;
                charArray[index] = (char) c;
                if (index == bufferSize - 1) {
                    int remainderIndex = parseUtility.writeMajorJsonObjectsWithRemainderIndex(charArray, list);
                    if (!list.isEmpty()) {
                        for (String s : list) {
                            consumer.consume(fromJson(s, type));
                        }
                        list.clear();
                    }
                    if (bufferSize - remainderIndex >= 0)
                        System.arraycopy(charArray, remainderIndex, charArray, 0, bufferSize - remainderIndex);
                    index = bufferSize - 1 - remainderIndex;
                }
            }
            parseUtility.writeMajorJsonObjectsWithRemainderIndex(Arrays.copyOf(charArray, index + 1), list);
            if (!list.isEmpty()) {
                for (String s : list) {
                    consumer.consume(fromJson(s, type));
                }
                list.clear();
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public <T> String toJson(T object) throws JsonParseException {
        if (hasCyclicReference(object, new ArrayList<>())) {
            throw new JsonParseException(CYCLIC_REFERENCE_MESSAGE + object);
        }
        return returnJson(object).toString();
    }

    private <T> StringBuilder returnJson(T object) throws JsonParseException {
        if (object instanceof Iterable) {
            return getJsonArray(((Iterable<?>) object).iterator());
        } else {
            if (isSuitableType(object.getClass())) {
                return new StringBuilder(object.toString());
            }
            return getJsonObject(object);
        }
    }

    private <T> StringBuilder getJsonObject(T object) {
        StringBuilder result = new StringBuilder("{");
        Field[] fields = object.getClass().getDeclaredFields();
        boolean hasOneNotNullValue = false;
        for (Field field : fields) {
            StringBuilder fieldSequence = getFieldJsonSequence(field, object);
            if (fieldSequence == null) continue;
            if (hasOneNotNullValue) {
                result.append(", ");
            }
            hasOneNotNullValue = true;
            result.append(getFieldJsonSequence(field, object));
        }
        result.append("}");
        return result;
    }

    private StringBuilder getJsonArray(Iterator<?> iterator) {
        StringBuilder result = new StringBuilder("[");
        while (iterator.hasNext()) {
            result.append(returnJson(iterator.next()));
            if (iterator.hasNext()) {
                result.append(", ");
            }
        }
        result.append("]");
        return result;
    }

    private <T> StringBuilder getFieldJsonSequence(Field field, T targetObject) {
        try {
            StringBuilder result = new StringBuilder();
            boolean isAccessible = field.isAccessible();
            field.setAccessible(true);
            Object fieldObject = field.get(targetObject);
            if (fieldObject == null) {
                if (writingNullValues) {
                    result.append("\"");
                    result.append(field.getName());
                    result.append("\": null");
                    return result;
                }
                return null;
            }
            Class<?> type = field.getType();
            result.append("\"");
            result.append(field.getName());
            result.append("\": ");
            if (isSuitableUnquotedType(type)) {
                result.append(field.get(targetObject));
            } else if (isSuitableQuotedType(type)) {
                result.append("\"");
                if (type.equals(Date.class)) {
                    result.append(FORMATTER.format(field.get(targetObject)));
                } else {
                    result.append(field.get(targetObject));
                }
                result.append("\"");
            } else {
                result.append(returnJson(field.get(targetObject)));
            }
            field.setAccessible(isAccessible);
            return result;
        } catch (Exception e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    private <T> boolean hasCyclicReference(T object, List<Object> list) {
        if (object == null) {
            return false;
        }
        if (list == null) {
            list = new ArrayList<>();
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

    private boolean isSuitableType(Class<?> type) {
        return isSuitableQuotedType(type) || isSuitableUnquotedType(type);
    }

    private boolean isSuitableUnquotedType(Class<?> type) {
        return short.class.equals(type) || Short.class.equals(type) || int.class.equals(type)
                || Integer.class.equals(type) || long.class.equals(type) || Long.class.equals(type)
                || float.class.equals(type) || Float.class.equals(type) || double.class.equals(type)
                || Double.class.equals(type) || boolean.class.equals(type) || Boolean.class.equals(type);
    }

    private boolean isSuitableQuotedType(Class<?> type) {
        return String.class.equals(type) || char.class.equals(type) || Character.class.equals(type)
                || Date.class.equals(type);
    }
}
