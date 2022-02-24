package com.lakirev.mentor_exercise1.util;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListUtilityTest {
    private static final int COUNT_OF_ELEMENTS = 100;

    private static final int RANDOM_BOUND = 10;

    private final List<Integer> testList = new ArrayList<>();

    @Test
    public void getDistinctElements() {
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        assertEquals(ListUtility.getDistinctElements(testList), testList.stream().distinct().collect(Collectors.toList()));
    }
}