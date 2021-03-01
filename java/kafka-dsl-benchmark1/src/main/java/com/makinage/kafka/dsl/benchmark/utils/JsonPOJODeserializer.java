package com.makinage.kafka.dsl.benchmark.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

/**
 * The type Json pojo deserializer.
 *
 * @param <T> the type parameter
 */
public class JsonPOJODeserializer<T> implements Deserializer<T> {

    /**
     * The Log.
     */
    static final Logger LOG = LogManager.getLogger(JsonPOJODeserializer.class.getName());

    /**
     * The Gson.
     */
    static final Gson gson = new Gson();

    private Class<T> tClass;

    /**
     * Default constructor needed by Kafka
     */
    public JsonPOJODeserializer() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Map<String, ?> props, boolean isKey) {
        tClass = (Class<T>) props.get("JsonPOJOClass");
    }

    @Override
    public T deserialize(String topic, byte[] bytes) {
        if (bytes == null)
            return null;

        T data;
        try {
            data = gson.fromJson(new String(bytes), tClass);
        } catch (JsonSyntaxException e) {
            LOG.error("Failed to create classOfT from Json (" + new String(bytes) + ")\n");
            throw new SerializationException(e);
        }

        return data;
    }

    @Override
    public void close() {

    }
}
