package com.example.daggerandencryption.data.di

import android.content.Context
import com.example.daggerandencryption.data.Cryptography
import com.example.daggerandencryption.data.db.realm.RealmDatabase
import com.example.daggerandencryption.data.db.realm.RealmDatabaseImpl
import com.example.daggerandencryption.data.di.core.AppContext
import com.example.daggerandencryption.data.di.core.SubComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import io.realm.Realm
import javax.inject.Singleton

@Module(includes = [RealmBindModule::class])
object RealmModule {

    @Provides
    @Singleton
    fun provideRealm(
        @AppContext context: Context,
        cryptography: Cryptography
    ): RealmDatabase = RealmDatabaseImpl(
        context = context,
        key = cryptography.getExistingKey() ?: cryptography.getNewKey(Realm.ENCRYPTION_KEY_LENGTH)
    ).also { it.init() }

}

@Module(subcomponents = [RealmComponent::class])
interface RealmBindModule {

    @Binds
    @IntoMap
    @ClassKey(RealmDatabaseImpl::class)
    fun bindRealm(factory: RealmComponent.Factory): SubComponent.Factory

}

@Subcomponent
interface RealmComponent : SubComponent<RealmDatabaseImpl> {

    @Subcomponent.Factory
    interface Factory : SubComponent.Factory
}