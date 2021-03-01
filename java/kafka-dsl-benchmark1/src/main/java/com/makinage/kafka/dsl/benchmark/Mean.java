package com.makinage.kafka.dsl.benchmark;

public class Mean {

    public Mean() {
        value = new Double(0);
        count = new Long(0);
        event_id = new Long(-1);

    }

    public Mean(Double mean, Long count) {
        this.value = mean;
        this.count = count;
        this.event_id = -1L;
    }

    public Double value;
    public Long count;
    public Long event_id;


    @Override
    public String toString() {
        return "Mean{" +
                "value=" + value +
                ", count=" + count +
                ", event_id=" + event_id +
                '}';
    }
}
