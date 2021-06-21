package org.apache.flink.playgrounds.ops.clickcount.records;

import org.apache.flink.playgrounds.ops.clickcount.functions.DataGenerator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class TaxiRide {
    public long rideId;
    public boolean isStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss:SSS")
    public Date startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss:SSS")
    public Date endTime;
    public float startLon;
    public float startLat;
    public float endLon;
    public float endLat;
    public short passengerCnt;
    public long taxiId;
    public long driverId;


    public TaxiRide() {
    }

    public TaxiRide(boolean isStart, Date startTime, Date endTime, Long rideId) {
        DataGenerator g = new DataGenerator(rideId);

        this.rideId = rideId;
        this.isStart = isStart;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLon = g.startLon();
        this.startLat = g.startLat();
        this.endLon = g.endLon();
        this.endLat = g.endLat();
        this.passengerCnt = g.passengerCnt();
        this.taxiId = g.taxiId();
        this.driverId = g.driverId();
    }

    public TaxiRide(long rideId, boolean isStart, Date startTime, Date endTime,
                    float startLon, float startLat, float endLon, float endLat,
                    short passengerCnt, long taxiId, long driverId) {
        this.rideId = rideId;
        this.isStart = isStart;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startLon = startLon;
        this.startLat = startLat;
        this.endLon = endLon;
        this.endLat = endLat;
        this.passengerCnt = passengerCnt;
        this.taxiId = taxiId;
        this.driverId = driverId;
    }


    @Override
    public String toString() {

        return rideId + "," +
                (isStart ? "START" : "END") + "," +
                startTime.toString() + "," +
                endTime.toString() + "," +
                startLon + "," +
                startLat + "," +
                endLon + "," +
                endLat + "," +
                passengerCnt + "," +
                taxiId + "," +
                driverId;
    }


    @Override
    public boolean equals(Object other) {
        return other instanceof TaxiRide &&
                this.rideId == ((TaxiRide) other).rideId;
    }

    @Override
    public int hashCode() {
        return (int) this.rideId;
    }
}
