package com.lakirev.mentor_exercise1.json.service;

@FunctionalInterface
public interface ObjectConsumer<T> {
    void consume(T object);
}
