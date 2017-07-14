package com.xome.aparamasi.cab_track;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import com.xome.aparamasi.cab_track.custom.FontEdittext;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.BaseResponse;
import com.xome.aparamasi.cab_track.model.Getnumber;
import com.xome.aparamasi.cab_track.model.alternatenumber;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

public class SettingsActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Getnumber getnumber = new Getnumber();
//        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String name = preferences.getString("user", "");
//        getnumber.setUsername(name);
//        VolleyServiceCalls.getVolleyCallInstance().getUserAttributes(getnumber, new IServiceListener() {
//            @Override
//            public void onSuccess(Object response) {
//                PhoneResponse response1 = VolleyServiceCalls.getGsonParser().fromJson(response.toString(), PhoneResponse.class);
//                 Toast.makeText(SettingsActivity.this, "Number :" + response1.getPhonenumber(), Toast.LENGTH_SHORT).show();
//               SharedPreferences.Editor editor = preferences.edit();
//                editor.putString("alternateno",response1.getPhonenumber());
//                editor.apply();
////                final EditText passTextView = (EditText) findViewById(R.id.editText4);
////                passTextView.setText(response1.AlternateNo);
//            }
//
//            @Override
//            public void onError() {
//            }
//        });

        setContentView(R.layout.activity_settings);
        setupToolbar(getString(R.string.cab_track), NO_NAV_ICON);
        findViewById(R.id.updatenumber1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNumber();
            }
        });
        final FontEdittext contantname = (FontEdittext) findViewById(R.id.editText);
        final FontEdittext contantno = (FontEdittext) findViewById(R.id.editText2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String previousName = preferences.getString("alternatename", "");
        String previousNumber = preferences.getString("alternateno", "");
        contantname.setText(previousName);
        contantno.setText(previousNumber);
    }
    public void updateNumber(){
        final FontEdittext contantname = (FontEdittext) findViewById(R.id.editText);
        final String altname = contantname.getText().toString();
        final FontEdittext contantno = (FontEdittext) findViewById(R.id.editText2);
        final String number = contantno.getText().toString();
        if(altname.equals("") || number.equals("")){
                        if(altname.equals("")){
                            contantname.setError("Please enter a contact name");
                        }
                        else{
                            contantno.setError("Please enter a contact number");
                        }

        }
        else{
            alternatenumber alt = new alternatenumber();
            final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String name = preferences.getString("user", "");

            alt.setName(name);
            alt.setAltname(altname);
            alt.setNumber(number);
            VolleyServiceCalls.getVolleyCallInstance().postnumber(alt, new IServiceListener() {
                @Override
                public void onSuccess(Object response) {
                    VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("alternatename", altname);
                    editor.putString("alternateno", number);
                    Toast.makeText(getBaseContext(), "Emergency Contact has been successfully updated", Toast.LENGTH_SHORT).show();
                    editor.apply();
                    Intent myIntent = new Intent(getBaseContext(), WelcomeActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(myIntent);
                }

                @Override
                public void onError() {
                }
            });
        }
    }
}
