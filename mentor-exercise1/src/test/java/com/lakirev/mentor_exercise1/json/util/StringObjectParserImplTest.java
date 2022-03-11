package com.lakirev.mentor_exercise1.json.util;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class StringObjectParserImplTest {
    private static final StringObjectParser STRING_OBJECT_PARSER = new StringObjectParserImpl();

    @Test
    void parseObject() {
        assertEquals((short) 25, STRING_OBJECT_PARSER.parseObject("25", short.class));
        assertEquals((short) 25, STRING_OBJECT_PARSER.parseObject("25", Short.class));
        assertEquals(25, STRING_OBJECT_PARSER.parseObject("25", int.class));
        assertEquals(25, STRING_OBJECT_PARSER.parseObject("25", Integer.class));
        assertEquals(25L, STRING_OBJECT_PARSER.parseObject("25", long.class));
        assertEquals(25L, STRING_OBJECT_PARSER.parseObject("25", Long.class));
        assertEquals(25f, STRING_OBJECT_PARSER.parseObject("25", float.class));
        assertEquals(25f, STRING_OBJECT_PARSER.parseObject("25", Float.class));
        assertEquals(25d, STRING_OBJECT_PARSER.parseObject("25", double.class));
        assertEquals(25d, STRING_OBJECT_PARSER.parseObject("25", Double.class));
        assertEquals(true, STRING_OBJECT_PARSER.parseObject("true", boolean.class));
        assertEquals(false, STRING_OBJECT_PARSER.parseObject("not true", Boolean.class));
        assertEquals('c', STRING_OBJECT_PARSER.parseObject("c", char.class));
        assertEquals('c', STRING_OBJECT_PARSER.parseObject("c", Character.class));
        assertEquals("testString", STRING_OBJECT_PARSER.parseObject("testString", String.class));
        Date expected = new Date();
        Date actual = (Date) STRING_OBJECT_PARSER.parseObject(STRING_OBJECT_PARSER.FORMATTER.format(expected), Date.class);
        assertEquals(expected.toString(), actual.toString());
    }
}