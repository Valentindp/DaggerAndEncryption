package com.example.daggerandencryption.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.AndroidInjection
import kotlin.reflect.KClass

abstract class BaseActivity<VM : ViewModel>(kClass: KClass<VM>) : AppCompatActivity() {

    protected val viewModel: VM by inject(kClass)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
    }

    private fun <VM : ViewModel> inject(kClass: KClass<VM>): Lazy<VM> =
        lazy { ViewModelProvider(this).get(kClass.java) }
}