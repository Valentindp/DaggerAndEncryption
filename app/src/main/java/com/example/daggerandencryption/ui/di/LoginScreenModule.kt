package com.example.daggerandencryption.ui.di

import com.example.daggerandencryption.data.di.core.SubComponent
import com.example.daggerandencryption.ui.login.LoginActivity
import com.example.daggerandencryption.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [LoginViewModelComponent::class])
interface LoginScreenModule {

    //@ContributesAndroidInjector(modules = [LoginActivityModule::class])
    @ContributesAndroidInjector
    fun bindActivity(): LoginActivity

    @Binds
    @IntoMap
    @ClassKey(LoginViewModel::class)
    fun bindViewModel(factory: LoginViewModelComponent.Factory): SubComponent.Factory
}

@Subcomponent
interface LoginViewModelComponent : SubComponent<LoginViewModel> {

    @Subcomponent.Factory
    interface Factory : SubComponent.Factory
}

/*@Module
class LoginActivityModule {

    @Provides
    fun viewModule(activity: LoginActivity): LoginViewModel =
        ViewModelProvider(activity).get(LoginViewModel::class.java)
} */