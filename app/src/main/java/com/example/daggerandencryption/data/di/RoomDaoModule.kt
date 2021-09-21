package com.example.daggerandencryption.data.di

import com.example.daggerandencryption.data.db.room.CredentialDao
import com.example.daggerandencryption.data.db.room.RoomDatabase
import dagger.Module
import dagger.Provides

@Module(includes = [RoomModule::class])
object RoomDaoModule {

    @Provides
    fun provideCredentialDao(roomDatabase: RoomDatabase): CredentialDao = roomDatabase.credentialDao()
}