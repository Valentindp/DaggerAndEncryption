package com.example.daggerandencryption.data

import com.example.daggerandencryption.data.model.Credentials
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(login: String, password: String)
    fun observeCredentials(): Flow<Credentials>

    fun getCredentials(): Credentials
}