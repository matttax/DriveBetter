package com.matttax.drivebetter.profile.data.network

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.coroutines.provideDispatcher
import com.matttax.drivebetter.profile.data.ProfileRemoteDataSource
import com.matttax.drivebetter.profile.data.network.model.TokenHolder
import com.matttax.drivebetter.profile.data.network.model.edit.EditResponseBody
import com.matttax.drivebetter.profile.data.network.model.edit.toDomainModel
import com.matttax.drivebetter.profile.data.network.model.edit.toDomainModelWithToken
import com.matttax.drivebetter.profile.data.network.model.edit.toEditBody
import com.matttax.drivebetter.profile.data.network.model.login.LogInErrorHolder
import com.matttax.drivebetter.profile.data.network.model.login.LogInRequestBody
import com.matttax.drivebetter.profile.data.network.model.login.toThrowable
import com.matttax.drivebetter.profile.data.network.model.registration.RegistrationErrorHolder
import com.matttax.drivebetter.profile.data.network.model.registration.toRequestBody
import com.matttax.drivebetter.profile.data.network.model.registration.toThrowable
import com.matttax.drivebetter.profile.domain.model.ProfileDomainModel
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.KmLog
import kotlin.concurrent.Volatile

class ProfileRemoteDataSourceImpl(
    private val ktorService: KtorService
) : ProfileRemoteDataSource {

    @Volatile
    private var cachedToken: String? = null

    override suspend fun logIn(email: String, password: String): Result<ProfileDomainModel> {
        return withContext(provideDispatcher().io) {
            val token = getToken(email, password)?.token ?: return@withContext Result.failure(LogInException("Cannot obtain token"))
            val dataResponse = ktorService.client.put("${KtorService.BASE_URL}api/profile") {
                contentType(ContentType.Application.Json)
                headers {
                    append(AUTH_TOKEN_HEADER, "Token $token")
                }
                setBody("{}")
            }
            when(dataResponse.status) {
                HttpStatusCode.OK -> {
                    val body = dataResponse.body<EditResponseBody>().also {
                        log.d { "sign in response: $it" }
                    }
                    Result.success(body.toDomainModelWithToken(token))
                }
                else -> {
                    val throwable = try {
                        dataResponse.body<LogInErrorHolder>().toThrowable()
                    } catch (ex: Throwable) {
                        ex
                    }
                    Result.failure(throwable)
                }
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
                HttpStatusCode.Created -> {
                    val token = getToken(profileDomainModel.email!!, password)
                    token?.let { Result.success(it) } ?: Result.failure(RegistrationException("Unable to obtain token"))
                }
                else -> Result.failure(
                    registrationResponse.body<RegistrationErrorHolder>().toThrowable(registrationResponse.status.value)
                )
            }
        }
    }

    override suspend fun edit(newProfile: ProfileDomainModel, token: String?): Result<ProfileDomainModel> {
        val localToken = token ?: cachedToken ?: return Result.failure(InvalidTokenException())
        return withContext(provideDispatcher().io) {
            val editResponse = ktorService.client.put("${KtorService.BASE_URL}api/profile") {
                contentType(ContentType.Application.Json)
                headers {
                    append(AUTH_TOKEN_HEADER, "Token $localToken")
                }
                setBody(
                    Json.encodeToString(newProfile.toEditBody())
                        .also { bodyJson -> log.d { "edit request: $bodyJson" } }
                )
            }
            val body = editResponse.body<String>()
            log.d { "edit response(${editResponse.status.value}): $body" }
            when (editResponse.status) {
                HttpStatusCode.OK -> {
                    Result.success(editResponse.body<EditResponseBody>().toDomainModel())
                }
                else -> Result.failure(EditProfileException(body))
            }
        }
    }

    private suspend fun getToken(email: String, password: String): TokenHolder? {
        cachedToken = null
        return withContext(provideDispatcher().io) {
            val authResponse = ktorService.client.post("${KtorService.BASE_URL}api/login") {
                contentType(ContentType.Application.Json)
                setBody(Json.encodeToString(LogInRequestBody(email, password)))
            }
            if (authResponse.status == HttpStatusCode.OK) {
                authResponse.body<TokenHolder?>().also {
                    cachedToken = it?.token
                    log.d { "token body: $it" }
                }
            } else null
        }
    }

    companion object {
        private val log = KmLog("ProfileRemoteDataSourceImpl")
        private const val AUTH_TOKEN_HEADER = "Authorization"
    }
}
