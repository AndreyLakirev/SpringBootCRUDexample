package com.lakirev.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class StringUtilityTest {
    private static final int MINIMUM_SYMBOL_COUNT = 10;

    private static final int RANDOM_BOUND = 50;

    private static final int LEFT_SYMBOL_BOUND = 48;

    private static final int RIGHT_SYMBOL_BOUND = 122;

    @Test
    public void getReversed() {
        int stringLength = new Random().nextInt(RANDOM_BOUND) + MINIMUM_SYMBOL_COUNT;
        Random random = new Random();
        String generatedString = random.ints(LEFT_SYMBOL_BOUND, RIGHT_SYMBOL_BOUND + 1)
                .limit(stringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        Assert.assertEquals(StringUtility.getReversed(generatedString), new StringBuilder(generatedString).reverse().toString());
    }
}