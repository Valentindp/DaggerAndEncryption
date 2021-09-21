package com.example.daggerandencryption.data.di

import com.example.daggerandencryption.App
import com.example.daggerandencryption.data.LoginDataSource
import com.example.daggerandencryption.data.LoginDataSourceImpl
import com.example.daggerandencryption.data.di.core.SubComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [LoginDataSourceBindModule::class])
object LoginDataSourceModule {

    @Provides
    @Singleton
    fun provideLoginDataSource(application: App): LoginDataSource = LoginDataSourceImpl(application)
}

@Module(subcomponents = [LoginDataComponent::class])
interface LoginDataSourceBindModule {

    @Binds
    @IntoMap
    @ClassKey(LoginDataSourceImpl::class)
    fun bindLoginDataSource(factory: LoginDataComponent.Factory): SubComponent.Factory
}

@Subcomponent
interface LoginDataComponent : SubComponent<LoginDataSourceImpl> {

    @Subcomponent.Factory
    interface Factory : SubComponent.Factory
}