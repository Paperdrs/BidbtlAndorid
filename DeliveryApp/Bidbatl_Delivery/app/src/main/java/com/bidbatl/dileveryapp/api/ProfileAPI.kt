package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.Profile
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header

interface ProfileAPI {
    @GET(Constants.userProfile)
    fun getUserProfile(@Header("Token") token:String):LiveData<ApiResponse<Profile>>
}