package com.xome.aparamasi.cab_track;

/**
 * Created by aparamasi on 8/11/2016.
 */
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.BaseResponse;
import com.xome.aparamasi.cab_track.model.Devices;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

public class GCMRegistrationIntentService extends IntentService {
    //Constants for success and errors
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

    //Class constructor
    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //Registering gcm to the device
        registerGCM();
    }

    private void registerGCM() {
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try {
            //Creating an instanceid
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());

            //Getting the token from the instance id
            token = instanceID.getToken("631849066639", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
            final String token1 = token;

            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String  device = preferences.getString("deviceID", "");
            final String  name = preferences.getString("user", "");
            final Devices thisDevice = new Devices();
            thisDevice.setUserID(name);
            thisDevice.setDeviceID(device);

            VolleyServiceCalls.getVolleyCallInstance().removeDevice(thisDevice, new IServiceListener() {
                @Override
                public void onSuccess(Object response) {
                    VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("deviceID",token1);
                    editor.apply();
                    thisDevice.setUserID(name);
                    thisDevice.setDeviceID(token1);

                    VolleyServiceCalls.getVolleyCallInstance().addDevice(thisDevice, new IServiceListener() {
                        @Override
                        public void onSuccess(Object response) {
                            VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);

                        }

                        @Override
                        public void onError() {
                        }
                    });

                }

                @Override
                public void onError() {
                }
            });





            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

            //Putting the token to the intent
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            //If any error occurred
             registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}