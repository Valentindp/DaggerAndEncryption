package com.example.daggerandencryption.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = CredentialsRoom.TABLE_NAME)
data class CredentialsRoom(
    @PrimaryKey val id: Long = 9999,
    @ColumnInfo(name = "login") val login: String,
    @ColumnInfo(name = "password") val password: String
) {

    companion object {
        const val TABLE_NAME = "Credentials"
    }
}

fun CredentialsRoom.toDomain() = Credentials(login, password)

