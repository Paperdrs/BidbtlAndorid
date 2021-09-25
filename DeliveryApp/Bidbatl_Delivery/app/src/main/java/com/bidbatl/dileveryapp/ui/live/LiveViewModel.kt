package com.bidbatl.dileveryapp.ui.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.model.googlemap.DirectionResponse
import com.bidbatl.dileveryapp.model.googlemap.Distance
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.LiveRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class LiveViewModel @Inject constructor(private val liveRepository: LiveRepository) :ViewModel(){

    fun getLiveDelivery():LiveData<Resource<LiveDelivery>>{
        return liveRepository.getLiveDelivery()
    }

    fun reportStart(id:String):LiveData<Resource<ReportResponse>>{
        return liveRepository.reportStart(id)
    }

    fun reportEnd(id: String):LiveData<Resource<ReportResponse>>{
        return liveRepository.reportEnd(id)
    }


    fun acceptPayment(params:HashMap<String,Any>):LiveData<Resource<Acceptpayment>>{
        return liveRepository.acceptPayment(params)
    }

    fun uploadCheque(file:MultipartBody.Part):LiveData<Resource<UploadResponse>>{
        return liveRepository.uploadCheque(file)
    }

    fun getDuration(source:String,destination:String):LiveData<Resource<Distance>>{
        return liveRepository.getDuration(source,destination)
    }
    fun getDirection(source:String,destination:String):LiveData<Resource<DirectionResponse>>{
        return liveRepository.getDirection(source,destination)
    }

    fun updateDeliveryStatus(params:HashMap<String,Any>):LiveData<Resource<DeliveryStatus>>{
        return liveRepository.updateDeliveryStatus(params)
    }

}