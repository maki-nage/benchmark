package com.makinage.kafka.dsl.benchmark;

public class FeatureOutput {

    public FeatureOutput(Long event_id, Long event_time, String group_id, Double field2_mean) {
        this.event_id = event_id;
        this.event_time = event_time;
        this.group_id = group_id;
        this.field2_mean = field2_mean;
    }

    public Long event_id;
    public Long event_time;
    public String group_id;
    public Double  field2_mean;

    @Override
    public String toString() {
        return "FeatureOutput{" +
                "event_id=" + event_id +
                ", event_time=" + event_time +
                ", group_id='" + group_id + '\'' +
                ", field2_mean=" + field2_mean +
                '}';
    }
}
