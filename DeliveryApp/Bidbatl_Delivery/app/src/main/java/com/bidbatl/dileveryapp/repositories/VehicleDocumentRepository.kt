package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.VehicleDocumentAPI
import com.bidbatl.dileveryapp.model.VehicleDocuments
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import javax.inject.Inject

class VehicleDocumentRepository @Inject constructor(private val appExecutors: AppExecutors,
                                                    private val vehicleDocumentAPI: VehicleDocumentAPI,private val preference: Preference){
    fun getVehicleDocuments():LiveData<Resource<VehicleDocuments>>{
        return object :NetworkBoundResource<VehicleDocuments,VehicleDocuments>(appExecutors){
            override fun createCall() = vehicleDocumentAPI.getVehicleDocuments(preference.getString(Common.token),
                preference.getString(Common.PreferenceKey.vehicleid))
        }.asLiveData()
    }
}