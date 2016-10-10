package com.xome.aparamasi.cab_track.model;

/**
 * Created by aparamasi on 7/18/2016.
 */
public class PhoneResponse {
    public String getPhonenumber() {
        return AlternateNo;
    }

    public void setAlternateNo(String alternateNo) {
        AlternateNo = alternateNo;
    }

    public void setAlternateName(String alternateName) {
        AlternateName = alternateName;
    }

    public void setName(String name) {
        Name = name;
    }

    private String AlternateNo;

    public String getAlternateName() {
        return AlternateName;
    }

    private String AlternateName;
    public String getName() {
        return Name;
    }

    private  String Name;

}
