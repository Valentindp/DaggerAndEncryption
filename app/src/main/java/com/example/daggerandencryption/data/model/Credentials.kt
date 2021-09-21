package com.example.daggerandencryption.data.model

data class Credentials(val login: String, val password: String)

fun Credentials.toRealm() = CredentialsRealm(login, password)

fun Credentials.toRoom() = CredentialsRoom(login = login, password = password)