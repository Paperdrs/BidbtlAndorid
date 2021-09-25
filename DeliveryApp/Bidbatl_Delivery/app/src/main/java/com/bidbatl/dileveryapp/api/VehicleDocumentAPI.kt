package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.VehicleDocuments
import com.bidbatl.dileveryapp.network_util.ApiResponse
import com.bidbatl.dileveryapp.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface VehicleDocumentAPI {
    @GET(Constants.vehicleDocument +"{id}")
    fun getVehicleDocuments(@Header("Token") token:String,@Path("id") id: String): LiveData<ApiResponse<VehicleDocuments>>
}