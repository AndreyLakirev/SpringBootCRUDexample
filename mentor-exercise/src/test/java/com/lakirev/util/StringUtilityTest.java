package com.lakirev.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class StringUtilityTest {
    private static final int MINIMUM_SYMBOL_COUNT = 10;

    private static final int RANDOM_BOUND = 50;

    @Test
    public void getReversed() {
        int stringLength = new Random().nextInt(RANDOM_BOUND) + MINIMUM_SYMBOL_COUNT;
        String generatedString = StringUtility.generateAlphanumericString(stringLength);
        Assert.assertEquals(StringUtility.getReversed(generatedString), new StringBuilder(generatedString).reverse().toString());
    }
}