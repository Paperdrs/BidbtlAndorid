package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.GoogleMapAPI
import com.bidbatl.dileveryapp.api.LiveApi
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.model.googlemap.DirectionResponse
import com.bidbatl.dileveryapp.model.googlemap.Distance
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.MLog
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import okhttp3.MultipartBody
import javax.inject.Inject

class LiveRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val liveApi: LiveApi,
    private val googleMapAPI: GoogleMapAPI,
    val preference: Preference)
{
    fun getLiveDelivery():LiveData<Resource<LiveDelivery>>{
        return object : NetworkBoundResource<LiveDelivery,LiveDelivery>(appExecutors){
            override fun createCall() = liveApi.getLiveDelivery(preference.getString(Common.token))

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun reportStart(id:String):LiveData<Resource<ReportResponse>>{
        return object : NetworkBoundResource<ReportResponse,ReportResponse>(appExecutors){
            override fun createCall() = liveApi.reportStart(preference.getString(Common.token),id)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }


    fun reportEnd(id: String):LiveData<Resource<ReportResponse>>{
        return object : NetworkBoundResource<ReportResponse,ReportResponse>(appExecutors){
            override fun createCall() = liveApi.reportEnd(preference.getString(Common.token),id)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun acceptPayment(params:HashMap<String,Any>):LiveData<Resource<Acceptpayment>>{
        return object  : NetworkBoundResource<Acceptpayment,Acceptpayment>(appExecutors){
            override fun createCall() = liveApi.acceptPayment(preference.getString(Common.token),params)

        }.asLiveData()
    }

    fun uploadCheque(file:MultipartBody.Part):LiveData<Resource<UploadResponse>>{
        return object :NetworkBoundResource<UploadResponse,UploadResponse>(appExecutors){
            override fun createCall() = liveApi.uploadCheque(file)
        }.asLiveData()
    }

    fun getDuration(source:String,destination:String):LiveData<Resource<Distance>>{
        return object : NetworkBoundResource<Distance,Distance>(appExecutors){
            override fun createCall() = googleMapAPI.getDistanceAndTimeBetweenTwoLocation(source,destination)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun getDirection(source:String,destination:String):LiveData<Resource<DirectionResponse>>{
        return object : NetworkBoundResource<DirectionResponse,DirectionResponse>(appExecutors){
            override fun createCall() = googleMapAPI.getDirectionBetweenTwoLocation(source,destination)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }
    fun updateDeliveryStatus(params:HashMap<String,Any>):LiveData<Resource<DeliveryStatus>>{
        return object  : NetworkBoundResource<DeliveryStatus,DeliveryStatus>(appExecutors){
            override fun createCall() = liveApi.updateDeliveryStatus(preference.getString(Common.token),params)

        }.asLiveData()
    }
}