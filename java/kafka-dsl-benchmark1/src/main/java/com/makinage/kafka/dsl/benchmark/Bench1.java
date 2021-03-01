package com.makinage.kafka.dsl.benchmark;

import com.google.gson.Gson;
import com.makinage.kafka.dsl.benchmark.utils.FeatureTimestampExtractor;
import com.makinage.kafka.dsl.benchmark.utils.JsonSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;


public class Bench1 {

    static final Logger LOG = LogManager.getLogger(Bench1.class.getName());

    static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");

    static final Gson gson = new Gson();

    public static void main(final String[] args) throws Exception, StreamsException {

        String applicationId = "kafka-dsl-benchmark1";
        if (args.length == 1 && !args[0].isEmpty())
            applicationId = args[0];

        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        streamsConfiguration.put(StreamsConfig.CLIENT_ID_CONFIG, applicationId  + "-client");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:29092");
        streamsConfiguration.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, FeatureTimestampExtractor.class);
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp/kafka-streams/bench1");
        streamsConfiguration.put(ProducerConfig.ACKS_CONFIG, "all");
        streamsConfiguration.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        streamsConfiguration.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 50);

        final KafkaStreams streams = createStreams(streamsConfiguration);
        streams.cleanUp();
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                streams.close();
            } catch (final Exception e) {
                // ignored
            }
        }));
    }

    static KafkaStreams createStreams(final Properties streamsConfiguration) {

        final StreamsBuilder builder = new StreamsBuilder();
        final KStream<String, Feature> featuresStream  =
                builder.stream("bench1_feature", Consumed.with(Serdes.String(), JsonSerde.FeatureSerde()));

        featuresStream.groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofHours(2)).grace(Duration.ofMinutes(0)))
                .aggregate(
                        Mean::new,
                        (group_id, newValue, mean) -> {

                            if (mean.event_id == -1L) {
                                // only store the first one
                                mean.event_id = newValue.event_id;
                            }
                            mean.value = ((mean.value * mean.count) + newValue.data.field2) / (mean.count+1);
                            mean.count++;

                            LOG.debug(group_id
                                    + " / " +  dateFormat.format(new Date(newValue.getEventTime().toEpochMilli()))
                                    + " / new value=" + newValue.data.field2
                                    + " / " + mean.toString() + "\n");

                            return mean;
                        },
                        Materialized.with(Serdes.String(), JsonSerde.MeanSerde())
                )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .toStream()
                .map((groupIddWindowed, mean) -> {

                    if (mean == null)
                        return null;

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(groupIddWindowed.key()
                                + " / win[" + dateFormat.format(new Date(groupIddWindowed.window().start()))
                                + "," + dateFormat.format(new Date(groupIddWindowed.window().end())) + "]"
                                + " / " + mean.toString() + "\n");

                    }

                    FeatureOutput feature = new FeatureOutput(mean.event_id,
                            groupIddWindowed.window().start(),
                            groupIddWindowed.key(),
                            mean.value);

                    LOG.info("Stream to bench1_kafka_dsl" + " - " + gson.toJson(groupIddWindowed.key()) + "/ "
                            + gson.toJson(feature) + "\n");

                    return new KeyValue<>(groupIddWindowed.key(), gson.toJson(feature));
                })
                .to("bench1_kafka_dsl");

        return new KafkaStreams(builder.build(), streamsConfiguration);
    }
}
