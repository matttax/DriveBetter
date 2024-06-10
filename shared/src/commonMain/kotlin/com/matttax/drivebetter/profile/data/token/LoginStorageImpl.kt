package com.matttax.drivebetter.profile.data.token

import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.KmLog
import studio.zebro.kcrypt.getKCrypt

class LoginStorageImpl : LoginStorage {

    private val settings: Settings by lazy { Settings() }
    private val kCrypt = getKCrypt()

    override var token: String?
        get() = kCrypt.getString(StorageKeys.TOKEN.key)
        set(value) {
            kCrypt.saveString(StorageKeys.TOKEN.key, value ?: "")
            log.d { "token saved: $token" }
        }

    override var lastProfile: ProfileDomainModel?
        get() {
            val profileJson = settings.get<String>(StorageKeys.PROFILE_DATA.key)
            return profileJson?.let {
                Json.decodeFromString<ProfileStorageModel>(it).toDomainModel()
            }
        }
        set(value) {
            settings[StorageKeys.PROFILE_DATA.key] = Json.encodeToString(value?.toStorageModel())
            log.d { "profileId saved: $lastProfile" }
        }

    override var avatar: ByteArray?
        get() = settings
            .get<String>(StorageKeys.AVATAR.key)
            ?.split(" ")
            ?.mapNotNull {
                it.toByteOrNull()
            }?.toByteArray()

        set(value) {
            settings[StorageKeys.AVATAR.key] = value?.joinToString(" ")
            log.d { "avatar saved: $avatar" }
        }

    override fun clear() {
        settings.clear()
        log.d { "storage cleared" }
    }

    companion object {
        private val log = KmLog("LoginStorageImpl")
    }
}

@Serializable
data class ProfileStorageModel(
    val name: String? = null,
    val gender: String? = null,
    val city: String? = null,
    val driversLicenseId: String? = null,
    val rating: String? = null,
    val email: String? = null,
    val dateOfBirth: String? = null,
    val dateLicenseIssued: String? = null
)

fun ProfileDomainModel.toStorageModel() = ProfileStorageModel(
    name, gender?.text?.lowercase(), city, driversLicenseId, rating.toString(), email, dateOfBirthString, dateLicenseIssuedString
)

fun ProfileStorageModel.toDomainModel() = ProfileDomainModel(
    name, gender?.asGender(), city, driversLicenseId, rating?.toDoubleOrNull() ?: 0.0, email
).setDateOfBirth(dateOfBirth).setDateOfIssue(dateLicenseIssued)
