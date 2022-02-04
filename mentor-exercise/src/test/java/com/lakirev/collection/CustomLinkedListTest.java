package com.lakirev.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CustomLinkedListTest {
    private static final int COUNT_OF_ELEMENTS = 15;

    private static final int RANDOM_BOUND = 1000;

    private CustomLinkedList<Integer> testList;

    @Before
    public void beforeEachTest() {
        testList = new CustomLinkedList<>();
    }

    @Test
    public void size() {
        int count = new Random().nextInt(COUNT_OF_ELEMENTS);
        for (int i = 0; i < count; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        assertEquals(testList.size(), count);
    }

    @Test
    public void isEmpty() {
        assertTrue(testList.isEmpty());
        testList.add(new Random().nextInt(RANDOM_BOUND));
        assertFalse(testList.isEmpty());
    }

    @Test
    public void add() {
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        assertFalse(testList.isEmpty());
        assertTrue(testList.size() > 0);
    }

    @Test
    public void addToPosition() {
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        Integer randomNumber = new Random().nextInt(RANDOM_BOUND);
        int randomIndex = new Random().nextInt(COUNT_OF_ELEMENTS);
        testList.add(randomNumber, randomIndex);
        assertEquals(randomNumber, testList.get(randomIndex));
    }

    @Test
    public void get() {
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            assertNotNull(testList.get(i));
        }
    }

    @Test
    public void getLast() {
        Integer randomNumber = new Random().nextInt(RANDOM_BOUND);
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        testList.add(randomNumber);
        assertEquals(testList.getLast(), randomNumber);
    }

    @Test
    public void getFirst() {
        Integer randomNumber = new Random().nextInt(RANDOM_BOUND);
        testList.add(randomNumber);
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        assertEquals(testList.getFirst(), randomNumber);
    }

    @Test
    public void swap() {
        int randomIndex1 = new Random().nextInt(COUNT_OF_ELEMENTS);
        int randomIndex2 = new Random().nextInt(COUNT_OF_ELEMENTS);
        for (int i = 0; i < COUNT_OF_ELEMENTS; i++) {
            testList.add(new Random().nextInt(RANDOM_BOUND));
        }
        Integer number1 = testList.get(randomIndex1);
        Integer number2 = testList.get(randomIndex2);
        testList.swap(randomIndex1, randomIndex2);
        assertEquals(number1, testList.get(randomIndex2));
        assertEquals(number2, testList.get(randomIndex1));
    }
}