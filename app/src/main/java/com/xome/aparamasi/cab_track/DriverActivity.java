package com.xome.aparamasi.cab_track;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.xome.aparamasi.cab_track.custom.FontTextView;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.Getnumber;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

public class DriverActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();
        String name = preferences.getString("user", "");
        Getnumber getnumber = new Getnumber();
        getnumber.setUsername(name);
        setContentView(R.layout.activity_driver);
        setupToolbar(getString(R.string.cab_track), NO_NAV_ICON);
        VolleyServiceCalls.getVolleyCallInstance().getDriverDetails(getnumber, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                com.xome.aparamasi.cab_track.model.Driver response1 = VolleyServiceCalls.getGsonParser().fromJson(response.toString(), com.xome.aparamasi.cab_track.model.Driver.class);

                if (response1 != null){
                    editor.putString("driver", response1.getDriverName());
                    editor.putString("driverphone", response1.getDriverPhoneNumber());
                    editor.putString("cabNo", response1.getCabNo());
                    editor.apply();

                    final String driverName = preferences.getString("driver", "");
                    final String driverPhone = preferences.getString("driverphone", "");
                    final String cab = preferences.getString("cabNo", "");
                    FontTextView ft = (FontTextView) findViewById(R.id.driverlabel);

                    if (driverName.equals("")) {
                        ft.setText("No Trips Generated for you");

                    } else {
                        ft.setText("Driver Name : " + driverName + "\n" + "Driver Phone :" + driverPhone + "\n Cab Number : " + cab);
                    }
                }
                else{

                    FontTextView ft = (FontTextView) findViewById(R.id.driverlabel);
                    ft.setText("No Trips Generated for you");

                }
            }

            @Override
            public void onError() {
            }
        });

    }

}
