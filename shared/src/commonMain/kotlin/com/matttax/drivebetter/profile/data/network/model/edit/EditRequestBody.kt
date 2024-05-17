package com.matttax.drivebetter.profile.data.network.model.edit

import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditRequestBody(
    @SerialName("name") val name: String?,
    @SerialName("gender") val gender: String?,
    @SerialName("city") val city: String?,
    @SerialName("birth_date") val birthDate: String?,
    @SerialName("licence") val license: String?,
    @SerialName("licence_received") val licenseReceived: String?,
    @SerialName("email") val email: String?,
)

fun ProfileDomainModel.toEditBody() = EditRequestBody(
    name = name,
    gender = gender?.text?.lowercase(),
    city = city,
    birthDate = dateOfBirthString,
    license = driversLicenseId,
    licenseReceived = dateLicenseIssuedString,
    email = email,
)
