package org.apache.flink.playgrounds.ops.clickcount.functions;

import java.time.Instant;
import java.util.Random;


public class DataGenerator {
    private static final int SECONDS_BETWEEN_RIDES = 20;
    private static final int NUMBER_OF_COURIERS = 200;
    private static final Instant beginTime = Instant.parse("2020-01-01T12:00:00.00Z");

    private transient long rideId;

    public DataGenerator(long rideId) {
        this.rideId = rideId;
    }

    public Instant startTime() {
        return beginTime.plusSeconds(SECONDS_BETWEEN_RIDES * rideId);
    }

    public Instant endTime() {
        return startTime().plusSeconds(60 * rideDurationMinutes());
    }

    public long courierId() {
        Random rnd = new Random(rideId);
        return 2013000000 + rnd.nextInt(NUMBER_OF_COURIERS);
    }

    public long deliveryId() {
        return courierId();
    }

    public long rideId() {
        return courierId();
    }

    public float startLat() {
        return aFloat((float) (GeoUtils.LAT_SOUTH - 0.1), (float) (GeoUtils.LAT_NORTH + 0.1F));
    }

    public float startLon() {
        return aFloat((float) (GeoUtils.LON_WEST - 0.1), (float) (GeoUtils.LON_EAST + 0.1F));
    }

    public float endLat() {
        return bFloat((float) (GeoUtils.LAT_SOUTH - 0.1), (float) (GeoUtils.LAT_NORTH + 0.1F));
    }

    public float endLon() {
        return bFloat((float) (GeoUtils.LON_WEST - 0.1), (float) (GeoUtils.LON_EAST + 0.1F));
    }

    public short passengerCnt() {
        return (short) aLong(1L, 4L);
    }

    public String paymentType() {
        return (rideId % 2 == 0) ? "CARD" : "CASH";
    }

    public float tip() {
        return aLong(0L, 60L, 10F, 15F);
    }

    public float tolls() {
        return (rideId % 10 == 0) ? aLong(0L, 5L) : 0L;
    }

    public float totalFare() {
        return (float) (3.0 + (1.0 * rideDurationMinutes()) + tip() + tolls());
    }

    private long rideDurationMinutes() {
        return aLong(0L, 600, 20, 40);
    }

    private long aLong(long min, long max) {
        float mean = (min + max) / 2.0F;
        float stddev = (max - min) / 8F;

        return aLong(min, max, mean, stddev);
    }

    private long aLong(long min, long max, float mean, float stddev) {
        Random rnd = new Random(rideId);
        long value;
        do {
            value = (long) Math.round((stddev * rnd.nextGaussian()) + mean);
        } while ((value < min) || (value > max));
        return value;
    }

    private float aFloat(float min, float max) {
        float mean = (min + max) / 2.0F;
        float stddev = (max - min) / 8F;

        return aFloat(rideId, min, max, mean, stddev);
    }

    private float bFloat(float min, float max) {
        float mean = (min + max) / 2.0F;
        float stddev = (max - min) / 8F;

        return aFloat(rideId + 42, min, max, mean, stddev);
    }

    private float aFloat(long seed, float min, float max, float mean, float stddev) {
        Random rnd = new Random(seed);
        float value;
        do {
            value = (float) (stddev * rnd.nextGaussian()) + mean;
        } while ((value < min) || (value > max));
        return value;
    }

}