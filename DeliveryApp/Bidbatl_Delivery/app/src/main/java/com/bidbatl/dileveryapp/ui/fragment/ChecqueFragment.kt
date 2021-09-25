package com.bidbatl.dileveryapp.ui.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.BaseFragment
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.FragmentChecqueBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.live.AcceptPaymentActivity
import com.bidbatl.dileveryapp.ui.live.LiveViewModel
import com.bidbatl.dileveryapp.ui.live.PaymentStatusActivity
import com.bidbatl.dileveryapp.util.Common
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class ChecqueFragment : BaseFragment() {
    lateinit var bindChequeFragment: FragmentChecqueBinding
    lateinit var liveViewModel: LiveViewModel
    @Inject
    lateinit var viewModelProviderFactory: ViewModelProviderFactory
    lateinit var file:File
    var chequeURL = ""

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindChequeFragment = DataBindingUtil.inflate(inflater,R.layout.fragment_checque,container,false)
        liveViewModel = ViewModelProvider(this,viewModelProviderFactory).get(LiveViewModel::class.java)

        bindChequeFragment.buttonChequePhoto.setOnClickListener {
            capturePhotoFromCamera()
        }
        bindChequeFragment.buttonProceed.setOnClickListener {
        validatePayment()
        }
        bindChequeFragment.textViewChequeDate.setOnClickListener {
            val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val myFormat = "dd-MM-yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                bindChequeFragment.textViewChequeDate.text = sdf.format(c.time)

                // bindChequeFragment.textViewChequeDate.text = "" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year
            }, year, month, day)

            dpd.show()
        }
        bindChequeFragment.textViewChequeDate.text  = Common.getCurrentDate()
        return bindChequeFragment.root
    }
    private fun validatePayment(){
        when {
            chequeURL.isNullOrEmpty() -> {
                showToastMessage("Please upload cheque photo")
                return
            }
            bindChequeFragment.textViewChequeDate.text.isNullOrEmpty() -> {
                showToastMessage("Please enter cheque date")
                return
            }
            bindChequeFragment.textViewChequeNo.text.isNullOrEmpty() -> {
                showToastMessage("Please enter cheque no")
                return
            }
            bindChequeFragment.textViewChequeAmount.text.isNullOrEmpty() -> {
                showToastMessage("Please enter cheque amount")
                return
            }
            else -> {
            acceptChequePayment()
            }
        }
    }
    private fun showStatusScreen(status: Boolean) {
        val intent = Intent(BaseApplication.instance, PaymentStatusActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("status",status)
        BaseApplication.instance.startActivity(intent)
        (activity as AcceptPaymentActivity).finish()
    }
    private fun acceptChequePayment() {
Log.e("erer",bindChequeFragment.textViewChequeAmount.text.toString());
        if (BaseApplication.instance.kLiveDeliveryData?.data!!.total_amount == bindChequeFragment.textViewChequeAmount.text.toString().trim()) {
            showCircleDialog()
            val params = HashMap<String, Any>()
        params["order_id"] = BaseApplication.instance.kLiveDeliveryData?.data!!.id
        params["type"] = "cheque"
        params["cheque_no"] = bindChequeFragment.textViewChequeAmount.text.toString()
        params["amount"] = BaseApplication.instance.kLiveDeliveryData?.data!!.total_amount
        params["photo"] = chequeURL
        liveViewModel.acceptPayment(params).observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                if (it.data?.code == 200) {
                    updateDeliveryStatus()
                    showToastMessage("Payment Accepted Successfully")
                    showStatusScreen(true)
                } else {
                    showStatusScreen(false)
                }
                dismissDialog()
            }
            if (it.status == Status.ERROR) {
                dismissDialog()
                showStatusScreen(false)
            }
        })
    }
        else{
            showToastMessage("Incorrect Amount")
        }
    }
    private fun updateDeliveryStatus(){
        val params = java.util.HashMap<String, Any>()
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

    private fun uploadChequePhoto(){
        bindChequeFragment.progressCircular.visibility = View.VISIBLE
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        liveViewModel.uploadCheque(body).observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS){
                if (it.data?.error_status == "0"){
                   chequeURL = it.data.full_url
                }
                bindChequeFragment.progressCircular.visibility = View.GONE
            }
            if (it.status == Status.ERROR)
            {
                bindChequeFragment.progressCircular.visibility = View.GONE
            }
        })

    }
    private fun capturePhotoFromCamera(){
        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)         //Final image size will be less than 1 MB(Optional)
            .start { resultCode, data ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        file = ImagePicker.getFile(data)!!
                        uploadChequePhoto()
                        Glide.with(this).load(file).into(bindChequeFragment.imageViewCheque)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChecqueFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance() = ChecqueFragment()
    }
}
