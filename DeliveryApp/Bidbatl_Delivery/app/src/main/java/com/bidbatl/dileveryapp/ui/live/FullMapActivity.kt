package com.bidbatl.dileveryapp.ui.live

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityFullMapBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.util.LocationTracker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.layout_initate_payment.view.*
import kotlinx.android.synthetic.main.layout_message.view.*
import kotlinx.android.synthetic.main.layout_unloading.view.*
import javax.inject.Inject

class FullMapActivity : BaseActivity(), OnMapReadyCallback {
    var mGoogleMap: GoogleMap? = null
    private var mLocationPermissionGranted = false
    private val REQUEST_FINE_LOCATION = 1
    val LOCATION_REQUEST = 1000
    lateinit  var toolbar : Toolbar
    lateinit var activityBinding:ActivityFullMapBinding
    private var isPaymentActivated = false
    var orderId = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    lateinit var liveViewModel: LiveViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         activityBinding = DataBindingUtil.setContentView(this,
                    R.layout.activity_full_map
                )
        liveViewModel = ViewModelProvider(this,viewModelFactory).get(LiveViewModel::class.java)
        if (BaseApplication.instance.kLiveDeliveryData != null){
            orderId  = BaseApplication.instance.kLiveDeliveryData?.data!!.number


        }
        else{
            activityBinding.cardView2.visibility = View.GONE
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        setupPermissions()
        activateClickListener()
        setToolBar()
    }
    private fun reportStart(){
        showCircleDialog()
        liveViewModel.reportStart(orderId).observe(this, Observer {
            if (it.status == Status.SUCCESS){
                dismissDialog()
                if (it.data?.code == 200){
                    activityBinding.layoutUnloading.button_report_start.isEnabled = false
                    activityBinding.layoutUnloading.button_report_start.setBackgroundResource(R.drawable.disable)
                    showToastMessage("Unloading reported to admin")
                    preference.setString(orderId,"2")
                }
            }
            else if (it.status == Status.ERROR){
                dismissDialog()
                showToastMessage(it.message!!)
            }

        })
    }
    private fun reportEnd(){
        showCircleDialog()
        liveViewModel.reportEnd(orderId).observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
//                    updateDeliveryStatus()
                    showToastMessage("Unloading reported to admin")
                        activityBinding.layoutInitiatePayment.visibility = View.VISIBLE
                    isPaymentActivated = true
                    activityBinding.buttonReportArrival.visibility = View.VISIBLE
                    preference.setString(orderId,"3")
                }
            }
            else if (it.status == Status.ERROR) {
                dismissDialog()
                showToastMessage(it.message!!)
            }
        })
    }
    private fun updateDeliveryStatus(){
        val params = HashMap<String, Any>()
        params["status"] = "delivered"
        params["order_id"] = BaseApplication.instance.kLiveDeliveryData?.data!!.id
        liveViewModel.updateDeliveryStatus(params).observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                }
            }
            else if (it.status == Status.ERROR) {
                dismissDialog()
                showToastMessage(it.message!!)
            }
        })
    }
    private fun showPayment(){
        val intent = Intent(BaseApplication.instance, AcceptPaymentActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)
    }
    fun activateClickListener(){
        activityBinding.buttonReportArrival.setOnClickListener {
            if (activityBinding.buttonReportArrival.text == "Proceed"){
                if (isPaymentActivated){
                    showPayment()
                }
                else {
                    reportEnd()
                }
            }
            else{
                preference.setString(orderId,"1")
                activityBinding.layoutUnloading.visibility = View.VISIBLE
                activityBinding.buttonReportArrival.text = "Proceed"
                activityBinding.buttonReportArrival.visibility = View.GONE
            }
        }
        activityBinding.layoutUnloading.button_report_start.setOnClickListener {
            reportStart()
        }
        activityBinding.layoutUnloading.button_report_end.setOnClickListener {
            reportEnd()
        }
        activityBinding.layoutMessage.button_transaction_complete.setOnClickListener {
        }
        activityBinding.layoutInitiatePayment.button_initiate_payment.setOnClickListener {
            showPayment()
        }

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

    private fun getCurrentLatLong() {
        val locationTracker =
            LocationTracker(this,
                LocationTracker.CoordinateSender { lat, longi ->
                    if (lat != 0.0 && longi != 0.0) {
                        Log.i("LATT", lat.toString() + "")
                        addmarker(lat, longi)
                    }
                })
        locationTracker.getLocation()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        mGoogleMap?.isMyLocationEnabled = true
    }

    private fun addmarker(lat: Double, long: Double) {
        //Place current location marker
        //Place current location marker
        val latLng = LatLng(lat, long)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("Current Position")
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
        mGoogleMap!!.addMarker(markerOptions)

        //move map camera
        //move map camera
        mGoogleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f))
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("TAG", "Permission to record denied")
            makeRequest()
        } else {
            mLocationPermissionGranted = true
            getCurrentLatLong()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        mLocationPermissionGranted = false
        when (requestCode) {
            REQUEST_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                    getCurrentLatLong()
                } else {
                    Toast.makeText(this, "Location Permission Denied.", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                return
            }
            LOCATION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLatLong()
                } else {
                    Toast.makeText(this, "Location Permission Denied.", Toast.LENGTH_SHORT).show()
                    onBackPressed()
                }
                return
            }
        }
    }

}
