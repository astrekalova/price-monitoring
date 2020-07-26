package com.example;

import java.util.Comparator;
import java.util.TreeSet;

// Not thread safe, needs synchronization
public class SlidingMax {
    final long windowSize;
    final Comparator<Tick> comparator;
    final TreeSet<Tick> window = new TreeSet<>(Tick.timestampComparator);

    public SlidingMax(long windowSize, Comparator<Tick> comparator) {
        this.comparator = comparator;
        this.windowSize = windowSize;
    }

    public void add(long now, Tick tick) {
        if (!window.isEmpty()) {
            Tick nextLaterTick = window.ceiling(tick); // same or later timestamp
            if (nextLaterTick != null && lessThan(tick, nextLaterTick)) {
            } else {
                window.headSet(tick, true).removeIf(i -> lessThan(i, tick));
                window.add(tick);
            }
        } else {
            window.add(tick);
        }
        purgeOldItems(now);
        assert (window.isEmpty() || !lessThan(window.first(), window.last()));
    }

    public Double get(long now) {
        purgeOldItems(now);
        return window.isEmpty() ? null : window.first().getPrice();
    }

    private void purgeOldItems(long now) {
        while (!window.isEmpty() && window.first().getTimestamp() + windowSize < now)
            window.pollFirst();
    }

    boolean lessThan(Tick tick1, Tick tick2) {
        return comparator.compare(tick1, tick2) < 0;
    }

}
