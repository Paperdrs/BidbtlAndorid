package com.bidbatl.dileveryapp.di.vehicledocument

import com.bidbatl.dileveryapp.api.VehicleDocumentAPI
import com.bidbatl.dileveryapp.repositories.VehicleDocumentRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class VehicleDocumentsModule {
    @Provides
    fun provideVehicleDocumentRepository(appExecutors: AppExecutors,retrofit: Retrofit,preference: Preference):VehicleDocumentRepository{
        return VehicleDocumentRepository(appExecutors,retrofit.create(VehicleDocumentAPI::class.java),preference)
    }
}