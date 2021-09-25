package com.bidbatl.dileveryapp.di.reportnondeliver

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.reportnondeliver.ReportNonDeliverViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReportNonDeliverViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReportNonDeliverViewModel::class)
    abstract fun provideReportNonDeliverViewModel(reportNonDeliverViewModel: ReportNonDeliverViewModel):ViewModel
}