package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.LoginAPI
import com.bidbatl.dileveryapp.model.ActivationCode
import com.bidbatl.dileveryapp.model.LoginResponse
import com.bidbatl.dileveryapp.model.SendOtp
import com.bidbatl.dileveryapp.model.ValidateOtp
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.MLog
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import javax.inject.Inject


class LoginRepository @Inject constructor(private val appExecutors: AppExecutors, private val loginAPI: LoginAPI) {

    fun callLoginAPI(hashMap: HashMap<String, String>): LiveData<Resource<LoginResponse>> {
        return object  : NetworkBoundResource<LoginResponse, LoginResponse>(appExecutors) {
            override fun createCall() = loginAPI.callLoginAPI(hashMap)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }


    fun requestOTP(hashMap: HashMap<String, String>): LiveData<Resource<SendOtp>> {
        return object  : NetworkBoundResource<SendOtp, SendOtp>(appExecutors) {
            override fun createCall() = loginAPI.callRequestOTPAPI(hashMap)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun requestActivation(hashMap: HashMap<String, String>): LiveData<Resource<ActivationCode>> {
        return object  : NetworkBoundResource<ActivationCode, ActivationCode>(appExecutors) {
            override fun createCall() = loginAPI.callResendActivationCodeAPI(hashMap)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun validateOTP(hashMap: HashMap<String, String>): LiveData<Resource<ValidateOtp>> {
        return object  : NetworkBoundResource<ValidateOtp, ValidateOtp>(appExecutors) {
            override fun createCall() = loginAPI.callValidateOTPAPI(hashMap)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }
}