package com.example.daggerandencryption.data.di

import dagger.Module

@Module(
    includes = [
        LoginDataSourceModule::class,
        LoginRepositoryModule::class,
        RoomModule::class,
        RoomDaoModule::class
    ]
)
interface SourceModule