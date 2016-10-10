package com.xome.aparamasi.cab_track.volley;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xome.aparamasi.cab_track.AppController;
import com.xome.aparamasi.cab_track.constants.constants;
import com.xome.aparamasi.cab_track.interfaces.IServiceListener;
import com.xome.aparamasi.cab_track.model.Devices;
import com.xome.aparamasi.cab_track.model.Employee;
import com.xome.aparamasi.cab_track.model.Getnumber;
import com.xome.aparamasi.cab_track.model.User;
import com.xome.aparamasi.cab_track.model.alternatenumber;
import com.xome.aparamasi.cab_track.model.coordinates;
import com.xome.aparamasi.cab_track.model.stats;

import java.util.HashMap;
import java.util.Map;

public class VolleyServiceCalls {
    private static VolleyServiceCalls volleyServiceCalls = new VolleyServiceCalls();
    private static Gson gson = new GsonBuilder().create();



    public static VolleyServiceCalls getVolleyCallInstance() {
        return volleyServiceCalls;
    }

    public static void submitLogin(final User user, final IServiceListener iServiceListener) {

        String tag_json_obj = "login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.SUBMIT_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
               // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", user.getUsername());
                params.put("pwd", user.getPassword());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    public static void  postCoordinate(final coordinates mCoordinate, final IServiceListener iServiceListener) {

        String tag_json_obj = "postcoordinate";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.POST_COORDINATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Latitude", mCoordinate.getLatitude());
                params.put("Longitude", mCoordinate.getLongitude());
                params.put("Timestamp",mCoordinate.getTime());
                params.put("EmpName",mCoordinate.getUsername());
                params.put("TripNo",mCoordinate.getTripNo());
                params.put("Content-Type", "application/j2son");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public static void  addDevice(final Devices device, final IServiceListener iServiceListener) {

        String tag_json_obj = "addDevice";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.ADD_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceID",device.getDeviceID());
                params.put("userID",device.getUserID());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public static void  removeDevice(final Devices device, final IServiceListener iServiceListener) {

        String tag_json_obj = "removedevice";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.REMOVE_DEVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deviceID",device.getDeviceID());
                params.put("userID",device.getUserID());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    public static void  setstatus(final stats user, final IServiceListener iServiceListener) {

        String tag_json_obj = "setstatus";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.SET_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Name", user.getUsername());
                params.put("Status", user.getStatus());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public static void  postnumber(final alternatenumber alt, final IServiceListener iServiceListener) {

        String tag_json_obj = "postnumber";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.POST_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("PhoneNo", alt.getNumber());
                params.put("Name", alt.getName());
                params.put("AltName", alt.getAltname());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    public static void  getUserAttributes(final Getnumber alt, final IServiceListener iServiceListener) {

        String tag_json_obj = "getnumber";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.GET_NUMBER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Name", alt.getUsername());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public static void getDriverDetails(final Getnumber alt, final IServiceListener iServiceListener) {

        String tag_json_obj = "getdriver";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.GET_DRIVER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("UserName", alt.getUsername());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    public static void getTripSchema(final Employee employee, final IServiceListener iServiceListener) {

        String tag_json_obj = "gettrip";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                constants.GET_TRIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        iServiceListener.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                iServiceListener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders(){
                Map<String, String> headers = new HashMap<String, String>();
                // String ua=new WebView().getSettings().getUserAgentString();
                headers.put("User-agent","https://chennai.xome.com");
                return headers;
            }
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("EmpName", employee.getEmpid());
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }
    public static Gson getGsonParser() {
        return gson;
    }

}
