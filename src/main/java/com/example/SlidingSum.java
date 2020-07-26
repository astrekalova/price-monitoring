package com.example;

import java.math.BigDecimal;
import java.util.PriorityQueue;

// Not thread safe, needs synchronization
public class SlidingSum {
    final long windowSize;
    final PriorityQueue<Tick> window = new PriorityQueue<>(Tick.timestampComparator);
    private BigDecimal sum = BigDecimal.ZERO;

    public SlidingSum(long windowSize) {
        this.windowSize = windowSize;
    }

    public void add(long now, Tick tick) {
        window.add(tick);
        sum = sum.add(BigDecimal.valueOf(tick.getPrice()));
        purgeOldItems(now);
    }

    public double getSum(long now) {
        purgeOldItems(now);
        return sum.doubleValue();
    }

    public int getCount(long now) {
        purgeOldItems(now);
        return window.size();
    }

    private void purgeOldItems(long now) {
        while (!window.isEmpty() && window.peek().getTimestamp() + windowSize < now) {
            Tick tick = window.poll();
            sum = sum.subtract(BigDecimal.valueOf(tick.getPrice()));
        }
    }
}
