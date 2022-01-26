package com.lakirev.util;

public class StringUtility {
    public static String getReversed(String string) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length / 2; i++) {
            char c = charArray[i];
            charArray[i] = charArray[charArray.length - i - 1];
            charArray[charArray.length - i - 1] = c;
        }
        return new String(charArray);
    }
}
