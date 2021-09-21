package com.example.daggerandencryption.data

import android.app.Application
import com.example.daggerandencryption.data.base.BaseDataSource
import com.example.daggerandencryption.data.model.Credentials
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginRepositoryImpl(application: Application) : BaseDataSource(application), LoginRepository {

    @Inject
    lateinit var dataSource: LoginDataSource

    override suspend fun login(login: String, password: String) {
        dataSource.login(login, password)
    }

    override fun observeCredentials(): Flow<Credentials> =  dataSource.observeCredentials()

    override fun getCredentials(): Credentials = dataSource.getCredentials()

}