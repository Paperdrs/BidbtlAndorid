package com.android.bidbatl.Activity.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.bidbatl.repository.RetrofitRepository

import javax.inject.Inject

class RetroViewModel(retrofitRepository: RetrofitRepository): ViewModel() {

    lateinit var retrofitRepository:RetrofitRepository
    var postInfoLiveData: LiveData<List<String>> = MutableLiveData()

    init {
        this.retrofitRepository  = retrofitRepository
        fetchPostInfoFromRepository()
        }

    fun fetchPostInfoFromRepository(){
//        postInfoLiveData =  retrofitRepository.fetchPostInfoList()
    }


}