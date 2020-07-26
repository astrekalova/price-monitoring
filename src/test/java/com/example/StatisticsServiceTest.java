package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatisticsServiceTest {

    private StatisticsService statisticsService;

    @BeforeEach
    public void setUp() {
        statisticsService = new StatisticsService(500, 100);
    }

    @Test
    public void testUpdateStatistics() throws Exception {
        long now = System.currentTimeMillis();
        statisticsService.updateStatistics(new Tick("A", 100, now));
        statisticsService.updateStatistics(new Tick("A", 200, now));
        statisticsService.updateStatistics(new Tick("B", 300, now));
        Thread.sleep(50);

        final Result aggregatedStatistics = statisticsService.getAggregatedStatistics();
        assertEquals(200., aggregatedStatistics.getAvg());
        assertEquals(300., aggregatedStatistics.getMax());
        assertEquals(100., aggregatedStatistics.getMin());
        assertEquals(3, aggregatedStatistics.getCount());

        final Result instrumentAStatistics = statisticsService.getStatisticsForInstrument("A");
        assertEquals(150, instrumentAStatistics.getAvg());
        assertEquals(200., instrumentAStatistics.getMax());
        assertEquals(100., instrumentAStatistics.getMin());
        assertEquals(2, instrumentAStatistics.getCount());

        final Result instrumentBStatistics = statisticsService.getStatisticsForInstrument("B");
        assertEquals(300, instrumentBStatistics.getAvg());
        assertEquals(300, instrumentBStatistics.getMax());
        assertEquals(300., instrumentBStatistics.getMin());
        assertEquals(1, instrumentBStatistics.getCount());
    }

    @Test
    public void testRefreshStatistics() throws Exception {
        long now = System.currentTimeMillis();
        statisticsService.updateStatistics(new Tick("A", 100, now));
        Thread.sleep(50);

        final Result aggregatedStatistics = statisticsService.getAggregatedStatistics();
        assertEquals(100., aggregatedStatistics.getAvg());
        assertEquals(100., aggregatedStatistics.getMax());
        assertEquals(100., aggregatedStatistics.getMin());
        assertEquals(1, aggregatedStatistics.getCount());

        final Result instrumentAStatistics = statisticsService.getStatisticsForInstrument("A");
        assertEquals(100., instrumentAStatistics.getAvg());
        assertEquals(100., instrumentAStatistics.getMax());
        assertEquals(100., instrumentAStatistics.getMin());
        assertEquals(1, instrumentAStatistics.getCount());

        Thread.sleep(1000);

        final Result refreshedAggregatedStatistics = statisticsService.getAggregatedStatistics();
        assertEquals(0, refreshedAggregatedStatistics.getAvg());
        assertNull(refreshedAggregatedStatistics.getMax());
        assertNull(refreshedAggregatedStatistics.getMin());
        assertEquals(0, refreshedAggregatedStatistics.getCount());

        final Result refreshedInstrumentAStatistics = statisticsService.getStatisticsForInstrument("A");
        assertEquals(0, refreshedInstrumentAStatistics.getAvg());
        assertNull(refreshedInstrumentAStatistics.getMax());
        assertNull(refreshedInstrumentAStatistics.getMin());
        assertEquals(0, refreshedInstrumentAStatistics.getCount());
    }
}