package com.xome.aparamasi.cab_track;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.xome.aparamasi.cab_track.custom.FontTextView;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.BaseResponse;
import com.xome.aparamasi.cab_track.model.Devices;
import com.xome.aparamasi.cab_track.model.Employee;
import com.xome.aparamasi.cab_track.model.Getnumber;
import com.xome.aparamasi.cab_track.model.Trip;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

public class WelcomeActivity extends BaseActivity {
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("displayname", "");
        FontTextView ft = (FontTextView) findViewById(R.id.employeelabel);
        ft.setText("Welcome " + name);
        setupToolbar(getString(R.string.cab_track), NO_NAV_ICON);
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //disableButtons();
               startTrip();
            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //disableButtons();
               onLogOut();
            }
        });
        findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WelcomeActivity.this, SettingsActivity.class));
            }
        });
        findViewById(R.id.driverlabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, DriverActivity.class));
            }
        });

        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                    //Getting the registration token from the intent
                    String token = intent.getStringExtra("token");
                    //Displaying the token as toast
//                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                    //if the intent is not with success then displaying error messages
                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
//                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                }
            }
        };

        //Checking play service is available or not
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if(ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }

    }
    public void enableButtons(){
        FontTextView btnstart = (FontTextView)findViewById(R.id.start);
        btnstart.setEnabled(true);
        FontTextView driverlabel = (FontTextView)findViewById(R.id.driverlabel);
        driverlabel.setEnabled(true);
        FontTextView logout = (FontTextView)findViewById(R.id.logout);
        logout.setEnabled(true);
        FontTextView settings = (FontTextView)findViewById(R.id.settings);
        settings.setEnabled(true);
    }
    public void disableButtons(){
        FontTextView btnstart = (FontTextView)findViewById(R.id.start);
        btnstart.setEnabled(false);
        FontTextView driverlabel = (FontTextView)findViewById(R.id.driverlabel);
        driverlabel.setEnabled(false);
        FontTextView logout = (FontTextView)findViewById(R.id.logout);
        logout.setEnabled(false);
        FontTextView settings = (FontTextView)findViewById(R.id.settings);
        settings.setEnabled(false);
    }
    public void startTrip() {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        final String name = preferences.getString("user", "");
        Getnumber getnumber = new Getnumber();
        getnumber.setUsername(name);
        VolleyServiceCalls.getVolleyCallInstance().getDriverDetails(getnumber, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                com.xome.aparamasi.cab_track.model.Driver response1 = VolleyServiceCalls.getGsonParser().fromJson(response.toString(), com.xome.aparamasi.cab_track.model.Driver.class);

               if (response1 != null)
               {
                    editor.putString("driver", response1.getDriverName());
                    editor.putString("driverphone", response1.getDriverPhoneNumber());
                    editor.putString("cabNo",response1.getCabNo());
                    editor.apply();
                   final String drivername = preferences.getString("driver","");
                   if(drivername.equals("")){
                       Toast.makeText(getBaseContext(), "No Trips generated for you", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       Employee emp = new Employee();
                       emp.setEmpid(name);
                       VolleyServiceCalls.getVolleyCallInstance().getTripSchema(emp, new IServiceListener() {
                           @Override
                           public void onSuccess(Object response) {
                               Trip response1 = VolleyServiceCalls.getGsonParser().fromJson(response.toString(), Trip.class);
                               editor.putString("tripid", response1.getTripID());
                               editor.putString("driverid", response1.getDriverID());
                               editor.apply();
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                           }

                           @Override
                           public void onError() {
                           }
                       });
                   }
                }
                else{
                   Toast.makeText(getBaseContext(), "No Trips generated for you", Toast.LENGTH_SHORT).show();

               }

            }

            @Override
            public void onError() {
            }
        });




    }

    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
    }


    //Unregistering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



    public  void onLogOut(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();



        final String  device = preferences.getString("deviceID", "");
        final String  name = preferences.getString("user", "");
        Devices thisDevice = new Devices();
        thisDevice.setUserID(name);
        thisDevice.setDeviceID(device);

        VolleyServiceCalls.getVolleyCallInstance().removeDevice(thisDevice, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);

            }

            @Override
            public void onError() {
            }
        });
        editor.putString("user","");
        editor.putString("password","");
        editor.putString("deviceID","");
        editor.apply();

//        enableButtons();
        Intent myIntent = new Intent(getBaseContext(),SinginActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);
        finish();

//        Toast.makeText(WelcomeActivity.this, "Log out", Toast.LENGTH_SHORT).show();
    }
}
