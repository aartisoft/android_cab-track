package com.xome.aparamasi.cab_track;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.xome.aparamasi.cab_track.custom.FontEdittext;
import com.xome.aparamasi.cab_track.custom.FontTextView;

public class QRActivity extends BaseActivity {

    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the main content layout of the Activity
//        Intent myIntent = new Intent(getBaseContext(),MainActivity.class);
//        startActivity(myIntent);
        setContentView(R.layout.activity_qr);
        setupToolbar(getString(R.string.cab_track), NO_NAV_ICON);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String driverName = preferences.getString("driver", "");
        final String driverPhone = preferences.getString("driverphone", "");

        final String cab = preferences.getString("cabNo", "");

        FontTextView ft = (FontTextView) findViewById(R.id.driverlabel);
        ft.setText("Driver Name : " + driverName + "\n" + "Driver Phone :"  + driverPhone + "\n Cab Number : " + cab);
//        findViewById(R.id.driverlabel).
        findViewById(R.id.qrscan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQR();
            }
        });
        findViewById(R.id.valtrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateTripId();
            }
        });
    }

    public void validateTripId(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("tripid", "");
        final FontEdittext tripTextView = (FontEdittext) findViewById(R.id.editText);
        final String tripid = tripTextView.getText().toString();
        if(tripid.equals(name)){
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast toast1 = Toast.makeText(getBaseContext(), "Please check your cab details with Admin",Toast.LENGTH_LONG);
            toast1.show();
        }
    }
    //product qr code mode
    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(QRActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                toast.show();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                String name = preferences.getString("driverid", "");
                if(contents.equals(name)) {
                    Intent myIntent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(myIntent);
                }
                else{
                    Toast toast1 = Toast.makeText(getBaseContext(), "Please check your cab details with Admin",Toast.LENGTH_LONG);
                    toast1.show();
                }
            }
        }
    }
}