package com.xome.aparamasi.cab_track.model;

/**
 * Created by aparamasi on 8/17/2016.
 */
public class Driver {
    private String DriverName;
    private String DriverPhoneNumber;

    public String getCabNo() {
        return CabNo;
    }

    public void setCabNo(String cabNo) {
        CabNo = cabNo;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverPhoneNumber() {
        return DriverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        DriverPhoneNumber = driverPhoneNumber;
    }

    private String CabNo;
}
