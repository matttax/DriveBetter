package com.matttax.drivebetter.profile.data.token

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.KmLog

class LoginStorageImpl : LoginStorage {

    private val settings: Settings by lazy { Settings() }

    override var token: String?
        get() = settings[StorageKeys.TOKEN.key]
        set(value) {
            settings[StorageKeys.TOKEN.key] = value
            log.d { "token saved: $token" }
        }

    override var lastProfile: ProfileDomainModel?
        get() {
            val profileJson = settings.get<String>(StorageKeys.PROFILE_DATA.key)
            return profileJson?.let {
                Json.decodeFromString<ProfileDomainModel>(it)
            }
        }
        set(value) {
            settings[StorageKeys.PROFILE_DATA.key] = Json.encodeToString(value)
            log.d { "profileId saved: $lastProfile" }
        }

    override var avatar: ByteArray?
        get() = null
        set(value) {}

    override fun clear() {
        settings.clear()
        log.d { "storage cleared" }
    }

    companion object {
        private val log = KmLog("LoginStorageImpl")
    }
}
