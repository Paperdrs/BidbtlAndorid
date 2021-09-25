package com.bidbatl.dileveryapp.ui.operation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.model.data.OperationData
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.repositories.OperationRepository
import javax.inject.Inject

class OperationViewModel @Inject constructor(private val operationRepository:OperationRepository):ViewModel(){

    var operationModelLiveData = MutableLiveData<OperationData>()
    var customerLiveData = MutableLiveData<Customer>()

    fun getOrder():LiveData<Resource<OperationModel>>{
        return operationRepository.getOrder()
    }
    fun searchOrder(keyword:String):LiveData<Resource<OperationModel>>{
        return operationRepository.searchOrder(keyword)
    }
    fun getCustomerList():LiveData<Resource<CustomerResponse>>{
        return operationRepository.getCustomerList()
    }
    fun getCollectionSummary(id:String):LiveData<Resource<CollectionSummary>>{
        return operationRepository.getCollectionSummary(id)
    }
    fun getDeliverySummary(id: String):LiveData<Resource<DeliverySummary>>{
        return operationRepository.getDeliverySummary(id)
    }
    fun getGoodsReturnDetail(id: String):LiveData<Resource<GoodsReturnDetails>>{
        return operationRepository.getGoodsReturnsDetail(id)
    }
    fun setSelectedOrder(operationModel: OperationData){
        operationModelLiveData.value = operationModel
    }
    fun observeSelectedOrder():LiveData<OperationData>{
        return operationModelLiveData
    }
    fun setCustomer(customer: Customer){
        customerLiveData.value = customer
    }
    fun getCustomer():LiveData<Customer>{
        return customerLiveData
    }

}