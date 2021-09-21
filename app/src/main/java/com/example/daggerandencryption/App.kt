package com.example.daggerandencryption

import android.app.Application
import com.example.daggerandencryption.data.db.realm.RealmDatabase
import com.example.daggerandencryption.data.di.core.CommonInjector
import com.example.daggerandencryption.data.di.core.DispatchingCommonInjector
import com.example.daggerandencryption.data.di.core.HasCommonInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App: Application(), HasAndroidInjector, HasCommonInjector {

    @Inject lateinit var commonInjector: DispatchingCommonInjector
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject lateinit var realm: RealmDatabase

    val appComponent by lazy { DaggerAppComponent.factory().create(this) }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)
    }

    override fun commonInjector(): CommonInjector = commonInjector

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}
