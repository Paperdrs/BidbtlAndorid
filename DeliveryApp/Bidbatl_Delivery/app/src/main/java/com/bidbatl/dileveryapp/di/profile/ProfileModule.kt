package com.bidbatl.dileveryapp.di.profile

import com.bidbatl.dileveryapp.api.ProfileAPI
import com.bidbatl.dileveryapp.repositories.ProfileRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import com.preference.Preference
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
 class ProfileModule {
    @Provides
    fun provideProfileRepository(appExecutors: AppExecutors,retrofit: Retrofit,preference: Preference):ProfileRepository{
        return ProfileRepository(appExecutors,retrofit.create(ProfileAPI::class.java),preference)
    }
}