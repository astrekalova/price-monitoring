package com.example;

public class Statistics {

    final SlidingMax slidingMax;
    final SlidingMax slidingMin;
    final SlidingSum slidingSum;

    public Statistics(long windowSize) {
        this.slidingMax = new SlidingMax(windowSize, Tick.valueComparatorAsc);
        this.slidingMin = new SlidingMax(windowSize, Tick.valueComparatorDesc);
        this.slidingSum = new SlidingSum(windowSize);
    }

    public void add(long now, Tick tick) {
        slidingMax.add(now, tick);
        slidingMin.add(now, tick);
        slidingSum.add(now, tick);
    }

    public Result getStatistics(long now) {
        double sum = slidingSum.getSum(now);
        int count = slidingSum.getCount(now);
        double avg = count > 0 ? sum / count : 0;
        return new Result(
                avg,
                slidingMax.get(now),
                slidingMin.get(now),
                count
        );
    }
}
