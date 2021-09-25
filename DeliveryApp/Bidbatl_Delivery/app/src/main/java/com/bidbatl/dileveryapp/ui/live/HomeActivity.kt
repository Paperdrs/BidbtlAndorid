package com.bidbatl.dileveryapp.ui.live

import android.Manifest
import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.WebViewActivity
import com.bidbatl.dileveryapp.databinding.ActivityHomeBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.model.LiveDelivery
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.login.MainActivity
import com.bidbatl.dileveryapp.ui.operation.OperationActivity
import com.bidbatl.dileveryapp.ui.profile.MyProfileActivity
import com.bidbatl.dileveryapp.ui.reportbreakdown.ReportBreakDownActivity
import com.bidbatl.dileveryapp.ui.reportnondeliver.ReportNonDeliveryActivity
import com.bidbatl.dileveryapp.ui.vehicledocument.VehicleDetailActivity
import com.bidbatl.dileveryapp.util.BottomSheetUtil
import com.bidbatl.dileveryapp.util.Common
import com.bidbatl.dileveryapp.util.Constants
import com.bidbatl.dileveryapp.util.LocationTracker
import com.bumptech.glide.Glide
import com.eduisfun.stepapp.ui.utils.ScreenUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.button_layout.view.*
import kotlinx.android.synthetic.main.layout_home.view.*
import kotlinx.android.synthetic.main.layout_initate_payment.view.*
import kotlinx.android.synthetic.main.layout_message.view.*
import kotlinx.android.synthetic.main.layout_unloading.view.*
import kotlinx.android.synthetic.main.layout_vehicleload.*
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


class HomeActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener,
    OnMapReadyCallback {
    private lateinit var confirmationBottomSheetDialog: BottomSheetDialog
    lateinit var toolbar: Toolbar
    lateinit var activityBinding:ActivityHomeBinding
    var mGoogleMap: GoogleMap? = null
    private var mLocationPermissionGranted = false
    private val REQUEST_FINE_LOCATION = 1
    val LOCATION_REQUEST = 1000
    lateinit var layoutUnloading:View
    lateinit var layoutMessage:View
    lateinit var layoutInitiatePayment:View
    var orderId = ""
    var isPaymentActivated = false
    var taskKey = ""
    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory
    lateinit var liveViewModel: LiveViewModel
    var currentlat = ""
    var currentLongt = ""
    lateinit var invoiceURL:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_home
        )
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        getCurrentLatLong()
        initCardView()
        setToolBar()
        initDrawer()
        layoutUnloading = activityBinding.homeLayout.layoutUnloading
        layoutInitiatePayment = activityBinding.homeLayout.layoutInitiatePayment
        layoutMessage = activityBinding.homeLayout.layoutMessage
        liveViewModel = ViewModelProvider(this, viewModelFactory).get(LiveViewModel::class.java)
        activateClickListener()
    }
    private  fun openURLInBrowser(url: String, title: String){
        Log.e("URLL", url)
        if (invoiceURL.isNullOrEmpty()){
            showToastMessage("File is not available")
            return
        }
        val intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra(Common.PreferenceKey.pageUrl, url)
        intent.putExtra(Common.PreferenceKey.pageTitle, title)
        startActivity(intent)
    }
    private fun downloadFile(url: String, title: String) {
        if (url.isNullOrEmpty()){
            return
        }
        val fileName = url.replace("/", "_")
        //Constants.BASE_URL
        val DownloadUrl: String = "http://www.africau.edu/images/default/sample.pdf"
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
        val manager1 = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        Objects.requireNonNull(manager1).enqueue(request1)
        if (DownloadManager.STATUS_SUCCESSFUL == 8) {
            Log.e("success", "ssssss")
            Toast.makeText(this,"File Downloaded in your download folder",Toast.LENGTH_LONG).show()
            showChooser(fileName)
        }

    }
    private fun showChooser(filename: String){
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + "/" + filename)
        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(Uri.fromFile(file), "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

        val intent = Intent.createChooser(target, "Open File")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Instruct the user to install a PDF reader here, or something
        }
    }



    override fun onResume() {
        super.onResume()
        getLiveDelivery()
    }
    private fun getDuration(latString: String, longString: String){
        liveViewModel.getDuration(latString, longString).observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                println(it.data?.status)
                activityBinding.homeLayout.textViewReachTime.text = "Duration: " +
                        it.data?.rows?.get(0)?.elements?.get(0)?.duration?.text ?: ""

            }
        })
    }
    private fun getLiveDelivery(){
        showCircleDialog()
        liveViewModel.getLiveDelivery().observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {

                    setLiveDeliveryData(it.data)
                } else {
                    activityBinding.homeLayout.constraintLayoutOrderDetail.visibility = View.GONE
                    navigateToLogin()
                    showToastMessage(it.data!!.message)
                }
            } else if (it.status == Status.ERROR) {
                activityBinding.homeLayout.constraintLayoutOrderDetail.visibility = View.GONE
                showToastMessage(it.message!!)
                dismissDialog()
            }
        })
    }
    private fun setLiveDeliveryData(liveDelivery: LiveDelivery){
        if (liveDelivery.data == null){
            activityBinding.homeLayout.buttonVehicleLoad.visibility = View.GONE
            activityBinding.homeLayout.buttonReportArrival.visibility = View.GONE
            activityBinding.homeLayout.buttonInvoice.visibility = View.GONE
            activityBinding.homeLayout.constraintLayout3.visibility = View.GONE
            Toast.makeText(this, liveDelivery.message, Toast.LENGTH_LONG).show()
            activityBinding.homeLayout.constraintLayoutOrderDetail.visibility = View.GONE
            return
        }

        activityBinding.homeLayout.constraintLayoutOrderDetail.visibility = View.VISIBLE

        confirmationBottomSheetDialog.textView_capacity.setText(
            liveDelivery.data.load_capacity?.roundToInt().toString() + " Kg"
        )
        taskKey  = liveDelivery.data.number
        activityBinding.homeLayout.buttonVehicleLoad.text = "Vehicle Load\n" + liveDelivery.data.load_capacity?.roundToInt().toString() + " Kg"
        activityBinding.homeLayout.buttonVehicleLoad.visibility = View.VISIBLE
        activityBinding.homeLayout.buttonReportArrival.visibility = View.VISIBLE
        activityBinding.homeLayout.buttonInvoice.visibility = View.VISIBLE
        activityBinding.homeLayout.constraintLayout3.visibility = View.VISIBLE
        getDuration(
            currentlat + "," + this.currentLongt,
            liveDelivery.data.shipping_latitude + "," + liveDelivery.data.shipping_longitude
        )
        orderId = liveDelivery.data.id
        invoiceURL = if (liveDelivery.data.invoice_url.isNullOrEmpty()){
            ""
        } else{
            liveDelivery.data.invoice_url
        }
        BaseApplication.instance.kLiveDeliveryData = liveDelivery
        activityBinding.homeLayout.textViewCurrentDate.text = Common.getCurrentDateTime()
        activityBinding.homeLayout.task.text = "Delivery " + liveDelivery.data.delivery_no.toString()
        activityBinding.homeLayout.textViewOrderId.text = liveDelivery.data.number
        activityBinding.homeLayout.textViewCustomerName.text = liveDelivery.data.customer_name
        activityBinding.homeLayout.textViewInvoice.text = liveDelivery.data.invoice_no

        activityBinding.homeLayout.textViewAddress.text = liveDelivery.data?.address?.ship_address + " " +
                liveDelivery.data?.address?.ship_area + " " +
                liveDelivery.data?.address?.ship_city + " " +
                liveDelivery.data?.address?.ship_state + "- " +
                liveDelivery.data?.address?.zip_code

        if (liveDelivery.data.is_unloading_start == "1" && liveDelivery.data.is_unloading_end == "0"){
            preference.setString(taskKey,"2")


        }
        else  if (liveDelivery.data.is_unloading_start == "1" && liveDelivery.data.is_unloading_end == "1"){
            preference.setString(taskKey,"3")


        }
        else{
            preference.setString(taskKey,"1")

        }
        if ( preference.getString(taskKey,"1") == "3") {
            showPayment()
        }
        setTaskStatus()

    }
    private fun initDrawer() {

        val toggle = ActionBarDrawerToggle(
            this, activityBinding.drawerLayout, toolbar, 0, 0
        )
        activityBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        activityBinding.navView.setNavigationItemSelectedListener(this)
        val  headerView = activityBinding.navView.getHeaderView(0)
        var textViewname = headerView.findViewById<TextView>(R.id.textView_name)
        textViewname.text = preference.getString(Common.PreferenceKey.name)
        var imageViewProfile = headerView.findViewById<CircleImageView>(R.id.imageView_profile)
        Glide.with(this).load(preference.getString(Common.PreferenceKey.photo)).placeholder(R.drawable.user_photo).into(
            imageViewProfile
        )
        headerView.setOnClickListener {
            val intent = Intent(BaseApplication.instance, MyProfileActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            BaseApplication.instance.startActivity(intent)
        }
    }
    private fun logoutConfirmation() {
        confirmationBottomSheetDialog.button_done.setOnClickListener {
            confirmationBottomSheetDialog.dismiss()
        }
//        confirmationBottomSheetDialog.textView_no.setOnClickListener {
//            confirmationBottomSheetDialog.dismiss()
//        }
        ScreenUtil.instance.setViewHeightDynamicallyByPercentage(
            this,
            confirmationBottomSheetDialog.layout_vehicle_load,
            Constants.BOTTOM_SHEET_HT
        )
        confirmationBottomSheetDialog.show()

    }
    private fun setToolBar(){
        toolbar = findViewById(R.id.toolbar)
        val profileImage = toolbar.findViewById<CircleImageView>(R.id.imageView_profile)
        profileImage.visibility = View.VISIBLE
        Glide.with(this).load(preference.getString(Common.PreferenceKey.photo)).placeholder(R.drawable.user_photo).into(
            profileImage
        )

        val notiImage = toolbar.findViewById<ImageView>(R.id.imageView_noti)
        notiImage.visibility = View.GONE

        profileImage.setOnClickListener {
            val intent = Intent(BaseApplication.instance, MyProfileActivity::class.java);
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            BaseApplication.instance.startActivity(intent)
        }
        setSupportActionBar(toolbar)
    }
    private fun initCardView(){
        confirmationBottomSheetDialog = BottomSheetUtil.instance.inflateBottomSheetWithView(
            this,
            R.layout.layout_vehicleload,
            false
        )
        activityBinding.homeLayout.buttonLayout.button_operation.setOnClickListener {
            showOperationActivity()
            layoutInitiatePayment.visibility = View.GONE
            layoutMessage.visibility = View.GONE
            layoutUnloading.visibility = View.GONE
            activityBinding.homeLayout.buttonReportArrival.text = "Report Arrival"
            isPaymentActivated = false

        }
    }
    fun showFullMapView(view: View){
        val intent = Intent(BaseApplication.instance, FullMapActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)
    }
    private fun showOperationActivity(){
        val intent = Intent(BaseApplication.instance, OperationActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)
//var array  : Array<String> = arrayOf("Cash", "Cheque")
//
//
//        displaySingleChoiceDialog("Payment Mode",array,0,operation = {
//           println(it)
//        })
//        createAlertDialogWithTwoButtons("Unloading1","Unloading","Report End", DialogInterface.OnClickListener { dialog, which ->
//            println(which)
//            dialog.dismiss()
//        },
//            "Report Start", DialogInterface.OnClickListener { dialog, which ->
//                println(which)
//                dialog.dismiss()
//            })?.show()
    }
private fun setTaskStatus(){


    when {
        preference.getString(taskKey,"1") == "1" -> {
            activityBinding.homeLayout.constraintLayout3.textViewCir1.background = getDrawable(R.drawable.circle_yellow)
            activityBinding.homeLayout.constraintLayout3.textViewCir2.background = getDrawable(R.drawable.circle_gray)
            activityBinding.homeLayout.constraintLayout3.textViewCir3.background = getDrawable(R.drawable.circle_gray)
        }

        preference.getString(taskKey,"1") == "2" -> {
            activityBinding.homeLayout.constraintLayout.textView_n.visibility = View.GONE
            layoutUnloading.visibility = View.VISIBLE
            activityBinding.homeLayout.layoutUnloading.button_report_start.isEnabled = false
            activityBinding.homeLayout.layoutUnloading.button_report_start.setBackgroundResource(
                R.drawable.disable
            )
            activityBinding.homeLayout.buttonReportArrival.text = "Proceed"
            activityBinding.homeLayout.constraintLayout3.textViewCir1.background = getDrawable(R.drawable.circle_green)
            activityBinding.homeLayout.constraintLayout3.textViewCir2.background = getDrawable(R.drawable.circle_yellow)
            activityBinding.homeLayout.constraintLayout3.textViewCir3.background = getDrawable(R.drawable.circle_gray)
        }

        preference.getString(taskKey,"1") == "3" -> {
            layoutInitiatePayment.visibility = View.VISIBLE
            isPaymentActivated = true
            activityBinding.homeLayout.buttonReportArrival.text = "Proceed"
            activityBinding.homeLayout.constraintLayout3.textViewCir1.background = getDrawable(R.drawable.circle_green)
            activityBinding.homeLayout.constraintLayout3.textViewCir2.background = getDrawable(R.drawable.circle_green)
            activityBinding.homeLayout.constraintLayout3.textViewCir3.background = getDrawable(R.drawable.circle_yellow)
        }
        else -> {
            activityBinding.homeLayout.constraintLayout3.textViewCir1.background = getDrawable(R.drawable.circle_green)
            activityBinding.homeLayout.constraintLayout3.textViewCir2.background = getDrawable(R.drawable.circle_green)
            activityBinding.homeLayout.constraintLayout3.textViewCir3.background = getDrawable(R.drawable.circle_green)
        }
    }
}
    private fun activateClickListener(){
        activityBinding.homeLayout.buttonInvoice.setOnClickListener {
            Common.downloadFile(invoiceURL, Constants.PageTitle.INVOICE.value,this,taskKey)
            //openURLInBrowser(Constants.BASE_URL + invoiceURL, Constants.PageTitle.INVOICE.value)
        }
        activityBinding.homeLayout.buttonVehicleLoad.setOnClickListener {
//        logoutConfirmation()
        }
        activityBinding.homeLayout.buttonReportArrival.setOnClickListener {



            if (activityBinding.homeLayout.buttonReportArrival.text == "Proceed"){
                if (isPaymentActivated){
                    showPayment()
                }
                else {
                    reportEnd()
                }
            }
            else{
                preference.setString(taskKey,"1")
                activityBinding.homeLayout.constraintLayout.textView_n.visibility = View.GONE
                layoutUnloading.visibility = View.VISIBLE
                activityBinding.homeLayout.buttonReportArrival.text = "Proceed"
            }
            setTaskStatus()
        }
        activityBinding.homeLayout.layoutUnloading.button_report_start.setOnClickListener {
            reportStart()
        }
        activityBinding.homeLayout.layoutUnloading.button_report_end.setOnClickListener {
            reportEnd()
        }
        activityBinding.homeLayout.layoutMessage.button_transaction_complete.setOnClickListener {
        }
        activityBinding.homeLayout.layoutInitiatePayment.button_initiate_payment.setOnClickListener {
            showPayment()
        }

    }
    private fun reportStart(){
        showCircleDialog()
        liveViewModel.reportStart(orderId).observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                dismissDialog()
                if (it.data?.code == 200) {
                    preference.setString(taskKey,"2")
                    setTaskStatus()
                    activityBinding.homeLayout.layoutUnloading.button_report_start.isEnabled = false
                    showToastMessage("Unloading reported to admin")
                    activityBinding.homeLayout.layoutUnloading.button_report_start.setBackgroundResource(
                        R.drawable.disable
                    )
                }
            } else if (it.status == Status.ERROR) {
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
                    showToastMessage("Unloading reported to admin")
                    layoutInitiatePayment.visibility = View.VISIBLE
                    isPaymentActivated = true
                    preference.setString(taskKey,"3")
                    setTaskStatus()
                }
            } else if (it.status == Status.ERROR) {
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

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.nav_vehicle_doc -> {
                val intent = Intent(BaseApplication.instance, VehicleDetailActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                BaseApplication.instance.startActivity(intent)
            }
            R.id.nav_report_bd -> {
                val intent = Intent(BaseApplication.instance, ReportBreakDownActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                BaseApplication.instance.startActivity(intent)
            }
            R.id.nav_report_un_deliver -> {
                val intent =
                    Intent(BaseApplication.instance, ReportNonDeliveryActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                BaseApplication.instance.startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(BaseApplication.instance, MainActivity::class.java);
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                finishAffinity()
                BaseApplication.instance.startActivity(intent)
            }

        }
        activityBinding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun getCurrentLatLong() {
        val locationTracker =
            LocationTracker(this,
                LocationTracker.CoordinateSender { lat, longi ->
                    if (lat != 0.0 && longi != 0.0) {
                        Log.i("LATT", lat.toString() + "")
                        currentlat = lat.toString()
                        currentLongt = longi.toString()
                        addmarker(lat, longi)
                    }
                })
        locationTracker.getLocation()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mGoogleMap = p0
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
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

    override fun onBackPressed() {
    }
    // map Integration
}

