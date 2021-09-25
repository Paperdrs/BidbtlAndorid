package com.bidbatl.dileveryapp.ui.reportnondeliver

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityReportNonDeliveryBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ReportNonDeliveryActivity : BaseActivity() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var reportNonDeliverViewModel: ReportNonDeliverViewModel
    lateinit var toolbar:Toolbar
    lateinit var bindReportNonDelivery : ActivityReportNonDeliveryBinding
    lateinit var file: File
    var reason = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindReportNonDelivery = DataBindingUtil.setContentView(this,R.layout.activity_report_non_delivery)
        inflateSpinner()
        reportNonDeliverViewModel = ViewModelProvider(this,viewModelProviderFactory).get(ReportNonDeliverViewModel::class.java)


        setToolBar()
        bindReportNonDelivery.imageViewUpload.setOnClickListener {
            capturePhotoFromCamera()
        }
        bindReportNonDelivery.buttonSend.setOnClickListener {
            when {
                !::file.isInitialized -> {
                showToastMessage("Please upload photo")
                }
                reason.isNullOrEmpty() -> {
                showToastMessage("Please select reason")
                }
                else -> {
                    reportNonDelivery()
                }
            }
        }

        displayNonReportingItems()

    }
    private fun displayNonReportingItems(){
       // bindReportNonDelivery.textViewOrderId.text = kLiveDeliveryData?.data?.number
        bindReportNonDelivery.textViewOrderId.text =  BaseApplication.instance.kLiveDeliveryData?.data?.number
        bindReportNonDelivery.textViewCustomerName.text =  BaseApplication.instance.kLiveDeliveryData?.data?.customer_name
        bindReportNonDelivery.textViewInvoice.text =  BaseApplication.instance.kLiveDeliveryData?.data?.invoice_no

    }
    private fun reportNonDelivery() {
        if ( BaseApplication.instance.kLiveDeliveryData == null) {
            return
        } else {
        showCircleDialog()
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", file.name, requestFile)
        val orderId: RequestBody =  BaseApplication.instance.kLiveDeliveryData!!.data!!.number
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val reason: RequestBody =
            BaseApplication.instance.kLiveDeliveryData!!.data!!.number
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val message: RequestBody =
            bindReportNonDelivery.textViewMessage.text.toString()
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
        reportNonDeliverViewModel.reportNonDelivery(orderId,reason,message,body).observe(this, Observer {

            if (it.status == Status.SUCCESS){
                if (it.data?.code == 200){
                   showToastMessage("Order reported successfully")
                }
                else{
                   showToastMessage(it.data!!.message)
                }
                dismissDialog()
            }
            if (it.status == Status.ERROR){
                showToastMessage(it.message!!)
            }
        })
    }
    }
    fun capturePhotoFromCamera(){
        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        file = ImagePicker.getFile(data)!!
                        Glide.with(this).load(file).into(bindReportNonDelivery.imageViewUpload)
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
    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar_normal)
        val backAction = toolbar.findViewById<ImageView>(R.id.imageView_back)
        val title = toolbar.findViewById<TextView>(R.id.textView_title)
        title.text = getString(R.string.report_non_deliver_title)
        backAction.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
    private fun inflateSpinner(){
        val languages = resources.getStringArray(R.array.Languages)
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    if (position == 0){
                        reason = ""
                    }
                    else{
                        reason = languages[position]
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }
}
