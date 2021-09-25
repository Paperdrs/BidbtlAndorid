package com.bidbatl.dileveryapp.ui.operation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivitySummaryDetailBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.util.Common
import com.bidbatl.dileveryapp.util.Constants
import com.bumptech.glide.Glide
import javax.inject.Inject

class SummaryDetailActivity : BaseActivity() {
    lateinit var toolbar:Toolbar

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var activityBinding:ActivitySummaryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_summary_detail)
        activityBinding = DataBindingUtil.setContentView(this,R.layout.activity_summary_detail)
        setToolBar()
      val operationData:OperationData = intent.getSerializableExtra("summaryData") as OperationData
        println(intent.getSerializableExtra("summaryData"))
        displaySummaryDetailContent(operationData)

    }
    private fun displaySummaryDetailContent(operationData: OperationData){
        activityBinding.textViewName.text = operationData.customer_name
        activityBinding.textViewId.text = operationData.user_id
        activityBinding.textViewAddress.text = operationData.ship_address

        activityBinding.buttonInvoice.setOnClickListener {
            openURLInBrowser(operationData.invoice_url,Constants.PageTitle.INVOICE.value,operationData.number)
        }
        activityBinding.buttonChallan.setOnClickListener {
            openURLInBrowser(operationData.challan_url,Constants.PageTitle.CHALLAN.value,operationData.number)
        }
        activityBinding.buttonEwayBill.setOnClickListener {
            openURLInBrowser(operationData.bill_url,Constants.PageTitle.EWAYBILL.value,operationData.number)
        }
        activityBinding.buttonPaymentAdvice.setOnClickListener {
            openURLInBrowser(operationData.payment_advice_url,Constants.PageTitle.PAYMENTADVICE.value,operationData.number)
        }
        Glide.with(this).load(preference.getString(Common.PreferenceKey.photo)).placeholder(R.drawable.user_photo)
            .into(activityBinding.imageViewProfile)
    }
    private  fun openURLInBrowser(url:String,title:String,orderNumber:String){
        if (url.isNullOrEmpty()){
            showToastMessage("File is not available")
            return
        }
        Common.downloadFile(url,title,this,orderNumber)

//        val intent = Intent(this, WebViewActivity::class.java)
//        intent.putExtra(Common.PreferenceKey.pageUrl,url)
//        intent.putExtra(Common.PreferenceKey.pageTitle,title)
//        startActivity(intent)
    }

    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar)
        val menuImage = toolbar.findViewById<ImageView>(R.id.imageView_menu)
        menuImage.setImageDrawable(getDrawable(R.drawable.back))
        menuImage.visibility = View.VISIBLE
        menuImage.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
}
