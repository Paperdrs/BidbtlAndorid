package com.bidbatl.dileveryapp.di

import android.content.Context
import com.bidbatl.dileveryapp.BaseApplication
import com.bidbatl.dileveryapp.network_util.LiveDataCallAdapterFactory
import com.bidbatl.dileveryapp.util.Constants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.preference.PowerPreference
import com.preference.Preference
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule {
    val jsonBuilder: Gson = GsonBuilder().setLenient().create()
    @Singleton
    @Provides
    fun provideApplicationContext(application: BaseApplication): Context{
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providePowerPreference(): Preference{
        return PowerPreference.getDefaultFile()
    }

    @Singleton
    @Provides
    fun provideRetrofitInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        return Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    @Singleton
    @Named("Google")
    @Provides
    fun provideRetrofitInstanceGoogleMap(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        return Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(Constants.MAP_BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("test")
    fun provideRetrofitTestInstance(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        return Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(Constants.BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(jsonBuilder))
            .build()
    }


    @Singleton
    @Provides
    fun provideConnectionDetector(): Boolean {
        val connectionDetector = DaggerConnectionDetectorComponent.builder().build()
        return connectionDetector.connectionDetector().init()
    }

}