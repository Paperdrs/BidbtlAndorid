package com.bidbatl.dileveryapp.di.operation

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.operation.OperationViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class OperationViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(OperationViewModel::class)
    abstract fun bindOperationViewModel(operationViewModel: OperationViewModel):ViewModel
}