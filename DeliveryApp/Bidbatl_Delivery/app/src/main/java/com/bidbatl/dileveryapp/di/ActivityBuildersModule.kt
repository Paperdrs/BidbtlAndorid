package com.bidbatl.dileveryapp.di

import com.bidbatl.dileveryapp.SplashActivity
import com.bidbatl.dileveryapp.di.live.LiveModule
import com.bidbatl.dileveryapp.di.live.LiveViewModelsModule
import com.bidbatl.dileveryapp.di.login.LoginModule
import com.bidbatl.dileveryapp.di.login.LoginViewModelsModule
import com.bidbatl.dileveryapp.di.operation.OperationModule
import com.bidbatl.dileveryapp.di.operation.OperationViewModelsModule
import com.bidbatl.dileveryapp.di.profile.ProfileModule
import com.bidbatl.dileveryapp.di.profile.ProfileViewModelsModule
import com.bidbatl.dileveryapp.di.reortbreakdown.ReportBreakDownModule
import com.bidbatl.dileveryapp.di.reortbreakdown.ReportBreakDownViewModelsModule
import com.bidbatl.dileveryapp.di.reportnondeliver.ReportNonDeliverModule
import com.bidbatl.dileveryapp.di.reportnondeliver.ReportNonDeliverViewModelsModule
import com.bidbatl.dileveryapp.di.scope.AuthScope
import com.bidbatl.dileveryapp.di.scope.LiveScope
import com.bidbatl.dileveryapp.di.scope.OperationScope
import com.bidbatl.dileveryapp.di.vehicledocument.VehicleDocumentsModule
import com.bidbatl.dileveryapp.di.vehicledocument.VehicleDocumentsViewModelsModule
import com.bidbatl.dileveryapp.ui.live.AcceptPaymentActivity
import com.bidbatl.dileveryapp.ui.live.FullMapActivity
import com.bidbatl.dileveryapp.ui.live.HomeActivity
import com.bidbatl.dileveryapp.ui.live.OtpVerificationActivity
import com.bidbatl.dileveryapp.ui.login.LoginActivity
import com.bidbatl.dileveryapp.ui.login.MainActivity
import com.bidbatl.dileveryapp.ui.operation.OperationActivity
import com.bidbatl.dileveryapp.ui.operation.SummaryDetailActivity
import com.bidbatl.dileveryapp.ui.profile.MyProfileActivity
import com.bidbatl.dileveryapp.ui.reportbreakdown.ReportBreakDownActivity
import com.bidbatl.dileveryapp.ui.reportnondeliver.ReportNonDeliveryActivity
import com.bidbatl.dileveryapp.ui.vehicledocument.VehicleDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(modules =[LoginViewModelsModule::class , LoginModule::class])
    abstract fun provideLoginActivity(): LoginActivity


    @LiveScope
    @ContributesAndroidInjector(modules = [LoginViewModelsModule::class,LoginModule::class])
    abstract fun provideOtpVerificationActivity(): OtpVerificationActivity

    @AuthScope
    @ContributesAndroidInjector
    abstract fun provideSplashActivity(): SplashActivity

    @AuthScope
    @ContributesAndroidInjector
    abstract fun provideMainActivity(): MainActivity

    @OperationScope
    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class,OperationModule::class])
    abstract fun provideOperationActivity():OperationActivity

    @OperationScope
    @ContributesAndroidInjector(modules = [OperationViewModelsModule::class,OperationModule::class])
    abstract fun provideSummaryDetailActivity():SummaryDetailActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [LiveViewModelsModule::class,LiveModule::class])
    abstract fun provideHomeActivity():HomeActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [LiveViewModelsModule::class,LiveModule::class])
    abstract fun provideAcceptFullMapActivity():FullMapActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [LiveViewModelsModule::class,LiveModule::class])
    abstract fun provideAcceptPaymentActivity():AcceptPaymentActivity




    @LiveScope
    @ContributesAndroidInjector(modules = [ProfileViewModelsModule::class,ProfileModule::class])
    abstract fun provideMyProfileAActivity():MyProfileActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [ReportBreakDownViewModelsModule::class,ReportBreakDownModule::class])
    abstract fun provideReportBreakDownActivity():ReportBreakDownActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [ReportNonDeliverViewModelsModule::class,ReportNonDeliverModule::class])
    abstract fun provideReportNonDeliveryActivity():ReportNonDeliveryActivity

    @LiveScope
    @ContributesAndroidInjector(modules = [VehicleDocumentsViewModelsModule::class,VehicleDocumentsModule::class])
    abstract fun provideVehicleDetailActivity():VehicleDetailActivity



}