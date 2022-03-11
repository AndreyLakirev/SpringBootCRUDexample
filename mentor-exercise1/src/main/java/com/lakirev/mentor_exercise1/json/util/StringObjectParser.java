package com.lakirev.mentor_exercise1.json.util;

import java.text.SimpleDateFormat;

public interface StringObjectParser {
    SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    Object parseObject(String value, Class<?> type);
}
