package com.lakirev.json.parser;

import com.lakirev.json.exception.JsonParseException;
import com.lakirev.json.util.JsonParseUtility;
import com.lakirev.util.StringUtility;
import org.springframework.stereotype.Service;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class for parsing Java objects to JSON and from JSON
 * Does not accept cyclic reference in objects, also does not support Arrays, only Lists
 */
@Service
public class CustomJsonParser {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private static final String CYCLIC_REFERENCE_MESSAGE = "Could not parse json: Unacceptable cyclic reference in target object: ";

    private final JsonParseUtility parseUtility;

    private boolean writingNullValues = false;

    public CustomJsonParser(JsonParseUtility parseUtility) {
        this.parseUtility = parseUtility;
    }

    public boolean isWritingNullValues() {
        return writingNullValues;
    }

    public void setWritingNullValues(boolean writingNullValues) {
        this.writingNullValues = writingNullValues;
    }

    public <T> T fromJson(String json, Class<T> type) throws JsonParseException {
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

    public <T> List<T> fromJsonToList(String json, Class<T> type) throws JsonParseException {
        List<T> list = new ArrayList<>();
        for (String jsonObject : parseUtility.getMajorJsonObjects(json)) {
            list.add(fromJson(jsonObject, type));
        }
        return list;
    }

    public <T> String toJson(T object) throws JsonParseException {
        if (hasCyclicReference(object, new ArrayList())) {
            throw new JsonParseException(CYCLIC_REFERENCE_MESSAGE + object);
        }
        return returnJson(object).toString();
    }

    private <T> StringBuilder returnJson(T object) throws JsonParseException {
        StringBuilder result;
        if (object instanceof Iterable) {
            result = new StringBuilder("[");
            Iterator<?> iterator = ((Iterable<?>) object).iterator();
            while (iterator.hasNext()) {
                result.append(returnJson(iterator.next()));
                if (iterator.hasNext()) {
                    result.append(", ");
                }
            }
            result.append("]");
        } else {
            if (isSuitableType(object.getClass())) {
                return new StringBuilder(object.toString());
            }
            result = new StringBuilder("{");
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    boolean isAccessible = fields[i].isAccessible();
                    fields[i].setAccessible(true);
                    Object fieldObject = fields[i].get(object);
                    if (fieldObject == null) {
                        if (writingNullValues) {
                            result.append("\"");
                            result.append(fields[i].getName());
                            result.append("\": null");
                            if (i != fields.length - 1) {
                                result.append(", ");
                            }
                        } else if (i == fields.length - 1 && result.charAt(result.length() - 2) == ',') {
                            result.setLength(result.length() - 2);
                        }
                        continue;
                    }
                    Class<?> type = fields[i].getType();
                    result.append("\"");
                    result.append(fields[i].getName());
                    result.append("\": ");
                    if (isSuitableUnquotedType(type)) {
                        result.append(fields[i].get(object));
                    } else if (isSuitableQuotedType(type)) {
                        result.append("\"");
                        if (type.equals(Date.class)) {
                            result.append(FORMATTER.format(fields[i].get(object)));
                        } else {
                            result.append(fields[i].get(object));
                        }
                        result.append("\"");
                    } else {
                        result.append(returnJson(fields[i].get(object)));
                    }
                    if (i != fields.length - 1) {
                        result.append(", ");
                    }
                    fields[i].setAccessible(isAccessible);
                } catch (Exception e) {
                    throw new JsonParseException(e.getMessage(), e);
                }
            }
            result.append("}");
        }
        return result;
    }

    private <T> boolean hasCyclicReference(T object, List list) {
        if (object == null) {
            return false;
        }
        if (list == null) {
            list = new ArrayList();
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
