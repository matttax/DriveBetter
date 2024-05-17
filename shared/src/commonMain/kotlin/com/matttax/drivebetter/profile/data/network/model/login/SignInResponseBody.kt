package com.matttax.drivebetter.profile.data.network.model.login

import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignInResponseBody(
    @SerialName("email") val email: String,
    @SerialName("name") val name: String,
    @SerialName("gender") val gender: String,
    @SerialName("city") val city: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("licence") val license: String,
    @SerialName("licence_received") val licenseReceived: String,
    @SerialName("rating") val rating: Double
)

fun SignInResponseBody.toDomainModelWithToken(token: String) = ProfileDomainModel(
    name = name,
    gender = gender.asGender(),
    city = city,
    driversLicenseId = license,
    rating = rating
).setDateOfBirth(birthDate)
    .setDateOfIssue(licenseReceived)
    .also {
        it.token = token
    }