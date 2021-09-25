package com.bidbatl.dileveryapp.di

import com.bidbatl.dileveryapp.di.live.LiveModule
import com.bidbatl.dileveryapp.di.live.LiveViewModelsModule
import com.bidbatl.dileveryapp.di.operation.OperationModule
import com.bidbatl.dileveryapp.di.operation.OperationViewModelsModule
import com.bidbatl.dileveryapp.ui.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class, OperationModule::class])
    abstract fun contributeHomeFragment(): OperationFragment?

    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class, OperationModule::class])
    abstract fun contributeCollectionSummaryFragment(): CollectionSummaryFragment?

    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class, OperationModule::class])
    abstract fun contributeDeliverySummaryFragment(): DeliverySummaryFragment?

    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class, OperationModule::class])
    abstract fun contributeGoodsReturnedFragment(): GoodsReturnedFragment?

    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class, OperationModule::class])
    abstract fun contributeCustomerFragment(): CustomerListFragment?

    @ContributesAndroidInjector(modules = [LiveViewModelsModule::class, LiveModule::class])
    abstract fun contributeCashFragment(): CashFragment?

    @ContributesAndroidInjector(modules = [LiveViewModelsModule::class, LiveModule::class])
    abstract fun contributeChequeFragment(): ChecqueFragment?


}