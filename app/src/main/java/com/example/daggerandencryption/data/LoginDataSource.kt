package com.example.daggerandencryption.data

import com.example.daggerandencryption.data.model.Credentials
import kotlinx.coroutines.flow.Flow

interface LoginDataSource {

    suspend fun login(login: String, password: String)
    fun observeCredentials(): Flow<Credentials>
    fun observeCredentialsRoom(): Flow<Credentials>

    fun getCredentials(): Credentials
}