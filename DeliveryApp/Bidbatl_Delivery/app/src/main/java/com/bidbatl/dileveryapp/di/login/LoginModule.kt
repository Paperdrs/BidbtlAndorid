package com.bidbatl.dileveryapp.di.login

import com.bidbatl.dileveryapp.api.LoginAPI
import com.bidbatl.dileveryapp.repositories.LoginRepository
import com.bidbatl.dileveryapp.util.AppExecutors
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class LoginModule {

    @Provides
    fun provideLoginAPI(retrofit: Retrofit): LoginAPI {
        return retrofit.create(LoginAPI::class.java)
    }

    @Provides
    fun provideLoginRepository(appExecutors: AppExecutors, loginService:Retrofit): LoginRepository {
        return LoginRepository(appExecutors,loginService.create(LoginAPI::class.java))

    }
}