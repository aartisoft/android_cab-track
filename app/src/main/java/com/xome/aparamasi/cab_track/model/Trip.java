package com.xome.aparamasi.cab_track.model;

/**
 * Created by aparamasi on 8/3/2016.
 */
public class Trip {
    public String getTripID() {
        return TripNo;
    }

    public void setTripID(String tripID)
    {
        this.TripNo = tripID;
    }

    private String TripNo;

    public String getDriverID() {

        return DriverID;
    }

    public void setDriverID(String driverID) {

        this.DriverID = driverID;
    }

    private String DriverID;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    private String Status;
}
