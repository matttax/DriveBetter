package com.matttax.drivebetter.profile.data.network.model.registration

import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestBody(
    @SerialName("uuid") val uuid: String,
    @SerialName("name") val name: String,
    @SerialName("gender") val gender: String,
    @SerialName("city") val city: String,
    @SerialName("birth_date") val birthDate: String,
    @SerialName("licence") val license: String,
    @SerialName("licence_received") val licenseReceived: String,
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

fun SignUpRequestBody.toDomainModel() = ProfileDomainModel(
    name = name,
    gender = gender.asGender(),
    city = city,
    driversLicenseId = license,
    rating = 0.0
).apply {
    setDateOfBirth(birthDate)
    setDateOfIssue(licenseReceived)
}

fun ProfileDomainModel.toRequestBody(password: String) = SignUpRequestBody(
    uuid = uuid.toString(),
    name = name!!,
    gender = gender!!.text.lowercase(),
    city = city!!,
    birthDate = dateOfBirthString!!,
    license = driversLicenseId!!,
    licenseReceived = dateLicenseIssuedString!!,
    email = email!!,
    password = password
)
