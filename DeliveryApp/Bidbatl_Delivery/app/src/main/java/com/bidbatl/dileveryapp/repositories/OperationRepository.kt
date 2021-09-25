package com.bidbatl.dileveryapp.repositories

import androidx.lifecycle.LiveData
import com.bidbatl.dileveryapp.api.OperationAPI
import com.bidbatl.dileveryapp.model.*
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.NetworkBoundResource
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.MLog
import com.bidbatl.dileveryapp.network_util.network_and_db_resources.vo.Resource
import com.bidbatl.dileveryapp.util.AppExecutors
import com.bidbatl.dileveryapp.util.Common
import com.preference.Preference
import javax.inject.Inject

class OperationRepository @Inject constructor(val appExecutors: AppExecutors,val operationAPI: OperationAPI,val preference: Preference) {
    fun getOrder():LiveData<Resource<OperationModel>>{
        return object : NetworkBoundResource<OperationModel,OperationModel>(appExecutors){
            override fun createCall() = operationAPI.getOrder(preference.getString(Common.token))
        }.asLiveData()

    }
    fun searchOrder(keywords:String):LiveData<Resource<OperationModel>>{
        return object : NetworkBoundResource<OperationModel,OperationModel>(appExecutors){
            override fun createCall() = operationAPI.searchOrder(preference.getString(Common.token),keywords)
        }.asLiveData()

    }


    fun getCollectionSummary(id:String):LiveData<Resource<CollectionSummary>>{
        return object : NetworkBoundResource<CollectionSummary,CollectionSummary>(appExecutors){
            override fun createCall() = operationAPI.getCollectionSummary(id)
        }.asLiveData()

    }


    fun getDeliverySummary(id: String): LiveData<Resource<DeliverySummary>> {
        return object  : NetworkBoundResource<DeliverySummary, DeliverySummary>(appExecutors) {
            override fun createCall() = operationAPI.getDeliverySummary(id)

            override fun onFetchFailed() {
                MLog.e("LearningRepository onFetchFailed")
            }
        }.asLiveData()
    }

    fun getGoodsReturnsDetail(id: String):LiveData<Resource<GoodsReturnDetails>>{
        return object :NetworkBoundResource<GoodsReturnDetails,GoodsReturnDetails>(appExecutors){
            override fun createCall() = operationAPI.getGoodsReturnDetail(id)
        }.asLiveData()
    }
    fun getCustomerList():LiveData<Resource<CustomerResponse>>{
        return object :NetworkBoundResource<CustomerResponse,CustomerResponse>(appExecutors){
            override fun createCall() = operationAPI.getUserList(preference.getString(Common.token))
        }.asLiveData()
    }
}