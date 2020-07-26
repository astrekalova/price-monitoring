package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class StatisticsTest {

    final int windowSize = 500;
    Statistics statistics;

    @BeforeEach
    public void setUp() {
        statistics = new Statistics(windowSize);
    }

    @Test
    public void testAddStatistics() {
        long now = System.currentTimeMillis();
        statistics.add(now, new Tick("A", 100, now));

        long later = System.currentTimeMillis();
        final Result result = statistics.getStatistics(later);
        assertEquals(100., result.getAvg());
        assertEquals(100., result.getMax());
        assertEquals(100., result.getMin());
        assertEquals(1, result.getCount());
    }

    @Test
    public void testAddStatisticsWithOldTicks() {
        long now = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long outsideTimeWindow = timestamp.toInstant().minusMillis(windowSize).toEpochMilli();

        statistics = new Statistics(windowSize);
        statistics.add(now, new Tick("A", 100, now));
        statistics.add(now, new Tick("A", 200, outsideTimeWindow));

        long later = System.currentTimeMillis();
        final Result result = statistics.getStatistics(later);
        assertEquals(100., result.getAvg());
        assertEquals(100., result.getMax());
        assertEquals(100., result.getMin());
        assertEquals(1, result.getCount());
    }
}