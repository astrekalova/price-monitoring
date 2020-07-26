package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SlidingMaxTest {

    SlidingMax slidingMax;

    @BeforeEach
    public void setUp() {
        slidingMax = new SlidingMax(1000, Tick.valueComparatorAsc);
    }

    @Test
    public void testNullWhenNoItems() {
        slidingMax.add(0, new Tick("A", 42, 0));
        assertNull(slidingMax.get(2000));
    }

    @Test
    public void testOneItem() {
        slidingMax.add(0, new Tick("A", 42, 0));
        assertEquals(42, slidingMax.get(500), 0);
    }

    @Test
    public void testSameTimestamp() {
        slidingMax.add(0, new Tick("A", 50, 0));
        slidingMax.add(0, new Tick("A", 100, 0));
        slidingMax.add(0, new Tick("A", 70, 0));
        slidingMax.add(0, new Tick("A", 30, 0));
        assertEquals(100, slidingMax.get(500), 0);
    }

    @Test
    public void testDiscardOldItems() {
        slidingMax.add(0, new Tick("A", 200, 0));
        slidingMax.add(0, new Tick("A", 10, 100));
        slidingMax.add(0, new Tick("A", 100, 200));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1150), 0);
    }

    @Test
    public void testAscending() {
        slidingMax.add(0,  new Tick("A", 100, 100));
        assertEquals(100, slidingMax.get(1000), 0);
        assertNull(slidingMax.get(1150));
    }

    @Test
    public void testDescending() {
        slidingMax.add(0, new Tick("A", 100, 0));
        slidingMax.add(0, new Tick("A", 0, 100));
        assertEquals(100, slidingMax.get(1000), 0);
        assertEquals(0, slidingMax.get(1050), 0);
        assertNull(slidingMax.get(1150));
    }

    @Test
    public void testMixed() throws Exception {
        slidingMax.add(0, new Tick("A", 200, 0));
        slidingMax.add(0, new Tick("A", 0, 100));
        slidingMax.add(0, new Tick("A", 100, 200));
        assertEquals(200, slidingMax.get(1000), 0);
        assertEquals(100, slidingMax.get(1050), 0);
        assertEquals(100, slidingMax.get(1150), 0);
        assertNull(slidingMax.get(1250));
    }
}
