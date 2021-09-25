package com.bidbatl.dileveryapp.ui.reportbreakdown

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityReportBreakDownBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.util.Common
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


class ReportBreakDownActivity : BaseActivity() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory

    lateinit var reportBreakDownViewModel: ReportBreakDownViewModel
    lateinit var toolbar : Toolbar
    lateinit var reportBinding: ActivityReportBreakDownBinding
    lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportBinding = DataBindingUtil.setContentView(this,R.layout.activity_report_break_down)
        initVM()
        setToolBar()
        activateClickListner()


    }
    fun capturePhotoFromCamera(){
        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                         file = ImagePicker.getFile(data)!!
                        Glide.with(this).load(file).into(reportBinding.imageViewUpload)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
    private fun initVM(){
       reportBreakDownViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ReportBreakDownViewModel::class.java)
    }
    fun reportBreakDown(file: File){
        showCircleDialog()
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val id: RequestBody = preference.getString(Common.PreferenceKey.vehicleid).toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val message: RequestBody =
            reportBinding.textViewMessage.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        reportBreakDownViewModel.reportBreakDown(id,message,body).observe(this, Observer {
            if (it.status == Status.SUCCESS){
                dismissDialog()
                when (it.data?.code) {
                    200 -> {
                        showToastMessage("Break Down successfully reported")
                        backPressed()
                    }
                    401 -> {
                        showToastMessage(it.data.message)
                        navigateToLogin()
                    }
                    else -> {
                        showToastMessage(it.data!!.message)
                    }
                }
            }
            if (it.status == Status.ERROR){
                dismissDialog()
                showToastMessage(it.message!!)
            }
        })
    }
    private fun activateClickListner(){
        reportBinding.imageViewUpload.setOnClickListener {
            capturePhotoFromCamera()
        }
        reportBinding.buttonReport.setOnClickListener {
            if (!::file.isInitialized){
               showToastMessage("Please upload vehicle photo")
            }
            else if (reportBinding.textViewMessage.text.isNullOrEmpty()){
                showToastMessage("Please enter message")
            }
            else{
                reportBreakDown(file)
            }
        }
        reportBinding.buttonCallAdmin.setOnClickListener {
        callToNumber("")
        }
        reportBinding.constraintLayoutCall.setOnClickListener {
        callToNumber("")
        }
    }
    private fun callToNumber(number:String){
        val intent = Intent(Intent.ACTION_CALL);
        intent.data = Uri.parse("tel:$number")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE),
                1)
            // Ask Permission
        }
        startActivity(intent)
    }

    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar_normal)
        val backAction = toolbar.findViewById<ImageView>(R.id.imageView_back)
        val title = toolbar.findViewById<TextView>(R.id.textView_title)
        title.text = getString(R.string.report_break_down_title)
        backAction.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
}
