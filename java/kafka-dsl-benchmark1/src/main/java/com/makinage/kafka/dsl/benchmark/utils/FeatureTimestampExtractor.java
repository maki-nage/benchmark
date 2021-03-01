package com.makinage.kafka.dsl.benchmark.utils;

import com.makinage.kafka.dsl.benchmark.Feature;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FeatureTimestampExtractor implements TimestampExtractor {

    static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    @Override
    public long extract(ConsumerRecord<Object, Object> record, long l) {
        if (record.value() instanceof Feature) {
            return ((Feature)record.value()).getEventTime().toEpochMilli();
        }
        return 0;
    }
}
