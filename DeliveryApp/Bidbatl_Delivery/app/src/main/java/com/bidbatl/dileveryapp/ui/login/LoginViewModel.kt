package com.bidbatl.dileveryapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.ActivationCode

import com.bidbatl.dileveryapp.model.LoginResponse
import com.bidbatl.dileveryapp.model.SendOtp
import com.bidbatl.dileveryapp.model.ValidateOtp
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.LoginRepository
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {
    fun callLoginAPI(hashMap: HashMap<String, String>): LiveData<Resource<LoginResponse>> {
        return loginRepository.callLoginAPI(hashMap)
    }

    fun callRequestOTP(hashMap: HashMap<String, String>): LiveData<Resource<SendOtp>> {
        return loginRepository.requestOTP(hashMap)
    }

    fun resendActivation(hashMap: HashMap<String, String>): LiveData<Resource<ActivationCode>> {
        return loginRepository.requestActivation(hashMap)
    }

    fun validateOTP(hashMap: HashMap<String, String>): LiveData<Resource<ValidateOtp>> {
        return loginRepository.validateOTP(hashMap)
    }
}