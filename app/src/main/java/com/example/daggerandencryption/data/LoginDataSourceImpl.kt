package com.example.daggerandencryption.data

import android.util.Log
import com.example.daggerandencryption.App
import com.example.daggerandencryption.data.base.BaseDataSource
import com.example.daggerandencryption.data.db.realm.RealmThread
import com.example.daggerandencryption.data.db.room.CredentialDao
import com.example.daggerandencryption.data.model.*
import io.realm.Realm
import io.realm.kotlin.toFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginDataSourceImpl(application: App) : BaseDataSource(application), LoginDataSource {

    @Inject
    lateinit var preferences: SharedPreferencesManager

    @Inject
    lateinit var credentialDao: CredentialDao

    override suspend fun login(login: String, password: String) {
        preferences.login = login
        preferences.password = password
        saveCredentialsRealm(Credentials(login, password))
        saveCredentialsRoom(Credentials(login, password))
        Log.w("Pref", "Credentials saved")
    }

    override fun getCredentials() = Credentials(preferences.login.orEmpty(), preferences.password.orEmpty())

    override fun observeCredentials(): Flow<Credentials> = flow {
        emitAll(
            Realm.getDefaultInstance().where(CredentialsRealm::class.java).findAll().toFlow()
                .map { credentials ->
                    credentials.firstOrNull()
                }.mapNotNull { credentials -> credentials?.toDomain() }
        )
    }.flowOn(RealmThread.dispatcher)

    override fun observeCredentialsRoom(): Flow<Credentials> = credentialDao.observeCredentials()
        .mapNotNull {
            it.toDomain()
        }.distinctUntilChanged()

    private fun saveCredentialsRealm(credentials: Credentials) {
        Realm.getDefaultInstance().use { realm ->
            realm.executeTransaction {
                it.copyToRealmOrUpdate(credentials.toRealm())
            }
        }
    }

    private suspend fun saveCredentialsRoom(credentials: Credentials) {
        withContext(Dispatchers.IO) {
            credentialDao.insert(credentials.toRoom())
        }
    }

}