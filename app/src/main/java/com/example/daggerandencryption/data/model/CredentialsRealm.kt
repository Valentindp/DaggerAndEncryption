package com.example.daggerandencryption.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class CredentialsRealm(
    var login: String = "",
    var password: String = ""
) : RealmObject() {

    @PrimaryKey
    private var id: Int = 9999
}

fun CredentialsRealm.toDomain() = Credentials(login, password)