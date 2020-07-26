package com.example;

import java.util.Objects;

public class Result {

    private Double avg;
    private Double max;
    private Double min;
    private long count;

    public Result(Double avg, Double max, Double min, long count) {
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    public Double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Result{" +
                "avg=" + avg +
                ", max=" + max +
                ", min=" + min +
                ", count=" + count +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return count == result.count &&
                Objects.equals(avg, result.avg) &&
                Objects.equals(max, result.max) &&
                Objects.equals(min, result.min);
    }

    @Override
    public int hashCode() {
        return Objects.hash(avg, max, min, count);
    }
}
