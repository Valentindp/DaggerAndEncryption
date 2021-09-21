package com.example.daggerandencryption.data.db.realm

import android.content.Context
import com.example.daggerandencryption.data.Cryptography
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.coroutines.RealmFlowFactory
import java.io.File

class RealmDatabaseImpl(
    private val context: Context,
    private val cryptography: Cryptography
) : RealmDatabase {

    companion object {
        private const val ENCRYPTION_FILE_PREFIX = "enc_"
    }

    override fun init() {
        Realm.init(context)
        val realmConfiguration = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .flowFactory(RealmFlowFactory(true))

        createEncryptedRealm(realmConfiguration)
    }

    private fun createEncryptedRealm(builder: RealmConfiguration.Builder) {
        val unencryptedConfig = builder.build()
        val encryptedConfig = builder.name(ENCRYPTION_FILE_PREFIX + unencryptedConfig.realmFileName)
            .encryptionKey(cryptography.getExistingKey() ?: cryptography.getNewKey(Realm.ENCRYPTION_KEY_LENGTH))
            .build()
        migrationIfNeeded(unencryptedConfig, encryptedConfig)
        Realm.setDefaultConfiguration(encryptedConfig)
    }

    private fun migrationIfNeeded(
        unencryptedConfig: RealmConfiguration,
        encryptedConfig: RealmConfiguration
    ) {
        val unencryptedFile = File(unencryptedConfig.path)
        val encryptedFile = File(encryptedConfig.path)
        var unencryptedRealm: Realm? = null
        if (!encryptedFile.exists() && unencryptedFile.exists()) {
            try {
                unencryptedRealm = Realm.getInstance(unencryptedConfig)
                unencryptedRealm.writeEncryptedCopyTo(encryptedFile, encryptedConfig.encryptionKey)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (unencryptedRealm != null) {
                    unencryptedRealm.close()
                    unencryptedFile.delete()
                }
            }
        }
    }

}