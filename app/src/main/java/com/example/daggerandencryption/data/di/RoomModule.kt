package com.example.daggerandencryption.data.di

import android.content.Context
import com.example.daggerandencryption.data.Cryptography
import com.example.daggerandencryption.data.db.room.RoomDatabase
import com.example.daggerandencryption.data.di.core.AppContext
import dagger.Module
import dagger.Provides

@Module
object RoomModule {

    @Provides
    fun provideLocalDatabase(
        @AppContext context: Context,
        cryptography: Cryptography
    ): RoomDatabase = RoomDatabase(
        context = context,
        key = cryptography.getExistingKey() ?: cryptography.getNewKey(RoomDatabase.ENCRYPTION_KEY_LENGTH)
    )
}