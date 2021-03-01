package com.makinage.kafka.dsl.benchmark;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;

public class Feature {

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public Feature(Long event_id, String event_time, String group_id, Data data) {
        this.event_id = event_id;
        this.event_time = event_time;
        this.group_id = group_id;
        this.data = data;
    }

    public Long event_id;
    public String event_time;
    public String group_id;
    public Data data;

    public Instant getEventTime() {

        try {
            Instant instant = df.parse(event_time).toInstant();
            return instant;
        } catch (ParseException exception) {
            return Instant.EPOCH;
        }
    }

    @Override
    public String toString() {
        return "Feature{" +
                "event_id=" + event_id +
                ", event_time='" + event_time + '\'' +
                ", group_id='" + group_id + '\'' +
                ", data=" + data +
                '}';
    }
}
