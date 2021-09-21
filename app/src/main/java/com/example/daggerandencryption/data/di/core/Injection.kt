package com.example.daggerandencryption.data.di.core

interface CommonInjector {
    fun <T : Any> injectMembers(instance: T)
}

interface HasCommonInjector {
    fun commonInjector(): CommonInjector
}

fun <T: Any> T.checkHasCommonInjector(): HasCommonInjector = runCatching {
    this as HasCommonInjector
}.getOrElse { error("${this.javaClass.simpleName} does not implement HasCommonInjector") }