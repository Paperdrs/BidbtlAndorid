package com.android.bidbatl.di

import com.android.bidbatl.repository.RetrofitRepository
import com.android.bidbatl.Activity.product.RetroViewModel
import com.android.bidbatl.repository.RetroViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, APIModule::class])
interface APIComponent {
    fun inject(retrofitRepository: RetrofitRepository)
    fun inject(retroViewModel: RetroViewModel)
    fun inject(retroViewModelFactory: RetroViewModelFactory)
}