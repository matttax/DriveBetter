package com.matttax.drivebetter.profile.data.network

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.profile.data.ProfileRemoteDataSource
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import com.matttax.drivebetter.coroutines.provideDispatcher
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.KmLog

class ProfileRemoteDataSourceImpl(
    private val ktorService: KtorService
) : ProfileRemoteDataSource {

    override suspend fun logIn(email: String, password: String): Result<TokenHolder> {
        return withContext(provideDispatcher().io) {
            val response = ktorService.client.post("${KtorService.BASE_URL}api/login") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(LogInRequestBody(email, password)))
            }
            when(response.status) {
                HttpStatusCode.OK -> Result.success(response.body<TokenHolder>())
                else -> Result.failure(response.body<LogInErrorHolder>().toThrowable())
            }
        }
    }

    override suspend fun signUp(profileDomainModel: ProfileDomainModel, password: String): Result<TokenHolder> {
        log.d { "signing in: $profileDomainModel" }
        if (!profileDomainModel.isValid)
            return Result.failure(IncompleteProfileException())
        return withContext(provideDispatcher().io) {
            val registrationResponse = ktorService.client.post("${KtorService.BASE_URL}api/profile") {
                contentType(ContentType.Application.Json)
                setBody(
                    Json.encodeToString(profileDomainModel.toRequestBody(password))
                        .also { bodyJson -> log.d { "sign up request: $bodyJson" } }
                )
            }
            log.d { "sign up response(${registrationResponse.status.value}): ${registrationResponse.body<String>()}" }
            when (registrationResponse.status) {
                HttpStatusCode.Created -> logIn(profileDomainModel.email!!, password)
                else -> Result.failure(
                    registrationResponse.body<RegistrationErrorHolder>().toThrowable(registrationResponse.status.value)
                )
            }
        }
    }

    companion object {
        private val log = KmLog("ProfileRemoteDataSourceImpl")
    }
}

@Serializable data class LogInRequestBody(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)

@Serializable data class SignUpRequestBody(
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

@Serializable data class TokenHolder(
    @SerialName ("token") val token: String
)

@Serializable data class LogInErrorHolder(
    @SerialName ("error") val error: String,
)

@Serializable data class RegistrationErrorHolder(
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

fun LogInErrorHolder.toThrowable(): Throwable {
    return LogInException(error)
}

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

class LogInException(override val message: String?) : RuntimeException()
open class RegistrationException(override val message: String?) : RuntimeException()
class IncompleteProfileException : RegistrationException(message = "Profile is missing important fields")