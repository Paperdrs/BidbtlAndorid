package com.bidbatl.dileveryapp.ui.vehicledocument

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityVehicleDetailBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.VehicleData
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.util.Common
import com.bidbatl.dileveryapp.util.Constants
import javax.inject.Inject

class VehicleDetailActivity : BaseActivity() {
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var vehicleDocumentsViewModel: VehicleDocumentsViewModel
    lateinit var toolbar: Toolbar
    lateinit var vehicleBinding: ActivityVehicleDetailBinding
    var vehicleData: VehicleData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vehicleBinding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_detail)
        initVM()
        setToolBar()
        activateListner()
    }

    private fun initVM() {
        showCircleDialog()
        vehicleDocumentsViewModel = ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(VehicleDocumentsViewModel::class.java)
        vehicleDocumentsViewModel.getVehicleDetail().observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                when (it.data?.code) {
                    200 -> {
                        vehicleData = it.data.data
                        print(vehicleData)
                        setDisplayVehicleDetail(vehicleData!!)
                    }
                    410 -> {
                        navigateToLogin()
                        showToastMessage(it.data.message)
                    }
                    else -> {
                        showToastMessage(it.data!!.message)
                    }
                }

            }
            if (it.status == Status.ERROR) {
                showToastMessage(it.message!!)
                dismissDialog()
            }
        })
    }

    private fun activateListner() {
        vehicleBinding.buttonRc.setOnClickListener {
            openURLInBrowser(vehicleData!!.vehicle_docs.rc_image,Constants.PageTitle.RC.value)
        }
        vehicleBinding.buttonInsurance.setOnClickListener {
            openURLInBrowser(vehicleData!!.vehicle_docs.insurance_image,Constants.PageTitle.INSURANCE.value)
        }
        vehicleBinding.buttonPermit.setOnClickListener {
            openURLInBrowser(vehicleData!!.vehicle_docs.permit_image,Constants.PageTitle.PERMIT.value)
        }
        vehicleBinding.buttonPollutation.setOnClickListener {
            openURLInBrowser(vehicleData!!.vehicle_docs.pollution_image,Constants.PageTitle.POLLUTION.value)
        }
        vehicleBinding.buttonFitnes.setOnClickListener {
            openURLInBrowser(vehicleData!!.vehicle_docs.fitnes_url,Constants.PageTitle.FITNESS.value)
        }

    }
    private  fun openURLInBrowser(url:String,title:String){
        if (url.isNullOrEmpty()){
            Toast.makeText(this,"File is not available",Toast.LENGTH_LONG).show()
            return
        }
Common.downloadFile(url,title,this,"")
//        val intent = Intent(this,WebViewActivity::class.java)
//        intent.putExtra(Common.PreferenceKey.pageUrl,url)
//        intent.putExtra(Common.PreferenceKey.pageTitle,title)
//        startActivity(intent)
    }
    private fun setDisplayVehicleDetail(detail: VehicleData) {
        vehicleBinding.textViewVehicleNo.setText(detail.registration)
        vehicleBinding.textViewOwnerName.setText(detail.name)
        vehicleBinding.textViewEngineNo.setText(detail.engine_no)
        vehicleBinding.textViewChasisNo.setText(detail.chasis_no)

    }

    private fun setToolBar() {
        toolbar = findViewById(R.id.toolbar_normal)
        val backAction = toolbar.findViewById<ImageView>(R.id.imageView_back)
        val title = toolbar.findViewById<TextView>(R.id.textView_title)
        title.text = getString(R.string.vehicle_document_title)
        backAction.setOnClickListener {
            backPressed()
        }
        setSupportActionBar(toolbar)
    }
}
