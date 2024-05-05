package com.matttax.drivebetter.profile.data.token

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import org.lighthousegames.logging.KmLog

class LoginStorageImpl : LoginStorage {

    private val settings: Settings by lazy { Settings() }

    override var token: String?
        get() = settings[StorageKeys.TOKEN.key]
        set(value) {
            settings[StorageKeys.TOKEN.key] = value
            log.d { "token saved: $token" }
        }

    override var profileId: Long?
        get() = settings[StorageKeys.PROFILE_ID.key]
        set(value) {
            settings[StorageKeys.PROFILE_ID.key] = value
            log.d { "profileId saved: $profileId" }
        }

    override fun clear() {
        settings.clear()
        log.d { "storage cleared" }
    }

    companion object {
        private val log = KmLog("LoginStorageImpl")
    }
}
