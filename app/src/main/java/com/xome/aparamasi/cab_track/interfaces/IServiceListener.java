package com.xome.aparamasi.cab_track.interfaces;

/**
 * Created by aparamasi on 7/8/2016.
 */
public interface IServiceListener<RESPONSE_TYPE> {

    void onSuccess(RESPONSE_TYPE response);

    void onError();

}
