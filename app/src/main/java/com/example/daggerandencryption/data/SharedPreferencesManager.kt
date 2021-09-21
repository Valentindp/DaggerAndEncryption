package com.example.daggerandencryption.data

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesManager(context: Context) {

    companion object {
        private const val PREF = "prefs"
        private const val PREF_LOGIN = "login"
        private const val PREF_PASSWORD = "password"
        private const val PREF_REALM_KEY = "realm_key"
    }

    private val prefs by lazy { getSharedPreferences(context) }
    private var _login by prefs.nullableString(key = PREF_LOGIN)
    private var _password by prefs.nullableString(key = PREF_PASSWORD)
    private var _databaseEncryptionKey by prefs.nullableString(key = PREF_REALM_KEY)

    var login: String?
        get() = _login
        set(value) {
            _login = value
        }

    var password: String?
        get() = _password
        set(value) {
            _password = value
        }

    var databaseEncryptionKey: String?
        get() = _databaseEncryptionKey
        set(value) {
            _databaseEncryptionKey = value
        }

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            PREF,
            getMasterKey(context),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getMasterKey(context: Context): MasterKey {
        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()

        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
    }

    private fun SharedPreferences.nullableString(
        defaultValue: String? = null,
        key: String? = null
    ): ReadWriteProperty<Any, String?> {
        return delegate(defaultValue, key, SharedPreferences::getString, SharedPreferences.Editor::putString)
    }

    private inline fun <T> SharedPreferences.delegate(
        defaultValue: T,
        key: String?,
        crossinline getter: SharedPreferences.(String, T) -> T,
        crossinline setter: SharedPreferences.Editor.(String, T) -> Unit
    ): ReadWriteProperty<Any, T> {
        return object : ReadWriteProperty<Any, T> {

            override fun getValue(thisRef: Any, property: KProperty<*>): T {
                return getter(key ?: property.name, defaultValue)
            }

            override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
                edit().apply {
                    setter(key ?: property.name, value)
                    apply()
                }
            }

        }
    }
}