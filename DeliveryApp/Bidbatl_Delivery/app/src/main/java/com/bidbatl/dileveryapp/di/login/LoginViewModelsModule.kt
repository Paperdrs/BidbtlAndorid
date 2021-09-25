package com.bidbatl.dileveryapp.di.login

import androidx.lifecycle.ViewModel
import com.bidbatl.dileveryapp.di.ViewModelKey
import com.bidbatl.dileveryapp.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel
}