package com.makinage.kafka.dsl.benchmark.utils;

import com.makinage.kafka.dsl.benchmark.Feature;
import com.makinage.kafka.dsl.benchmark.Mean;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

import java.util.HashMap;
import java.util.Map;

public class JsonSerde {

    static Map<String, Object> serdeProps = new HashMap<>();

    public JsonSerde() {
    }

    static Serializer<Feature> FeatureSerializer = new JsonPOJOSerializer<>();
    static Deserializer<Feature> FeatureDeserializer = new JsonPOJODeserializer<>();
    static Serde<Feature> FeatureSerde = null;
    public static Serde<Feature> FeatureSerde() {
        if (FeatureSerde != null)
            return FeatureSerde;
        serdeProps.put("JsonPOJOClass", Feature.class);
        FeatureSerializer.configure(serdeProps, false);
        serdeProps.put("JsonPOJOClass", Feature.class);
        FeatureDeserializer.configure(serdeProps, false);
        FeatureSerde = Serdes.serdeFrom(FeatureSerializer, FeatureDeserializer);
        return FeatureSerde;
    }

    static Serializer<Mean> MeanSerializer = new JsonPOJOSerializer<>();
    static Deserializer<Mean> MeanDeserializer = new JsonPOJODeserializer<>();
    static Serde<Mean> MeanSerde = null;
    public static Serde<Mean> MeanSerde() {
        if (MeanSerde != null)
            return MeanSerde;
        serdeProps.put("JsonPOJOClass", Mean.class);
        MeanSerializer.configure(serdeProps, false);
        serdeProps.put("JsonPOJOClass", Mean.class);
        MeanDeserializer.configure(serdeProps, false);
        MeanSerde = Serdes.serdeFrom(MeanSerializer, MeanDeserializer);
        return MeanSerde;
    }

}
