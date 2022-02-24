package com.lakirev.mentor_exercise1.util;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringUtilityTest {
    private static final int MINIMUM_SYMBOL_COUNT = 10;

    private static final int RANDOM_BOUND = 50;

    @Test
    public void getReversed() {
        int stringLength = new Random().nextInt(RANDOM_BOUND) + MINIMUM_SYMBOL_COUNT;
        String generatedString = StringUtility.generateAlphanumericString(stringLength);
        assertEquals(StringUtility.getReversed(generatedString), new StringBuilder(generatedString).reverse().toString());
    }
}