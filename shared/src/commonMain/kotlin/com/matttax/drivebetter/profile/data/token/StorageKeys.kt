package com.matttax.drivebetter.profile.data.token

enum class StorageKeys {
    TOKEN,
    AVATAR,
    PROFILE_DATA;

    val key: String
        get() = this.name
}
