package org.apache.flink.playgrounds.ops.clickcount.records;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class TaxiRideEventDeserializationSchema implements DeserializationSchema<TaxiRide> {
    private static final long serialVersionUID = 1L;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TaxiRide deserialize(byte[] message) throws IOException {
        return objectMapper.readValue(message, TaxiRide.class);
    }

    @Override
    public boolean isEndOfStream(TaxiRide nextElement) {
        return false;
    }

    @Override
    public TypeInformation<TaxiRide> getProducedType() {
        return TypeInformation.of(TaxiRide.class);
    }

}
