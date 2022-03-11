package com.lakirev.mentor_exercise1.json.service;

import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.json.util.JsonReader;
import com.lakirev.mentor_exercise1.json.util.StringObjectParser;
import com.lakirev.mentor_exercise1.util.StringUtility;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.lakirev.mentor_exercise1.json.util.JsonObjectValidator.hasCyclicReference;
import static com.lakirev.mentor_exercise1.json.util.JsonTypeValidator.isSuitableQuotedType;
import static com.lakirev.mentor_exercise1.json.util.JsonTypeValidator.isSuitableType;
import static com.lakirev.mentor_exercise1.json.util.JsonTypeValidator.isSuitableUnquotedType;

/**
 * Class for parsing Java objects to JSON and from JSON
 * Does not accept cyclic reference in objects, also does not support Arrays, only Comparable
 */
@Service
public class ReflectionJsonParser implements JsonParser{
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String CYCLIC_REFERENCE_MESSAGE = "Could not parse json: Unacceptable cyclic reference in target object: ";

    private final JsonReader jsonReader;

    private final StringObjectParser stringObjectParser;

    private boolean writingNullValues = false;

    public ReflectionJsonParser(JsonReader jsonReader, StringObjectParser stringObjectParser) {
        this.jsonReader = jsonReader;
        this.stringObjectParser = stringObjectParser;
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
            Map<String, String> parameters = jsonReader.readObjectParameters(json.toCharArray());
            for (Map.Entry<String, String> kvp : parameters.entrySet()) {
                Field field = type.getDeclaredField(kvp.getKey());
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                Class<?> fieldType = field.getType();
                if (isSuitableType(fieldType)) {
                    field.set(result, stringObjectParser.parseObject(kvp.getValue(), fieldType));
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
        for (String jsonObject : jsonReader.readMajorJsonObjects(json.toCharArray())) {
            list.add(fromJson(jsonObject, type));
        }
        return list;
    }

    public <T> String toJson(T object) throws JsonParseException {
        if (hasCyclicReference(object)) {
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
                if (!writingNullValues) return null;
                result.append("\"");
                result.append(field.getName());
                result.append("\": null");
                return result;
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
            throw new JsonParseException(e);
        }
    }
}