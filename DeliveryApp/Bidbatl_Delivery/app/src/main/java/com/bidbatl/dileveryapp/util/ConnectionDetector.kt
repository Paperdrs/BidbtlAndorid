package com.bidbatl.dileveryapp.util

import android.content.Context
import android.net.ConnectivityManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConnectionDetector @Inject constructor() {
    fun init(): Boolean {
        return true
    }

    fun isInternetOn(context: Context?): Boolean {
        val cm = (context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val activeNetwork = cm.activeNetworkInfo
        return if (activeNetwork != null) {
            when (activeNetwork.type) {
                ConnectivityManager.TYPE_WIFI -> {
                    true
                }
                ConnectivityManager.TYPE_MOBILE -> {
                    true
                }
                else -> activeNetwork.type == ConnectivityManager.TYPE_MOBILE_DUN
            }
        } else {
            false
        }
    }
}