package com.matttax.drivebetter.profile.domain.model

import com.matttax.drivebetter.profile.data.AccountIdProvider
import com.matttax.drivebetter.profile.domain.util.DateConverter
import com.matttax.drivebetter.profile.domain.util.DateConverter.asString
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlin.math.abs

data class ProfileDomainModel(
    val name: String? = null,
    val gender: Gender? = null,
    val city: String? = null,
    val driversLicenseId: String? = null,
    val rating: Double? = null,
    val email: String? = null,
    val dateOfBirth: LocalDate? = null,
    val dateLicenseIssued: LocalDate? = null,
    val uuid: Long = AccountIdProvider.newId(),
    val avatarByteArray: ByteArray? = null,
) {
    val isValid: Boolean
        get() = name != null
                && dateOfBirth != null
                && gender != null
                && city != null
                && driversLicenseId != null
                && dateLicenseIssued != null
                && email != null

    val dateOfBirthString: String?
        get() = dateOfBirth?.asString()

    val dateLicenseIssuedString: String?
        get() = dateLicenseIssued?.asString()

    val age: Int
        get() {
            val epochDays = (Clock.System.now().epochSeconds / 86400).toInt()
            val gap = dateOfBirth?.minus(LocalDate.fromEpochDays(epochDays))
            return abs(gap?.years ?: 0)
        }

    var token: String? = null

    fun setDateOfBirth(date: String?): ProfileDomainModel {
        if (date == null) return this
        return DateConverter.fromString(date)?.let {
            this.copy(dateOfBirth = it)
        } ?: this
    }

    fun setDateOfIssue(date: String?): ProfileDomainModel {
        if (date == null) return this
        return DateConverter.fromString(date)?.let {
            this.copy(dateLicenseIssued = it)
        } ?: this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ProfileDomainModel

        if (name != other.name) return false
        if (gender != other.gender) return false
        if (city != other.city) return false
        if (driversLicenseId != other.driversLicenseId) return false
        if (rating != other.rating) return false
        if (avatarByteArray != null) {
            if (other.avatarByteArray == null) return false
            if (!avatarByteArray.contentEquals(other.avatarByteArray)) return false
        } else if (other.avatarByteArray != null) return false
        if (email != other.email) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (dateLicenseIssued != other.dateLicenseIssued) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (gender?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (driversLicenseId?.hashCode() ?: 0)
        result = 31 * result + (rating?.hashCode() ?: 0)
        result = 31 * result + (avatarByteArray?.contentHashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (dateOfBirth?.hashCode() ?: 0)
        result = 31 * result + (dateLicenseIssued?.hashCode() ?: 0)
        return result
    }
}
