package com.example.daggerandencryption.data.db.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.daggerandencryption.data.model.Credentials
import com.example.daggerandencryption.data.model.CredentialsRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CredentialDao {

    @Query("DELETE FROM ${CredentialsRoom.TABLE_NAME}")
    fun clearTable()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(credentials: CredentialsRoom)

    @Query("SELECT * FROM ${CredentialsRoom.TABLE_NAME} limit 1")
    fun observeCredentials(): Flow<CredentialsRoom>

}