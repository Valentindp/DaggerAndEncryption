package com.example.daggerandencryption

import com.example.daggerandencryption.data.di.AppModule
import com.example.daggerandencryption.data.di.core.SubComponent
import com.example.daggerandencryption.ui.login.LoginActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(application: App)

    fun subComponentFactories(): Map<Class<*>, SubComponent.Factory>

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: App): AppComponent
    }

}
