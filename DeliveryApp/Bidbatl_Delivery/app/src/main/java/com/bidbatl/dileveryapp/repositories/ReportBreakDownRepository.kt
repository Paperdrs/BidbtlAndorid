package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.ReportBreakDownAPI
import com.bidbatl.dileveryapp.model.ReportBreakDown
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportBreakDownRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val reportNonDeliverAPI: ReportBreakDownAPI,
    private val preference: Preference
) {
    fun reportBreakDown(
        vehicle_id: RequestBody, message: RequestBody, file: MultipartBody.Part
    ): LiveData<Resource<ReportBreakDown>> {

        return object : NetworkBoundResource<ReportBreakDown, ReportBreakDown>(appExecutors) {
            override fun createCall() = reportNonDeliverAPI.reportBreakDown(
                preference.getString(Common.token),
                vehicle_id,
                message,
                file
            )
        }.asLiveData()
    }
}