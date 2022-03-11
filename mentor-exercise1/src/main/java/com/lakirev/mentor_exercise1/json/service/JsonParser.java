package com.lakirev.mentor_exercise1.json.service;

import java.util.List;

public interface JsonParser {
    <T> String toJson(T object);

    <T> T fromJson(String json, Class<T> type);

    <T> List<T> fromJsonToList(String json, Class<T> type);
}