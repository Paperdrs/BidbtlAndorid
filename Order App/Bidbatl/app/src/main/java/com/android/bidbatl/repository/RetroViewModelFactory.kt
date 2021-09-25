package com.android.bidbatl.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.bidbatl.Activity.product.RetroViewModel
import com.android.bidbatl.di.MyRetroApplication
import com.android.bidbatl.di.APIComponent
import com.android.bidbatl.di.APIModule
import com.android.bidbatl.di.DaggerAPIComponent
import javax.inject.Inject


class RetroViewModelFactory : ViewModelProvider.Factory {
    lateinit var apiComponent: APIComponent
    @Inject
    lateinit var retrofitRepository: RetrofitRepository

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
     //   initDaggerComponent()
       var apiComponent : APIComponent =  MyRetroApplication.apiComponent
        apiComponent.inject(this)
        if (modelClass.isAssignableFrom(RetroViewModel::class.java)) {
            return RetroViewModel(retrofitRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    fun initDaggerComponent(){
        apiComponent =   DaggerAPIComponent
            .builder()
            .aPIModule(APIModule(APIURL.BASE_URL))
            .build()
        apiComponent.inject(this)
    }
}