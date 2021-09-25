package com.android.bidbatl.Utility;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

/**
 * Created by ASolutions on 6/28/2018.
 */


// 17 19 23 error left
public class FileAPIManager {

   public interface APICallbackInterface {

        void onSuccessFinished(String message);

        void onErrorFinished(String result);
    }

    private static final FileAPIManager ourInstance = new FileAPIManager();

    public static FileAPIManager getInstance() {
        return ourInstance;
    }

    private FileAPIManager() {
    }
    public void callAPI(final File file, final Context context, final APICallbackInterface callback) {
        {
            SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Constants.BASEURL+"upload.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            callback.onSuccessFinished(response);

                            Log.d("Response", response);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Response", error.toString());
                    callback.onErrorFinished(error.toString());
                }

            });
            smr.setShouldCache(false);
            smr.addFile("file", file.getPath());
//            RequestQueue mRequestQueue = Volley.newRequestQueue(RecorderApplication.getInstance());
//            mRequestQueue.add(smr);
            smr.setRetryPolicy(new DefaultRetryPolicy(
                    40000,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
            mRequestQueue.getCache().clear();
            mRequestQueue.add(smr);
        }

    }
}
