package org.apache.flink.playgrounds.ops.clickcount.records;

import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;


public class DeliveryRideEventSerializationSchema implements KafkaSerializationSchema<DeliveryRide> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private String topic;

    public DeliveryRideEventSerializationSchema(){ }

    public DeliveryRideEventSerializationSchema(String topic) {
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(DeliveryRide deliveryRide, @Nullable Long aLong) {
        try {
            //if topic is null, default topic will be used
            return new ProducerRecord<>(topic, objectMapper.writeValueAsBytes(deliveryRide));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not serialize record: " + deliveryRide, e);
        }
    }
}
