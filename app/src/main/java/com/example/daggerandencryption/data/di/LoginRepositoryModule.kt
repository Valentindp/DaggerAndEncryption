package com.example.daggerandencryption.data.di

import com.example.daggerandencryption.App
import com.example.daggerandencryption.data.LoginRepository
import com.example.daggerandencryption.data.LoginRepositoryImpl
import com.example.daggerandencryption.data.di.core.SubComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module(includes = [LoginRepositoryBindModule::class])
object LoginRepositoryModule {

    @Provides
    @Singleton
    fun provideLoginRepository(application: App): LoginRepository = LoginRepositoryImpl(application)
}

@Module(subcomponents = [LoginRepositoryComponent::class])
interface LoginRepositoryBindModule {

    @Binds
    @IntoMap
    @ClassKey(LoginRepositoryImpl::class)
    fun bindLoginRepository(factory: LoginRepositoryComponent.Factory): SubComponent.Factory
}

@Subcomponent
interface LoginRepositoryComponent : SubComponent<LoginRepositoryImpl> {

    @Subcomponent.Factory
    interface Factory : SubComponent.Factory
}