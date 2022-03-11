package com.lakirev.mentor_exercise1.json.exception;

public class JsonParseException extends RuntimeException {
    public JsonParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(Throwable throwable) {
        super(throwable);
    }
}