package com.bidbatl.dileveryapp.di.reortbreakdown

import com.bidbatl.dileveryapp.api.ReportBreakDownAPI
import com.bidbatl.dileveryapp.repositories.ReportBreakDownRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class ReportBreakDownModule {
    @Provides
    fun provideReportBreakDownRepository(appExecutors: AppExecutors,retrofit: Retrofit,preference: Preference):ReportBreakDownRepository{
        return ReportBreakDownRepository(appExecutors,retrofit.create(ReportBreakDownAPI::class.java),preference)
    }
}