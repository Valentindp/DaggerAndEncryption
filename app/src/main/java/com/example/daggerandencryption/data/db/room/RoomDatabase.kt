package com.example.daggerandencryption.data.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.daggerandencryption.data.model.CredentialsRoom
import net.sqlcipher.database.SupportFactory

@Database(
    version = 1,
    entities = [CredentialsRoom::class]
)
abstract class RoomDatabase : androidx.room.RoomDatabase() {

    abstract fun credentialDao(): CredentialDao

    companion object {
        const val ENCRYPTION_KEY_LENGTH = 64
        private const val ROOM_DATABASE_NAME = "room.db"

        @Volatile
        private var instant: RoomDatabase? = null

        private val LOCK = Any()

        operator fun invoke(context: Context, key: ByteArray) = instant ?: synchronized(LOCK) {
            instant ?: buildDatabase(context, key).also { instant = it }
        }

        private fun buildDatabase(context: Context, key: ByteArray): RoomDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                RoomDatabase::class.java, ROOM_DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .openHelperFactory(SupportFactory(key))
                .build()
        }
    }
}