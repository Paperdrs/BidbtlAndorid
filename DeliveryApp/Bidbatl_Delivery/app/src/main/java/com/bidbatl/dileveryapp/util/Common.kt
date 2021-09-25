package com.bidbatl.dileveryapp.util

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import com.bidbatl.dileveryapp.BaseApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object Common {
    val token = "token"
    val vehicleid = "vehicle_id"
    object PreferenceKey{
        val vehicleid = "vehicle_id"
        val name = "name"
        val photo = "photo"
        val phone = "phone"
        val isLoggedIn = "isLoggedIn"
        val customerId = "customer_id"
        val customerName = "customer_name"
        val pageUrl = "pageUrl"
        val pageTitle = "pageTitle"
        val taskStatus = "taskStatus"

    }

    fun getCurrentDateTime():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy, EEEE h:mm a")
        val currentDate = sdf.format(Date())
        println("CurrentDate : $currentDate")
        return currentDate
    }
    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        println("CurrentDate : $currentDate")
        return currentDate
    }
    fun downloadFile(url: String, title: String, context: Context, orderNumber: String) {
        if (url.isNullOrEmpty()){
            return
        }
        var fileName = url.replace("/", "_")
        var DownloadUrl: String =  Constants.BASE_URL+"uploads/" + url//"http://www.africau.edu/images/default/sample.pdf"

        if (url.contains("http") || url.contains("https")){
            fileName = title + "_" + BaseApplication.instance.kLiveDeliveryData?.data?.number  + getExt(url)
            DownloadUrl =  url//"http://www.africau.edu/images/default/sample.pdf"

        }


//        val fileName = "$title$orderNumber.pdf"
        //Constants.BASE_URL
        Log.e("urll", DownloadUrl)
        val request1 = DownloadManager.Request(Uri.parse(DownloadUrl))
        request1.setDescription(title) //appears the same in Notification bar while downloading
        request1.setTitle(title)
        request1.setVisibleInDownloadsUi(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request1.allowScanningByMediaScanner()
            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        }
//        request1.setDestinationInExternalFilesDir(applicationContext, Environment.DIRECTORY_DOWNLOADS, fileName)
        request1.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        val manager1 = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        Objects.requireNonNull(manager1).enqueue(request1)
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Log.e("success", "ssssss")
            Toast.makeText(context, "File Downloaded in your download folder", Toast.LENGTH_LONG).show()
            showChooser(fileName, context)
        }

    }
    private fun showChooser(filename: String, context: Context){
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())



        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + filename)
        val target = Intent(Intent.ACTION_VIEW)
        if (getExt(filename) == ".png" || getExt(filename) == ".jpg" || getExt(filename) == ".jpeg"){
            target.setDataAndType(Uri.fromFile(file), "image/*")

        }
        else{
            target.setDataAndType(Uri.fromFile(file), "application/pdf")

        }
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        val intent = Intent.createChooser(target, "Open File")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }
    }
    private fun  getExt(url:String):String{
        print(url.substring(url.lastIndexOf(".")).toLowerCase())
        val dff = url.substring(url.lastIndexOf(".")).toLowerCase()
        return  url.substring(url.lastIndexOf(".")).toLowerCase()
    }
}