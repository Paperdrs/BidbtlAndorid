package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import okhttp3.MultipartBody
import retrofit2.http.*

interface LiveApi {
    @GET(Constants.liveDelivery)
    fun getLiveDelivery(@Header("Token") id: String): LiveData<ApiResponse<LiveDelivery>>

    @GET(Constants.reportStart+"{id}")
    fun reportStart(@Header("Token") token:String,@Path("id") id: String):LiveData<ApiResponse<ReportResponse>>

    @GET(Constants.reportEnd+"{id}")
    fun reportEnd(@Header("Token") token:String,@Path("id") id: String):LiveData<ApiResponse<ReportResponse>>

    @Multipart
    @POST(Constants.upload)
    fun uploadCheque(@Part file: MultipartBody.Part): LiveData<ApiResponse<UploadResponse>>

//    @FormUrlEncoded
    @POST(Constants.acceptCashPayment)
    fun acceptPayment(@Header("Token") id: String,@Body params: HashMap<String,Any>): LiveData<ApiResponse<Acceptpayment>>

    @POST(Constants.deliveryStatus)
    fun updateDeliveryStatus(@Header("Token") id: String,@Body params: HashMap<String,Any>): LiveData<ApiResponse<DeliveryStatus>>


}