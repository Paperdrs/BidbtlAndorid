package com.eduisfun.stepapp.ui.utils

import android.content.Context
import android.view.View

class ScreenUtil {
    companion object {
        val instance = ScreenUtil()
    }

    fun setViewHeightDynamicallyByPercentage(context: Context, v: View, percentage: Float) {
        v.layoutParams.height = (context.resources.displayMetrics.heightPixels * percentage).toInt()

    }
    fun setFixedViewHeight( v: View, height: Int) {
        v.layoutParams.height = height

    }
}