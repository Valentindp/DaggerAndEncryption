package com.example.daggerandencryption.data.di.core

import dagger.MembersInjector

interface SubComponent<T> : MembersInjector<T> {

    interface Factory {
        fun create(): SubComponent<*>
    }
}
