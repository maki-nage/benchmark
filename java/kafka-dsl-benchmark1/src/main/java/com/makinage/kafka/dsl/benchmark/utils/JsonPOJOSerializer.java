package com.makinage.kafka.dsl.benchmark.utils;


import com.google.gson.Gson;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * The type Json pojo serializer.
 *
 * @param <T> the type parameter
 */
public class JsonPOJOSerializer<T> implements Serializer<T> {

    /**
     * The Log.
     */
    static final Logger LOG = LogManager.getLogger(JsonPOJOSerializer.class.getName());

    /**
     * The Gson.
     */
    static final Gson gson = new Gson();

    /**
     * Default constructor needed by Kafka
     */
    public JsonPOJOSerializer() {
    }

    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null)
            return null;

        try {
            return gson.toJson(data).getBytes();
        } catch (Exception e) {
            throw new SerializationException("Error serializing JSON message", e);
        }
    }

    @Override
    public void close() {
    }
}
