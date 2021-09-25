package com.bidbatl.dileveryapp.di.operation

import com.bidbatl.dileveryapp.api.OperationAPI
import com.bidbatl.dileveryapp.repositories.OperationRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named

@Module
class OperationModule {

    @Provides
    fun provideOperationAPI(@Named("test") retrofit: Retrofit):OperationAPI{
        return retrofit.create(OperationAPI::class.java)
    }
    @Provides
    fun provideOperationRepository(appExecutors: AppExecutors,@Named("test") retrofit: Retrofit,preference: Preference):OperationRepository{
        return OperationRepository(appExecutors,retrofit.create(OperationAPI::class.java),preference)
    }

}