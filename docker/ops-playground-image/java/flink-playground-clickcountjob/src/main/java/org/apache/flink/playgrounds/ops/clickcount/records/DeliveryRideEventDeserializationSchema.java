package org.apache.flink.playgrounds.ops.clickcount.records;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class DeliveryRideEventDeserializationSchema implements DeserializationSchema<DeliveryRide> {
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DeliveryRide deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, DeliveryRide.class);
    }

    @Override
    public boolean isEndOfStream(DeliveryRide nextElement) {
        return false;
    }

    @Override
    public TypeInformation<DeliveryRide> getProducedType() {
        return TypeInformation.of(DeliveryRide.class);
    }

}
