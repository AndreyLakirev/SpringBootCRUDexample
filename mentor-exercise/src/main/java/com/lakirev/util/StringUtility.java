package com.lakirev.util;

import java.util.Random;

public class StringUtility {
    private static final int LEFT_ALPHANUMERIC_SYMBOL_BOUND = 48;

    private static final int LEFT_ALPHABETICAL_SYMBOL_BOUND = 97;

    private static final int RIGHT_SYMBOL_BOUND = 122;

    public static String getReversed(String string) {
        char[] charArray = string.toCharArray();
        for (int i = 0; i < charArray.length / 2; i++) {
            char c = charArray[i];
            charArray[i] = charArray[charArray.length - i - 1];
            charArray[charArray.length - i - 1] = c;
        }
        return new String(charArray);
    }

    public static String generateAlphanumericString(int length) {
        return new Random().ints(LEFT_ALPHANUMERIC_SYMBOL_BOUND, RIGHT_SYMBOL_BOUND + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String generateAlphabeticalString(int length) {
        return new Random().ints(LEFT_ALPHABETICAL_SYMBOL_BOUND, RIGHT_SYMBOL_BOUND + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static String parseGenericTypeName(String fullName) {
        int startIndex = 0;
        char[] chars = fullName.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '<') {
                startIndex = i + 1;
            }
            if (chars[i] == '>') {
                return String.valueOf(chars, startIndex, i - startIndex);
            }
        }
        return "";
    }
}
