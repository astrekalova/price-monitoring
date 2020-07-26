package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatisticsService {

    private final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private final Statistics aggregatedStatistics;
    private volatile Result aggregatedLatestResult;
    private final Map<String, Statistics> instrumentStatistics = new ConcurrentHashMap<>();
    private final Map<String, Result> instrumentLatestResult = new ConcurrentHashMap<>();

    private final long windowsSizeMs;
    private final long refreshSizeMs;

    public StatisticsService(long windowsSizeMs, long refreshSizeMs) {
        this.aggregatedStatistics = new Statistics(windowsSizeMs);
        this.windowsSizeMs = windowsSizeMs;
        this.refreshSizeMs = refreshSizeMs;
        log.info("Starting StatisticsAsync with window size of {} ms", windowsSizeMs);
        new Thread(new Refresher()).start();
    }

    public void updateStatistics(Tick tick) {
        long now = System.currentTimeMillis();
        final String instrument = tick.getInstrument();
        final Statistics statisticsForInstrument = instrumentStatistics.get(instrument);

        synchronized (this.aggregatedStatistics) {
            this.aggregatedStatistics.add(now, tick);
            // Immediately refresh
            aggregatedLatestResult = this.aggregatedStatistics.getStatistics(now);
        }

        if (statisticsForInstrument == null) {
            final Statistics newStatisticsForInstrument = new Statistics(this.windowsSizeMs);
            newStatisticsForInstrument.add(now, tick);
            instrumentStatistics.put(instrument, newStatisticsForInstrument);
        } else {
            statisticsForInstrument.add(now, tick);
            instrumentStatistics.put(instrument, statisticsForInstrument);
        }
        final Result latestInstrumentStatistics = instrumentStatistics.get(instrument).getStatistics(now);
        instrumentLatestResult.put(instrument, latestInstrumentStatistics);
    }

    public Result getAggregatedStatistics() {
        return aggregatedLatestResult;
    }

    public Result getStatisticsForInstrument(String instrumentIdentifier) {
        return instrumentLatestResult.get(instrumentIdentifier);
    }

    private class Refresher implements Runnable {
        public void run() {
            log.info("Refresher: started");
            while (true) {
                try {
                    Thread.sleep(refreshSizeMs);
                    long now = System.currentTimeMillis();
                    synchronized (aggregatedStatistics) {
                        aggregatedLatestResult = aggregatedStatistics.getStatistics(now);
                    }
                    instrumentStatistics.forEach((instrument, statistics) -> {
                        final Result latestInstrumentStatistics = statistics.getStatistics(now);
                        instrumentLatestResult.put(instrument, latestInstrumentStatistics);
                    });
                    log.debug("Refresher: refreshed the latest result: {}", aggregatedLatestResult);
                } catch (Exception e) {
                    log.error("Refresher: error while refreshing the latest result.", e);
                }
            }
        }
    }
}
