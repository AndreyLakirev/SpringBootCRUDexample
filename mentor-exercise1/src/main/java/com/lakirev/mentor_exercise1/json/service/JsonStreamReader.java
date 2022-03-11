package com.lakirev.mentor_exercise1.json.service;

import com.lakirev.mentor_exercise1.json.exception.JsonParseException;
import com.lakirev.mentor_exercise1.json.util.JsonReader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JsonStreamReader {
    private final JsonReader jsonReader;

    public JsonStreamReader(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    public void consumeJsonStream(InputStream stream, int bufferSize, ObjectConsumer<String> consumer) {
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
                    int remainderIndex = jsonReader.readMajorJsonObjectsWithRemainderIndex(charArray, list);
                    if (!list.isEmpty()) {
                        for (String s : list) {
                            consumer.consume(s);
                        }
                        list.clear();
                    }
                    if (bufferSize - remainderIndex >= 0)
                        System.arraycopy(charArray, remainderIndex, charArray, 0, bufferSize - remainderIndex);
                    index = bufferSize - 1 - remainderIndex;
                }
            }
            jsonReader.readMajorJsonObjectsWithRemainderIndex(Arrays.copyOf(charArray, index + 1), list);
            if (!list.isEmpty()) {
                for (String s : list) {
                    consumer.consume(s);
                }
                list.clear();
            }
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }
}