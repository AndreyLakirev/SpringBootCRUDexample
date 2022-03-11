package com.lakirev.mentor_exercise1.json.util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class, that provides supporting methods to split, organize JSON and to convert JSON to basic data types
 */
@Service
public class JsonReaderImpl implements JsonReader{
    public List<String> readMajorJsonObjects(char[] jsonCharArray) {
        List<String> list = new ArrayList<>();
        int startIndex = 0;
        int bracketCount = 0;
        for (int i = 0; i < jsonCharArray.length; i++) {
            if (jsonCharArray[i] == '{') {
                if (bracketCount == 0) {
                    startIndex = i;
                }
                bracketCount++;
            } else if (jsonCharArray[i] == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    list.add(String.valueOf(jsonCharArray, startIndex, i - startIndex + 1));
                }
            }
        }
        return list;
    }

    public int readMajorJsonObjectsWithRemainderIndex(char[] jsonCharArray, List<String> outList) {
        int startIndex = 0;
        int bracketCount = 0;
        for (int i = 0; i < jsonCharArray.length; i++) {
            if (jsonCharArray[i] == '{') {
                if (bracketCount == 0) {
                    startIndex = i;
                }
                bracketCount++;
            } else if (jsonCharArray[i] == '}') {
                bracketCount--;
                if (bracketCount == 0) {
                    outList.add(String.valueOf(jsonCharArray, startIndex, i - startIndex + 1));
                }
            }
        }
        if (bracketCount > 0) {
            return startIndex;
        }
        return 0;
    }

    public Map<String, String> readObjectParameters(char[] jsonCharArray) {
        Map<String, String> parameters = new LinkedHashMap<>();
        String key = null;
        int startIndex = 0;
        int bracketCount = 0;
        int valueType = 0;
        for (int i = 0; i < jsonCharArray.length; i++) {
            if (key != null) {
                if (valueType == 0 && (jsonCharArray[i] == ',' || jsonCharArray[i] == '}')) {
                    parameters.put(key, String.valueOf(jsonCharArray, startIndex, i - startIndex).replaceAll("\\s+",""));
                    key = null;
                    bracketCount = 0;
                } else if (valueType == 0 && jsonCharArray[i] == '[') {
                    startIndex = i;
                    valueType = 3;
                } else if (valueType == 3 && jsonCharArray[i] == ']') {
                    parameters.put(key, String.valueOf(jsonCharArray, startIndex, i - startIndex + 1));
                    key = null;
                    bracketCount = 0;
                    valueType = 0;
                } else if (valueType == 0 && jsonCharArray[i] == '{') {
                    startIndex = i;
                    valueType = 2;
                } else if (valueType == 2 && jsonCharArray[i] == '}') {
                    parameters.put(key, String.valueOf(jsonCharArray, startIndex, i - startIndex + 1));
                    key = null;
                    bracketCount = 0;
                    valueType = 0;
                } else if (jsonCharArray[i] == '"') {
                    if (valueType == 0) {
                        valueType = 1;
                        startIndex = i + 1;
                    } else if (valueType == 1) {
                        parameters.put(key, String.valueOf(jsonCharArray, startIndex, i - startIndex));
                        key = null;
                        bracketCount = 0;
                        valueType = 0;
                    }
                }
            } else if (jsonCharArray[i] == '"') {
                if (bracketCount == 0) {
                    startIndex = i + 1;
                    bracketCount++;
                } else if (bracketCount == 1) { ;
                    key = String.valueOf(jsonCharArray, startIndex, i - startIndex);
                    bracketCount++;
                    i++;
                    startIndex = i + 1;
                }
            }
        }
        return parameters;
    }
}
