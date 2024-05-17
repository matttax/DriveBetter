package com.matttax.drivebetter.profile.data.network.model.registration

import com.matttax.drivebetter.profile.data.network.RegistrationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationErrorHolder(
    @SerialName("uuid") val uuid: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("birth_date") val birthDate: String? = null,
    @SerialName("licence") val license: String? = null,
    @SerialName("licence_received") val licenseReceived: String? = null,
    @SerialName("email") val email: List<String>? = null,
    @SerialName("password") val password: String? = null
)

fun RegistrationErrorHolder.toThrowable(code: Int): Throwable {
    val message = buildString {
        append("Registration failed ($code): " )
        if (uuid != null) append("uuid: $uuid, ")
        if (name != null) append("name: $name, ")
        if (gender != null) append("gender: $gender, ")
        if (birthDate != null) append("birth_date: $birthDate, ")
        if (license != null) append("licence: $license, ")
        if (licenseReceived != null) append("licence_received: $licenseReceived, ")
        if (email != null) append("email: ${email.joinToString(", ")}, ")
        if (password != null) append("password: $password, ")
    }
    return RegistrationException(message)
}
