package com.matttax.drivebetter.profile.data.token

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class LoginStorageImpl : LoginStorage {

    private val settings: Settings by lazy { Settings() }

    override var token: String?
        get() = settings[StorageKeys.TOKEN.key]
        set(value) {
            settings[StorageKeys.TOKEN.key] = value
        }

    override var profileId: Long?
        get() = settings[StorageKeys.PROFILE_ID.key]
        set(value) {
            settings[StorageKeys.PROFILE_ID.key] = value
        }

    override fun clear() {
        settings.clear()
    }
}
