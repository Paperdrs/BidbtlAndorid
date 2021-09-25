package com.bidbatl.dileveryapp.di.live

import com.bidbatl.dileveryapp.api.GoogleMapAPI
import com.bidbatl.dileveryapp.api.LiveApi
import com.bidbatl.dileveryapp.repositories.LiveRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class LiveModule {

    @Provides
    fun provideLiveRepository(appExecutors: AppExecutors, retrofit: Retrofit, @Named("Google") gMapretrofit: Retrofit, preference: Preference):LiveRepository{
        return LiveRepository(appExecutors,retrofit.create(LiveApi::class.java),gMapretrofit.create(GoogleMapAPI::class.java),preference)

    }

}