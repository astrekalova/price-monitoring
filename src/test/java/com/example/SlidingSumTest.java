package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlidingSumTest {
    SlidingSum slidingSum;

    @BeforeEach
    public void setUp() {
        slidingSum = new SlidingSum(1000);
    }

    @Test
    public void testReturnsZeroIfEmpty() throws Exception {
        assertEquals(0, slidingSum.getSum(0), 0);
        assertEquals(0, slidingSum.getCount(0));
    }

    @Test
    public void testReturnsSum() throws Exception {
        slidingSum.add(0, new Tick("A", 100, 0));
        slidingSum.add(0, new Tick("A", 100, 0));
        assertEquals(200, slidingSum.getSum(500), 0);
        assertEquals(2, slidingSum.getCount(500));
    }

    @Test
    public void shouldDropOldItems() throws Exception {
        slidingSum.add(0, new Tick("A", 100, 0));
        slidingSum.add(0, new Tick("A", 100, 0));
        slidingSum.add(0, new Tick("A", 100, 2000));
        slidingSum.add(0, new Tick("A", 100, 2000));
        // First two added items should be dropped
        assertEquals(200, slidingSum.getSum(2500), 0);
        assertEquals(2, slidingSum.getCount(2500));
    }
}
