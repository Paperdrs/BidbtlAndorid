package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.ReportNonDeliver
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ReportNonDeliverAPI {
    @Multipart
    @POST(Constants.reportNonDeliver)
    fun reportNonDeliverItem(
        @Header("Token") token: String,
        @Part("order_id") order_id: RequestBody,
        @Part("reason") reason: RequestBody,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part
    ): LiveData<ApiResponse<ReportNonDeliver>>
}