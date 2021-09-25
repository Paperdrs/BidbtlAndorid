package com.bidbatl.dileveryapp.ui.live

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityOtpVerificationBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.login.LoginViewModel
import com.bidbatl.dileveryapp.util.Common
import javax.inject.Inject

class OtpVerificationActivity : BaseActivity() {
    lateinit var otpBinding:ActivityOtpVerificationBinding
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = DataBindingUtil.setContentView(this,R.layout.activity_otp_verification)
        otpBinding.textViewDate.text = Common.getCurrentDateTime()
        initVM()
        activateListener()
    }
    private fun initVM(){
        loginViewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)
    }
    fun callRequestOTP(phoneNo:String){
            var params = HashMap<String, String>()
            params["phone"] = phoneNo
            showCircleDialog()
            loginViewModel.callRequestOTP(params).observe(this, Observer {
                if (it.status == Status.SUCCESS){
                    dismissDialog()
                    if (it.data!!.code == 200){
                        Toast.makeText(this,"" + it.data.data!!.otp, Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,"" + it.data.message, Toast.LENGTH_LONG).show()
                    }
                }else if (it.status == Status.ERROR){
                    Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                    dialog?.dismiss()
                }
            })
    }

    private fun activateListener(){
        otpBinding.textViewResendDriverOtp.setOnClickListener {
            callRequestOTP("7011918570")
        }
        otpBinding.textViewResendCustomerOtp.setOnClickListener {
            callRequestOTP("7011918570")
        }
        otpBinding.buttonVerify.setOnClickListener {
        if (otpBinding.editTextCustomerOtp.text.length<4 || otpBinding.editTextDriverOtp.text.length<4){
            showToastMessage("Please enter OTP")
        }
            else{

        }
        }
    }
}
