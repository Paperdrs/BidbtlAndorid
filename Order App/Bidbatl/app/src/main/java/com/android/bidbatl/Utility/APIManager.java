package com.android.bidbatl.Utility;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.bidbatl.Activity.LoginActivity;
import com.android.bidbatl.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.NetworkError;
import com.android.volley.error.NoConnectionError;
import com.android.volley.error.ParseError;
import com.android.volley.error.ServerError;
import com.android.volley.error.TimeoutError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.preference.PowerPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by ASolutions on 6/28/2018.
 */


// 17 19 23 error left
public class APIManager {

    public interface APICallbackInterface {

        void onSuccessFinished(String result);

        void onErrorFinished(String result);
    }

    private static final APIManager ourInstance = new APIManager();

    public static APIManager getInstance() {
        return ourInstance;
    }

    private APIManager() {
    }

    public void callAPI(int method, final String stringUrl, final Map<String, String> param, final Context context, final APIManager.APICallbackInterface callback) {
        StringRequest request = new StringRequest(method, stringUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
//                    if (jsonObject.getInt("code") == 400){
//                        Toast.makeText(context,"Your Session Expired!",Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        PowerPreference.clearAllData();
//                        context.startActivity(intent);
//                    }
//                    else {
                        callback.onSuccessFinished(response);


                   // }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("---------API---------","---------RESPONSE-----------");
                Log.d(stringUrl, response);
                Log.d("---------API---------", param.toString());
//                Log.d(stringUrl,param.toString());


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorString = "";
                if (error instanceof NoConnectionError) {
                    errorString = context.getResources().getString(R.string.no_network);
                }  else if (error instanceof TimeoutError) {
                    errorString = context.getResources().getString(R.string.time_out);
                } else if (error instanceof NetworkError) {
                    errorString = context.getResources().getString(R.string.failed_to_server);
                } else if (error instanceof ParseError) {
                    errorString = context.getResources().getString(R.string.data_parsing_error);

                } else if (error instanceof ServerError) {
                    errorString = context.getResources().getString(R.string.server_wrong);
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                    //errorString = context.getResources().getString(R.string.auth_failure);
                } else {
                    errorString = context.getResources().getString(R.string.error_message);
                }
                Log.e("error is ", "" + error.toString());
                callback.onErrorFinished(errorString);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //Add TOken or Authorization if required
                params.put("token", PowerPreference.getDefaultFile().getString("token"));
                Log.d("TTT",PowerPreference.getDefaultFile().getString("token"));
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            //Pass Your Parameters here
            @Override
            public byte[] getBody() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return new JSONObject(param).toString().getBytes();
            }
        };
        request.setShouldCache(false);
        ApplicationController.getInstance().getRequestQueue().getCache().clear();
        request.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getInstance().addToRequestQueue(request);

    }


}
