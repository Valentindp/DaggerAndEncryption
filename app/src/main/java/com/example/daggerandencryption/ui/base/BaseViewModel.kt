package com.example.daggerandencryption.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.daggerandencryption.data.di.core.CommonInjector
import com.example.daggerandencryption.data.di.core.checkHasCommonInjector

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    init {
        injectMembers(application.checkHasCommonInjector().commonInjector())
    }

    private fun injectMembers(commonInjector: CommonInjector) {
        commonInjector.injectMembers(this)
    }
}