package com.bidbatl.dileveryapp.di.reportnondeliver

import com.bidbatl.dileveryapp.api.ReportNonDeliverAPI
import com.bidbatl.dileveryapp.repositories.ReportNonDeliverRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ReportNonDeliverModule {

    @Provides
    fun provideReportNonDeliverRepository(appExecutors: AppExecutors,retrofit: Retrofit,preference: Preference):ReportNonDeliverRepository{
        return ReportNonDeliverRepository(appExecutors,retrofit.create(ReportNonDeliverAPI::class.java),preference)
    }
}