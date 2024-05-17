package com.matttax.drivebetter.profile.data.network.model.edit

import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditResponseBody(
    @SerialName("name") val name: String,
    @SerialName("gender") val gender: String,
    @SerialName("city") val city: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("licence") val license: String,
    @SerialName("email") val email: String,
    @SerialName("licence_received") val licenseReceived: String? = null,
)

fun EditResponseBody.toDomainModel() = ProfileDomainModel(
    name = name,
    gender = gender.asGender(),
    city = city,
    driversLicenseId = license,
    email = email,
).setDateOfBirth(birthDate).setDateOfIssue(licenseReceived)

fun EditResponseBody.toDomainModelWithToken(token: String) = ProfileDomainModel(
    name = name,
    gender = gender.asGender(),
    city = city,
    driversLicenseId = license,
    email = email,
)
    .setDateOfBirth(birthDate)
    .setDateOfIssue(licenseReceived ?: birthDate)
    .also {
        it.token = token
    }
