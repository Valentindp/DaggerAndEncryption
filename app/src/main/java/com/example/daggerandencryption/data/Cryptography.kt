package com.example.daggerandencryption.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class Cryptography(private val preferences: SharedPreferencesManager) {

    companion object {
        private const val ANDROID_KEY_STORE_NAME = "AndroidKeyStore"
        private const val DATABASE_KEY_ALIAS = "database_key"
    }

    // Create a key to encrypt a realm and save it securely in the keystore
    fun getNewKey(keyLength: Int): ByteArray {
        // open a connection to the android keystore
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
            keyStore.load(null)
        } catch (e: Exception) {
            Log.v("Cryptography", "Failed to open the keystore.")
            throw RuntimeException(e)
        }
        // create a securely generated random asymmetric RSA key
        val databaseKey = ByteArray(keyLength)
        SecureRandom().nextBytes(databaseKey)
        // create a cipher that uses AES encryption -- we'll use this to encrypt our key
        val cipher: Cipher = try {
            Cipher.getInstance(
                KeyProperties.KEY_ALGORITHM_AES
                        + "/" + KeyProperties.BLOCK_MODE_CBC
                        + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7
            )
        } catch (e: Exception) {
            Log.e("Cryptography", "Failed to create a cipher.")
            throw RuntimeException(e)
        }
        // generate secret key
        val keyGenerator: KeyGenerator = try {
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE_NAME)
        } catch (e: NoSuchAlgorithmException) {
            Log.e("Cryptography", "Failed to access the key generator.")
            throw RuntimeException(e)
        }
        val keySpec = KeyGenParameterSpec.Builder(
            DATABASE_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .build()
        try {
            keyGenerator.init(keySpec)
        } catch (e: InvalidAlgorithmParameterException) {
            Log.e("Cryptography", "Failed to generate a secret key.")
            throw RuntimeException(e)
        }
        keyGenerator.generateKey()
        // access the generated key in the android keystore, then
        // use the cipher to create an encrypted version of the key
        val initializationVector: ByteArray
        val encryptedKeyForRealm: ByteArray
        try {
            val secretKey = keyStore.getKey(DATABASE_KEY_ALIAS, null) as SecretKey
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            encryptedKeyForRealm = cipher.doFinal(databaseKey)
            initializationVector = cipher.iv
        } catch (e: Exception) {
            Log.e("Cryptography", "Failed encrypting the key with the secret key.")
            throw RuntimeException(e)
        }
        // keep the encrypted key in shared preferences
        // to persist it across application runs
        val initializationVectorAndEncryptedKey = ByteArray(
            Integer.BYTES +
                    initializationVector.size +
                    encryptedKeyForRealm.size
        )

        ByteBuffer.wrap(initializationVectorAndEncryptedKey).apply {
            order(ByteOrder.BIG_ENDIAN)
            putInt(initializationVector.size)
            put(initializationVector)
            put(encryptedKeyForRealm)
        }

        preferences.databaseEncryptionKey = Base64.encodeToString(initializationVectorAndEncryptedKey, Base64.NO_WRAP)

        return databaseKey // pass to a realm configuration via encryptionKey()
    }

    // Access the encrypted key in the keystore, decrypt it with the secret,
    // and use it to open and read from the realm again
    fun getExistingKey(): ByteArray? {
        // open a connection to the android keystore
        val keyStore: KeyStore
        try {
            keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_NAME)
            keyStore.load(null)
        } catch (e: Exception) {
            Log.e("Cryptography", "Failed to open the keystore.")
            throw RuntimeException(e)
        }
        // access the encrypted key that's stored in shared preferences
        val encodedKey = preferences.databaseEncryptionKey
        if (encodedKey == null) {
            return null
        } else {
            val initializationVectorAndEncryptedKey = Base64.decode(encodedKey, Base64.DEFAULT)
            val buffer = ByteBuffer.wrap(initializationVectorAndEncryptedKey)
            buffer.order(ByteOrder.BIG_ENDIAN)
            // extract the length of the initialization vector from the buffer
            val initializationVectorLength = buffer.int
            // extract the initialization vector based on that length
            val initializationVector = ByteArray(initializationVectorLength)
            buffer[initializationVector]
            // extract the encrypted key
            val encryptedKey = ByteArray(
                initializationVectorAndEncryptedKey.size
                        - Integer.BYTES
                        - initializationVectorLength
            )
            buffer[encryptedKey]
            // create a cipher that uses AES encryption to decrypt our key
            val cipher: Cipher = try {
                Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES
                            + "/" + KeyProperties.BLOCK_MODE_CBC
                            + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7
                )
            } catch (e: Exception) {
                Log.e("Cryptography", "Failed to create cipher.")
                throw RuntimeException(e)
            }

            // decrypt the encrypted key with the secret key stored in the keystore
            return try {
                val secretKey = keyStore.getKey(DATABASE_KEY_ALIAS, null) as SecretKey
                val initializationVectorSpec = IvParameterSpec(initializationVector)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, initializationVectorSpec)
                cipher.doFinal(encryptedKey)
            } catch (e: InvalidKeyException) {
                Log.e("Cryptography", "Failed to decrypt. Invalid key.")
                throw RuntimeException(e)
            } catch (e: Exception) {
                Log.e(
                    "Cryptography",
                    "Failed to decrypt the encrypted realm key with the secret key."
                )
                throw RuntimeException(e)
            }// pass to a realm configuration via encryptionKey()
        }
    }
}