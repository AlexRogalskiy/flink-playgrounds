package org.apache.flink.playgrounds.ops.clickcount.records;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;


public class TaxiRideEventSerializationSchema implements KafkaSerializationSchema<TaxiRide> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String topic;

    public TaxiRideEventSerializationSchema(){ }

    public TaxiRideEventSerializationSchema(String topic) {
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(TaxiRide taxiRide, @Nullable Long aLong) {
        try {
            //if topic is null, default topic will be used
            return new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(taxiRide));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize record: " + taxiRide, e);
        }
    }
}
