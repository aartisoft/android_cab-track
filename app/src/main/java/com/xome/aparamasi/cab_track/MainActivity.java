package com.xome.aparamasi.cab_track;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.BaseResponse;
import com.xome.aparamasi.cab_track.model.coordinates;
import com.xome.aparamasi.cab_track.model.stats;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//import com.google.android.gms.common.Google

public class MainActivity extends BaseActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener{
    private static String glatitude = "13.024753";
    private static String glongitude = "80.177638";
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 *30;
    private static final long FASTEST_INTERVAL = 1000 * 30;
    private Toolbar toolbar;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;

    private MapView mapView;
    private GoogleMap nMap;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onMapReady(GoogleMap map) {
        nMap = map;


        nMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();

    }
    @Override
    public boolean onMyLocationButtonClick() {
       return false;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setContentView(R.layout.activity_main);
        setupToolbar(getString(R.string.cab_track), NO_NAV_ICON);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.endtrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStopTrip();
            }
        });
//        findViewById(R.id.locate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                locateYourself();
//            }
//        });
        findViewById(R.id.emergency).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapEmergency();
            }
        });

        start();
      // toolbar = (Toolbar) findViewById(R.id.tool_bar);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }

    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status =googleAPI.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            googleAPI.getErrorDialog(this,status,1).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }
    private void enableMyLocation() {
         if (nMap != null) {
            // Access to the location has been granted to the app.
            nMap.setMyLocationEnabled(true);
        }
    }

    private void updateUI() {
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());
            String outText = "At Time: " + mLastUpdateTime + "\n" +
                    "Latitude: " + lat + "\n" +
                    "Longitude: " + lng + "\n" +
                    "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                    "Provider: " + mCurrentLocation.getProvider();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String name = preferences.getString("user", "");
            String trip = preferences.getString("tripid", "");
            coordinates mCoordinates = new coordinates();
            mCoordinates.setLatitude(lat);
            glatitude = lat;
            glongitude = lng;
            mCoordinates.setLongitude(lng);
            String time = "1369148661";
            if(nMap != null) {
                nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15)); // We will provide our own zoom controls.
                nMap.getUiSettings().setZoomControlsEnabled(false);
                nMap.getUiSettings().setMyLocationButtonEnabled(true);
            }
            long timestampLong = Long.parseLong(time)*1000;
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(timestampLong * 1000L);
            int seconds = c.get(Calendar.SECOND);
            mCoordinates.setTripNo(trip);
            mCoordinates.setTime(mLastUpdateTime);
            mCoordinates.setUsername(name);

            VolleyServiceCalls.getVolleyCallInstance().postCoordinate(mCoordinates, new IServiceListener() {
                @Override
                public void onSuccess(Object response) {
                    VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);

                }

                @Override
                public void onError() {
                }
            });
        } else {
        }
    }

    @Override
    protected void onPause() {
//        mapView.onResume();
        super.onPause();
    }

    protected void stopLocationUpdates() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
    }
    public void onLowMemory() {
        super.onLowMemory();
//        mapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }
    public void onStopTrip(){


        mGoogleApiClient.disconnect();
        stats user = new stats();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String  name = preferences.getString("user", "");
        user.setUsername(name);
        user.setStatus(""+2);
        VolleyServiceCalls.getVolleyCallInstance().setstatus(user, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);
            }

            @Override
            public void onError() {
            }
        });
        Intent myIntent = new Intent(getBaseContext(),WelcomeActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(myIntent);

    }

    public void start() {
        if(!mGoogleApiClient.isConnected()) {
            createLocationRequest();
            mGoogleApiClient.connect();

        }
        stats user = new stats();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String  name = preferences.getString("user", "");
        user.setUsername(name);
        user.setStatus(""+1);
        VolleyServiceCalls.getVolleyCallInstance().setstatus(user, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);
            }

            @Override
            public void onError() {
              }
        });

    }

    public void onTapEmergency(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String  number1 = preferences.getString("alternateno", "");
        final String  name = preferences.getString("user", "");
        new AlertDialog.Builder(this)
                .setTitle("Emergency")
                .setMessage("Do you really want to contact?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        stats user = new stats();
                        user.setUsername(name);
                        user.setStatus(""+3);
                        VolleyServiceCalls.getVolleyCallInstance().setstatus(user, new IServiceListener() {
                            @Override
                            public void onSuccess(Object response) {
                                VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);
                            }

                            @Override
                            public void onError() {
                            }
                        });
                        String number = "tel:" + number1.trim();
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
                        startActivity(callIntent);

                    }})
                .setNegativeButton(android.R.string.no, null).show();



    }
//    public void locateYourself(){
//        String uri = String.format(Locale.ENGLISH, "geo:%s,%s", glatitude, glongitude);
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//       // Uri location = Uri.parse("geo:0,0?q=DLF IT Park, Chennai, Tamil Nadu, India");
////1600+Amphitheatre+Parkway,+Mountain+View,+California
//      //  Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
//        startActivity(intent);
//    }
//    public  void onLogOut(){
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString("user","");
//        editor.putString("password","");
//        editor.apply();
//        Intent myIntent = new Intent(getBaseContext(),SinginActivity.class);
//        startActivity(myIntent);
//        Toast.makeText(MainActivity.this, "Log out", Toast.LENGTH_SHORT).show();
//    }
//    public  void onConfigure(){
//        Intent myIntent = new Intent(getBaseContext(),SettingsActivity.class);
//        startActivity(myIntent);
//   }
}