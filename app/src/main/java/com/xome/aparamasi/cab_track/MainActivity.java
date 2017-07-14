package com.xome.aparamasi.cab_track;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.location.LocationSettingsRequest;
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

//import com.google.android.gms.common.Google

public class MainActivity extends BaseActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{
    private static String glatitude = "0";
    private static String glongitude = "0";
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 30;
    private final int REQUEST_PERMISSION_LOCATION_STATE =1;
    private Toolbar toolbar;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationSettingsRequest.Builder builder;
    String mLastUpdateTime;

    private MapView mapView;
    private GoogleMap nMap;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

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
//        mGoogleApiClient.disconnect();
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int status = googleAPI.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            googleAPI.getErrorDialog(this, status, 1).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }


    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

//    @TargetApi(23)
    protected void startLocationUpdates() {

//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION )
//                    == PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ){

//       &&         ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            showExplanation("Permission Needed", "Turn on your GPS", Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_PERMISSION_LOCATION_STATE);

//            ActivityCompat.requestPermissions(this,
//                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION_STATE);
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_LOCATION_STATE);
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
//            } else {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
//            }
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_LOCATION_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
//        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
//
//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COURSE_LOCATION );
//        }
//            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this);
//}

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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
            if(!name.equals("")) {
                String trip = preferences.getString("tripid", "");
                coordinates mCoordinates = new coordinates();
                mCoordinates.setLatitude(lat);
                glatitude = lat;
                glongitude = lng;
                mCoordinates.setLongitude(lng);
                String time = "1369148661";
                if (nMap != null) {
                    nMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 15)); // We will provide our own zoom controls.
                    nMap.getUiSettings().setZoomControlsEnabled(false);
                    nMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
                long timestampLong = Long.parseLong(time) * 1000;
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
            }
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
//        if (mGoogleApiClient.isConnected()) {
//            startLocationUpdates();
//        }
    }
    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("End Trip")
                .setMessage("Are you sure you want to end the trip")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       onStopTrip();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });;
        builder.create().show();
    }

    public void onStopTrip(){


        mGoogleApiClient.disconnect();
        stats user = new stats();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

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
        editor.putString("status","2");
        editor.apply();
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
                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
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