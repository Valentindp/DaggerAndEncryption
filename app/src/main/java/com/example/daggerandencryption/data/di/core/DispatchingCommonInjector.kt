package com.example.daggerandencryption.data.di.core

import com.example.daggerandencryption.App
import dagger.MembersInjector
import javax.inject.Inject

class DispatchingCommonInjector @Inject constructor(application: App) : CommonInjector {

    private val subComponentFactories: Map<Class<*>, SubComponent.Factory> =
        application.appComponent.subComponentFactories()

    override fun <T : Any> injectMembers(instance: T) {
        injectMembers(instance.javaClass, instance)
    }

    private fun <T : Any> injectMembers(clazz: Class<T>, instance: T) {
        @Suppress("UNCHECKED_CAST")
        (((subComponentFactories[clazz] ?: membersInjectorError(clazz.simpleName))
            .create() as? MembersInjector<T>) ?: membersInjectorError(clazz.simpleName))
            .injectMembers(instance)
    }

    private fun membersInjectorError(className: String): Nothing =
        error("MembersInjector for class [$className] not found")
}
