package com.bidbatl.dileveryapp.ui.vehicledocument

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.VehicleDocuments
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.VehicleDocumentRepository
import javax.inject.Inject

class VehicleDocumentsViewModel @Inject constructor(private val vehicleDocumentRepository: VehicleDocumentRepository) :ViewModel() {
    fun test(){
        println("VehicleDocumentsViewModel")
    }
    fun getVehicleDetail():LiveData<Resource<VehicleDocuments>>{
        return vehicleDocumentRepository.getVehicleDocuments()
    }
}