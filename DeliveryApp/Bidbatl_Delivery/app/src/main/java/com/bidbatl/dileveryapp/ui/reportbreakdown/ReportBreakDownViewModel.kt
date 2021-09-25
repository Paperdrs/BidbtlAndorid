package com.bidbatl.dileveryapp.ui.reportbreakdown

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.ReportBreakDown
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.ReportBreakDownRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ReportBreakDownViewModel @Inject constructor(private val reportBreakDownRepository: ReportBreakDownRepository):ViewModel() {
    fun test(){
        println("ReportBreakDownViewModel")
    }
    fun reportBreakDown(vehicle_id: RequestBody, message: RequestBody, file: MultipartBody.Part
    ):LiveData<Resource<ReportBreakDown>>{
        return reportBreakDownRepository.reportBreakDown(vehicle_id,message,file)
    }
}