package com.bidbatl.dileveryapp.di.live

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.live.LiveViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LiveViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(LiveViewModel::class)
    abstract fun bindLiveViewModel(liveViewModel: LiveViewModel):ViewModel
}