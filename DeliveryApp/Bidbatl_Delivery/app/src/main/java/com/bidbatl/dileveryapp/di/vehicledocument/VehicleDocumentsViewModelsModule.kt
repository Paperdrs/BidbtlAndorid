package com.bidbatl.dileveryapp.di.vehicledocument

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.vehicledocument.VehicleDocumentsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class VehicleDocumentsViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(VehicleDocumentsViewModel::class)
    abstract fun provideVehicleDocumentViewModel(vehicleDocumentsViewModel: VehicleDocumentsViewModel):ViewModel
}