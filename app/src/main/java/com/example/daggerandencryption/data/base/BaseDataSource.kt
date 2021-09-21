package com.example.daggerandencryption.data.base

import android.app.Application
import com.example.daggerandencryption.data.di.core.CommonInjector
import com.example.daggerandencryption.data.di.core.checkHasCommonInjector

abstract class BaseDataSource(application: Application) {

    init {
        injectMembers(application.checkHasCommonInjector().commonInjector())
    }

    private fun injectMembers(commonInjector: CommonInjector) {
        commonInjector.injectMembers(this)
    }

}