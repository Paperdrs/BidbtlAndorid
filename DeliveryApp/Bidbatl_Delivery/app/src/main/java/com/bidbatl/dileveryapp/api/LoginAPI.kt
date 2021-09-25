package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.ActivationCode
import com.bidbatl.dileveryapp.model.LoginResponse
import com.bidbatl.dileveryapp.model.SendOtp
import com.bidbatl.dileveryapp.model.ValidateOtp
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginAPI {

    @FormUrlEncoded
    @POST(Constants.login)
    fun callLoginAPI(@FieldMap hashMap: HashMap<String, String>): LiveData<ApiResponse<LoginResponse>>

    @FormUrlEncoded
    @POST(Constants.requestOTP)
    fun callRequestOTPAPI(@FieldMap hashMap: HashMap<String, String>): LiveData<ApiResponse<SendOtp>>

    @FormUrlEncoded
    @POST(Constants.resendActivationCode)
    fun callResendActivationCodeAPI(@FieldMap hashMap: HashMap<String, String>): LiveData<ApiResponse<ActivationCode>>

    @FormUrlEncoded
    @POST(Constants.validateOTP)
    fun callValidateOTPAPI(@FieldMap hashMap: HashMap<String, String>): LiveData<ApiResponse<ValidateOtp>>
}