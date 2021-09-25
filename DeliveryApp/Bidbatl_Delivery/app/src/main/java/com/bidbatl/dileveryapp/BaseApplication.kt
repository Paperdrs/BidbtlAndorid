package com.bidbatl.dileveryapp

import com.bidbatl.dileveryapp.di.DaggerAppComponent
import com.bidbatl.dileveryapp.model.LiveDelivery
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {
    var kLiveDeliveryData : LiveDelivery? = null
    override fun applicationInjector(): AndroidInjector<out DaggerApplication?>? {
        return DaggerAppComponent.builder().application(this)!!.build()
    }
    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    companion object {
        lateinit var instance: BaseApplication
            private set
    }


}