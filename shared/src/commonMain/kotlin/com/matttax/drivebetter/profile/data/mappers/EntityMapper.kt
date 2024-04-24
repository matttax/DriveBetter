package com.matttax.drivebetter.profile.data.mappers

import com.matttax.drivebetter.ProfileEntity
import com.matttax.drivebetter.profile.domain.model.Gender.Companion.asGender
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.profile.domain.util.DateConverter

fun ProfileEntity.toDomainModel() = ProfileDomainModel(
    name = name,
    gender = gender.asGender(),
    city = city,
    driversLicenseId = licenseId,
    rating = rating,
    avatarByteArray = avatar,
    email = email,
    dateOfBirth = DateConverter.fromString(dateOfBirth),
    dateLicenseIssued = DateConverter.fromString(dateOfIssue)
)

fun ProfileDomainModel.toDatabaseEntity(): ProfileEntity? {
    if (!isValid) return null
    return ProfileEntity(
        id = -1,
        name = name!!,
        city = city!!,
        gender = gender!!.text,
        email = email!!,
        licenseId = driversLicenseId!!,
        dateOfBirth = dateOfBirthString!!,
        dateOfIssue = dateLicenseIssuedString!!,
        rating = null,
        avatar = null
    )
}
