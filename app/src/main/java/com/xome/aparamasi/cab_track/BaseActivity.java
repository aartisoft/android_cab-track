package com.xome.aparamasi.cab_track;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    private AlertDialog noInternetDialog;
    protected final static int NO_NAV_ICON = 0;
    private Toolbar toolbar;
   // protected ImageView toolbarImageView;
    private ProgressDialog pDialog;
//    private BroadcastReceiver mRegistrationBroadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

//        //Initializing our broadcast receiver
//        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//
//            //When the broadcast received
//            //We are sending the broadcast from GCMRegistrationIntentService
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                //If the broadcast has received with success
//                //that means device is registered successfully
//                if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
//                    //Getting the registration token from the intent
//                    String token = intent.getStringExtra("token");
//                    //Displaying the token as toast
//                    Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();
//
//                    //if the intent is not with success then displaying error messages
//                } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
//                    Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
//                }
//            }
//        };
//
//        //Checking play service is available or not
//        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
//
//        //if play service is not available
//        if(ConnectionResult.SUCCESS != resultCode) {
//            //If play service is supported but not installed
//            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                //Displaying message that play service is not installed
//                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
//                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
//
//                //If play service is not supported
//                //Displaying an error message
//            } else {
//                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
//            }
//
//            //If play service is available
//        } else {
//            //Starting intent to register device
//            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
//            startService(itent);
//        }


    }

//    //Registering receiver on activity resume
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.w("MainActivity", "onResume");
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
//    }
//
//
//    //Unregistering receiver on activity paused
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.w("MainActivity", "onPause");
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//    }
//

    protected void showProgress() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.show();
        }
    }

    protected void hideProgress() {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    public void showNoInternetDialog() {

        if (noInternetDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.no_internet_dialog_msg)
                    .setTitle(R.string.no_internet_connection)
                    .setPositiveButton(R.string.open_settings, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivity(new Intent(Settings.ACTION_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });

            noInternetDialog = builder.create();
            noInternetDialog.setCancelable(false);
            noInternetDialog.show();
        } else {
            if (!noInternetDialog.isShowing()) {
                noInternetDialog.show();
            }
        }
    }


    protected void setupToolbar(String title, int navIcon) {
        setupToolbar(title, null, navIcon);
    }

    public void updateToolbarBackgroundColor(int color) {
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
    }

    protected void setupToolbar(String title, String subtitle, int navIcon) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbarImageView = (ImageView) toolbar.findViewById(R.id.toolbarImage);
        if (toolbar != null) {
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText(title);
            toolbar.setTitle("");

            if (subtitle != null) {
                toolbar.setSubtitle(subtitle);
            }
            if (navIcon != NO_NAV_ICON) {
                toolbar.setNavigationIcon(navIcon);
            }

            // Have to inform about toolbar to actionbar before setting NavigationClickListener to it.
            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onToolbarNavButtonClicked();
                }
            });
        }
    }

    protected void onToolbarNavButtonClicked() {
        finish();
    }


    protected void showErrorDialog(Context ctx, String message) {
        new AlertDialog.Builder(ctx)
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }

}

