package com.lakirev.mentor_exercise1.json.util;

import java.util.List;
import java.util.Map;

public interface JsonReader {
    List<String> readMajorJsonObjects(char[] jsonCharArray);

    Map<String, String> readObjectParameters(char[] jsonCharArray);

    int readMajorJsonObjectsWithRemainderIndex(char[] jsonCharArray, List<String> outList);
}
