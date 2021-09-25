package com.android.bidbatl.Utility

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleRegistry
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyLog
import com.android.volley.toolbox.Volley

class BBApplication : Application(){


    private val activityReferences = 0
    private val isActivityChangingConfigurations = false

    /**
     * Log or request TAG
     */
    val TAG = "VolleyPatterns"

    /**
     * Global request queue for Volley
     */
    private var mRequestQueue: RequestQueue? = null

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private var sInstance: BBApplication? = null
    var mLayout: ConstraintLayout? = null
    private val mLifecycleRegistry: LifecycleRegistry? = null
    override fun onCreate() {
        super.onCreate()
        sInstance = this
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.d("ERR", "TERMINATE")
    }

    /**
     * @return ApplicationController singleton instance
     */
    @Synchronized
    fun getInstance(): BBApplication? {
        return sInstance
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    fun getRequestQueue(): RequestQueue? {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(applicationContext)
        }
        return mRequestQueue
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    fun <T> addToRequestQueue(req: Request<T>, tag: String?) {
        // set the default tag if tag is empty
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        VolleyLog.d("Adding request to queue: %s", req.url)
        getRequestQueue()!!.add(req)
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    fun <T> addToRequestQueue(req: Request<T>) {
        // set the default tag if tag is empty
        req.tag = TAG
        getRequestQueue()!!.add(req)
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    fun cancelPendingRequests(tag: Any?) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }
}