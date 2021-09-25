package com.bidbatl.dileveryapp.di.reortbreakdown

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.reportbreakdown.ReportBreakDownViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ReportBreakDownViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReportBreakDownViewModel::class)
    abstract fun provideReportBreakDownViewModel(reportBreakDownViewModel: ReportBreakDownViewModel):ViewModel
}