package com.example.daggerandencryption.data.di

import android.content.Context
import com.example.daggerandencryption.App
import com.example.daggerandencryption.data.Cryptography
import com.example.daggerandencryption.data.SharedPreferencesManager
import com.example.daggerandencryption.data.di.core.AppContext
import com.example.daggerandencryption.ui.di.PresentationModule
import dagger.Module
import dagger.Provides
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Module(
    includes = [
        AndroidInjectionModule::class,
        PresentationModule::class,
        SourceModule::class,
        RealmModule::class
    ]
)
class AppModule {

    @Provides
    @AppContext
    fun appContext(application: App): Context = application

    @Provides
    @Singleton
    fun provideSharedPreferences(@AppContext context: Context) = SharedPreferencesManager(context)

    @Provides
    @Singleton
    fun provideCryptography(preferences: SharedPreferencesManager) = Cryptography(preferences)
}