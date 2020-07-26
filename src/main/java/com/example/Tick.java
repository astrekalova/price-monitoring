package com.example;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

public class Tick {

    @NotNull
    private String instrument;
    @NotNull
    private double price;
    @NotNull
    private long timestamp;

    public Tick(String instrument, double price, long timestamp) {
        this.instrument = instrument;
        this.price = price;
        this.timestamp = timestamp;
    }

    static Comparator<Tick> timestampComparator = Comparator.comparingLong(x -> x.timestamp);
    static Comparator<Tick> valueComparatorAsc = Comparator.comparingDouble(x -> x.price);
    static Comparator<Tick> valueComparatorDesc = valueComparatorAsc.reversed();

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Tick{" +
                "instrument='" + instrument + '\'' +
                ", price=" + price +
                ", timestamp=" + timestamp +
                '}';
    }
}
