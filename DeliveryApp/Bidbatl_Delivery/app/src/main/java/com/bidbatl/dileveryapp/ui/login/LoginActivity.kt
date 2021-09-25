package com.bidbatl.dileveryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bidbatl.dileveryapp.BaseActivity
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.R
import com.bidbatl.dileveryapp.databinding.ActivityLoginBinding
import com.bidbatl.dileveryapp.di.ViewModelProviderFactory
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Status
import com.bidbatl.dileveryapp.ui.live.HomeActivity
import com.bidbatl.dileveryapp.util.Common
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    lateinit var activityLoginBinding: ActivityLoginBinding
    var timer : CountDownTimer? = null
    private var step :  Int = 0;


    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        initVM()
        activateListener()
        enableAndDisableField()
    }
    private fun enableAndDisableField(){
        when (step) {
            0 -> {
                activityLoginBinding.editTextOtp.isEnabled = false
                activityLoginBinding.buttonResendOtp.isEnabled = false
                activityLoginBinding.editTextActivationCode.isEnabled = false
                activityLoginBinding.textViewResendCode.isEnabled = false
            }
            1 -> {
                activityLoginBinding.buttonRequestOtp.isEnabled = false
                activityLoginBinding.editTextOtp.isEnabled = true
                activityLoginBinding.buttonResendOtp.isEnabled = true
                activityLoginBinding.editTextActivationCode.isEnabled = true
                activityLoginBinding.textViewResendCode.isEnabled = true
            }
        }
    }


    private fun initVM(){
        loginViewModel = ViewModelProvider(this, providerFactory).get(LoginViewModel::class.java)
    }
    private fun callRequestOTP(){
        if (activityLoginBinding.editTextMobileNo.text.length < 10 || activityLoginBinding.editTextMobileNo.text.isNullOrEmpty()){
            Toast.makeText(this,"Please Enter valid mobile number",Toast.LENGTH_LONG).show()
        }
        else{
            var params = HashMap<String, String>()
            params["phone"] = activityLoginBinding.editTextMobileNo.text.toString()
            showCircleDialog()
            loginViewModel.callRequestOTP(params).observe(this, Observer {
                if (it.status == Status.SUCCESS){
                    dismissDialog()
                    if (it.data!!.code == 200){
                        step = 1
                        enableAndDisableField()
                        Toast.makeText(this,"" + it.data.data!!.otp,Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,"" + it.data.message,Toast.LENGTH_LONG).show()
                    }

                }else if (it.status == Status.ERROR){
                        Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                        dialog?.dismiss()


                }

            })

        }
    }
    private fun validateOTP(){
        showCircleDialog()
        var params = HashMap<String, String>()
        params["phone"] = editText_mobileNo.text.toString()
        params["otp"] = activityLoginBinding.editTextOtp.text.toString()
        loginViewModel.validateOTP(params).observe(this, Observer {
            if (it.status == Status.SUCCESS){
                if (it.data!!.code == 200){
                    callLoginAPI()
                }
                else{
                    Toast.makeText(this,it.data!!.message,Toast.LENGTH_LONG).show()
                    dialog?.dismiss()

                }
            }
            if (it.status == Status.ERROR){
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                dialog?.dismiss()
            }


        })
    }
    private fun showContDownTimer(){
         timer = object: CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }
            override fun onFinish() {

            }
        }
        timer?.start()
    }
    private fun resendActivationCode(){
        var params = HashMap<String, String>()
        params["phone"] = "7011918570"
        params["password"] = "123456"
        loginViewModel.resendActivation(params).observe(this, Observer {

        })
    }
    private fun  test(){

    }

    private fun callLoginAPI(){
        var params = HashMap<String, String>()
        params["phone"] = editText_mobileNo.text.toString() //activityLoginBinding.editTextMobileNo.text.toString()
        params["activation_code"] = activityLoginBinding.editTextActivationCode.text.toString()
        loginViewModel.callLoginAPI(params).observe(this, Observer {
            if (it.status == Status.SUCCESS){
                dialog?.dismiss()
                if (it.data?.code == 200){
                preference.setBoolean(Common.PreferenceKey.isLoggedIn,true)
                    preference.setString(Common.PreferenceKey.vehicleid,it.data.data?.vehicle_id)
                    preference.setString(Common.PreferenceKey.name,it.data.data?.name)
                    preference.setString(Common.PreferenceKey.phone,it.data.data?.phone)
                    preference.setString(Common.PreferenceKey.photo,it.data.data?.photo)
                    preference.setString(Common.token,it.data.data?.token)
                    loginAction()
                }
                else{
                    Toast.makeText(this,it.data!!.message,Toast.LENGTH_LONG).show()
                }
            }
            if (it.status == Status.ERROR){
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
                dialog?.dismiss()
            }

        })
    }

    fun validateUserLogin():Boolean{
        return when {
            activityLoginBinding.editTextMobileNo.text.length < 10 || activityLoginBinding.editTextMobileNo.text.isNullOrEmpty()  -> {
                Toast.makeText(this,"Please Enter valid mobile number",Toast.LENGTH_LONG).show()
                false
            }
            activityLoginBinding.editTextOtp.text.length < 4 -> {
                Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_LONG).show()
                false
            }
            activityLoginBinding.editTextActivationCode.text.isNullOrEmpty() -> {
                Toast.makeText(this,"Please Enter activation code",Toast.LENGTH_LONG).show()

                false
            }
            else -> {
                true
            }
        }
    }

    private fun activateListener(){

       activityLoginBinding.buttonRequestOtp.setOnClickListener {
           callRequestOTP()

       }
        activityLoginBinding.buttonResendOtp.setOnClickListener {
            callRequestOTP()
        }
        activityLoginBinding.buttonLogin.setOnClickListener {
            if (validateUserLogin()){
            validateOTP()
            }
        }
        activityLoginBinding.textViewResendCode.setOnClickListener {
            resendActivationCode()
        }
    }

    private fun loginAction() {
        val intent = Intent(BaseApplication.instance, HomeActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        BaseApplication.instance.startActivity(intent)

    }
}
