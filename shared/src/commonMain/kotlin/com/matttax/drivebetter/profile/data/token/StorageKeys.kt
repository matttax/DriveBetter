package com.matttax.drivebetter.profile.data.token

enum class StorageKeys {
    TOKEN,
    PROFILE_ID;

    val key: String
        get() = this.name
}
