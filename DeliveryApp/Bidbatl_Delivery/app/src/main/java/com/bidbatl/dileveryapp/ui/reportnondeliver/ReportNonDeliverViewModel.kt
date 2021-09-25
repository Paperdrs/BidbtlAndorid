package com.bidbatl.dileveryapp.ui.reportnondeliver

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.ReportNonDeliver
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.ReportNonDeliverRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportNonDeliverViewModel @Inject constructor(private val reportNonDeliverRepository: ReportNonDeliverRepository) :ViewModel() {
    fun reportNonDelivery(orderId:RequestBody,reason:RequestBody,message:RequestBody,file:MultipartBody.Part):LiveData<Resource<ReportNonDeliver>>{
        return reportNonDeliverRepository.reportNonDeliverItems(orderId,reason, message, file)
    }
}