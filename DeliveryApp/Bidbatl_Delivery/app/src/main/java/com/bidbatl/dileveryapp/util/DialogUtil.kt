package com.bidbatl.dileveryapp.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.widget.TextView
import com.bidbatl.dileveryapp.R


fun Context.displaySingleChoiceDialog(title: String, options: Array<String>, currentSelection: Int, operation: (which: Int) -> Unit) {
    val optionsDialog = AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
    optionsDialog.setTitle(title)
    optionsDialog.setSingleChoiceItems(options, currentSelection) { dialog, which ->
        run {
            operation(which)
            dialog.dismiss()
        }
    }
    optionsDialog.create().show()
}

fun Context.createAlertDialogWithTwoButtons(title: String?, message: String?,
    positiveButtonText: String?,
    positiveListener: DialogInterface.OnClickListener?,
    negativeButtonText: String?,
    negativeListener: DialogInterface.OnClickListener?
): AlertDialog? {
    val myMsg = TextView(this)
    myMsg.text = "Central"
    myMsg.textSize = 20.0F
    myMsg.setPadding(0,40,0,40)
    myMsg.gravity = Gravity.CENTER_HORIZONTAL
    return AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
        .setTitle(title)
        .setCustomTitle(myMsg)
        .setPositiveButton(positiveButtonText, positiveListener)
        .setNegativeButton(negativeButtonText, negativeListener)
        .create()
}

fun Context.createAlertDialogWithOneButtons(title: String?, message: String?,
    positiveButtonText: String?,
    positiveListener: DialogInterface.OnClickListener?
): AlertDialog? {
    return AlertDialog.Builder(this, R.style.ThemeOverlay_AppCompat_Dialog)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText, positiveListener)
        .create()
}