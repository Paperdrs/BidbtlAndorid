package com.android.bidbatl.di

import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(myRetroApplication: MyRetroApplication){

    var myRetroApplication: MyRetroApplication

    init {
        this.myRetroApplication = myRetroApplication
    }

    @Provides
    fun provideMyRetroApplication(): MyRetroApplication {
        return myRetroApplication
    }
}