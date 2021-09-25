package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.ReportNonDeliverAPI
import com.bidbatl.dileveryapp.model.ReportNonDeliver
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportNonDeliverRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val reportNonDeliverAPI: ReportNonDeliverAPI,
    private val preference: Preference
) {
    fun reportNonDeliverItems(orderId:RequestBody,reason:RequestBody,message:RequestBody,file:MultipartBody.Part): LiveData<Resource<ReportNonDeliver>> {
        return object : NetworkBoundResource<ReportNonDeliver, ReportNonDeliver>(appExecutors) {
            override fun createCall() =
                reportNonDeliverAPI.reportNonDeliverItem(preference.getString(Common.token), orderId,reason,message,file)

        }.asLiveData()
    }

}