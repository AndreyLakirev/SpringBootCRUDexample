package com.lakirev.mentor_exercise1.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class QuickSortUtilityTest {
    @Test
    public void quickSortTest() {
        List<Integer> testList = Arrays.asList(5, 3, 15, 25, 144, 954, -515, 97, -238, 0, 5, 0, 2, 7);
        List<Integer> expected = testList.stream().sorted().collect(Collectors.toList());
        Assertions.assertEquals(expected, QuickSortUtility.quickSort(testList));
    }
}