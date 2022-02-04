package com.lakirev.json.exception;

public class JsonParseException extends Exception {
    public JsonParseException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public JsonParseException(String message) {
        super(message);
    }
}
