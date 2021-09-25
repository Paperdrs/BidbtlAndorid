package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface OperationAPI {

    @GET(Constants.order)
    fun getOrder(@Header("Token") token:String):LiveData<ApiResponse<OperationModel>>

    @GET(Constants.search+"{id}")
    fun searchOrder(@Header("Token") token:String,@Path("id") id: String):LiveData<ApiResponse<OperationModel>>
//
    @GET(Constants.order)
    fun getDeliverySummary(@Query("postId") deliverId:String):LiveData<ApiResponse<DeliverySummary>>

    @GET(Constants.order)
    fun getCollectionSummary(@Query("id") id:String):LiveData<ApiResponse<CollectionSummary>>

    @GET(Constants.order)
    fun getGoodsReturnDetail(@Query("id") id: String):LiveData<ApiResponse<GoodsReturnDetails>>

    @GET(Constants.customerList)
    fun getUserList(@Header("Token") token:String):LiveData<ApiResponse<CustomerResponse>>


}