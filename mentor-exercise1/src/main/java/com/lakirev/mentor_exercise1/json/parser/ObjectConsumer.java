package com.lakirev.mentor_exercise1.json.parser;

@FunctionalInterface
public interface ObjectConsumer<T> {
    void consume(T object);
}
