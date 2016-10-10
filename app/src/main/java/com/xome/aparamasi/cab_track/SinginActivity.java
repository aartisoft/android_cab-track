package com.xome.aparamasi.cab_track;

import android.app.ProgressDialog;
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
import com.xome.aparamasi.cab_track.model.PhoneResponse;
import com.xome.aparamasi.cab_track.model.User;
import com.xome.aparamasi.cab_track.volley.VolleyServiceCalls;

public class SinginActivity extends BaseActivity {
    public static ProgressDialog progress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("user", "");
        if(name != "") {
            startActivity(new Intent(SinginActivity.this, WelcomeActivity.class));
            finish();
        }
        setContentView(R.layout.activity_signin);
        progress  = new ProgressDialog(this);
        progress.setTitle("Signing In");
        progress.setMessage("Please wait while loading...");
        progress.setCancelable(false);
        progress.setCanceledOnTouchOutside(false);
        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onSubmit();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progress.dismiss();
        progress=null;
    }

    public void onSubmit(){
        final FontEdittext empIdTextView = (FontEdittext) findViewById(R.id.editText);
        final FontEdittext passTextView = (FontEdittext) findViewById(R.id.editText2);
        final String userId = empIdTextView.getText().toString();
        final String password = passTextView.getText().toString();
        User user = new User();
        user.setUsername(userId);
        user.setPassword(password);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        progress.show();
          VolleyServiceCalls.getVolleyCallInstance().submitLogin(user, new IServiceListener() {
            @Override
            public void onSuccess(Object response) {
                VolleyServiceCalls.getGsonParser().fromJson(response.toString(), BaseResponse.class);
               //----------------- Here It goesse -----------------
                editor.putString("user",userId);
                editor.putString("password",password);
                editor.apply();
                Getnumber getnumber = new Getnumber();
                getnumber.setUsername(userId);
                progress.dismiss();
                VolleyServiceCalls.getVolleyCallInstance().getUserAttributes(getnumber, new IServiceListener() {
                    @Override
                    public void onSuccess(Object response) {
                        PhoneResponse response1 = VolleyServiceCalls.getGsonParser().fromJson(response.toString(), PhoneResponse.class);
                        editor.putString("alternatename",response1.getAlternateName());
                        editor.putString("alternateno",response1.getPhonenumber());
                        editor.putString("displayname",response1.getName());
                        editor.apply();
                        Intent myIntent = new Intent(getBaseContext(),WelcomeActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getBaseContext(), "Wrong Credentials!", Toast.LENGTH_SHORT).show();
                    }
                });

                //------------------------------------------------
            }

            @Override
            public void onError() {
                    progress.dismiss();
                    Toast.makeText(getBaseContext(), "Wrong Credentials!", Toast.LENGTH_SHORT).show();
            }
        });
       }
}
