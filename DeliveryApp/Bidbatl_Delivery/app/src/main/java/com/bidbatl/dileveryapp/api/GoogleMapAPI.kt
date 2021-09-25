package com.bidbatl.dileveryapp.api

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.model.googlemap.DirectionResponse
import com.bidbatl.dileveryapp.model.googlemap.Distance
import com.bidbatl.dileveryapp.network_util.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapAPI {
    @GET("distancematrix/json?units=metric&key=AIzaSyB1pKJ3DgKQjWULciB65Wy-4N8bSafSdrc")
    fun getDistanceAndTimeBetweenTwoLocation(@Query("origins") source:String, @Query("destinations") destination:String):LiveData<ApiResponse<Distance>>

    @GET("directions/json?mode=DRIVING&key=AIzaSyB1pKJ3DgKQjWULciB65Wy-4N8bSafSdrc")
    fun getDirectionBetweenTwoLocation(@Query("origin") source:String, @Query("destination") destination:String):LiveData<ApiResponse<DirectionResponse>>
}