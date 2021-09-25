package com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo

import android.util.Log
import com.bidbatl.dileveryapp.BuildConfig

object MLog {
    fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun d(message: String) {
        if (BuildConfig.DEBUG) {
            Log.d("#### MyTag", message)
        }
    }

    fun e(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

    fun e(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e("MY_TAG ->", message)
        }
    }
}
