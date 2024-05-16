package com.matttax.drivebetter.history.data

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.coroutines.provideDispatcher
import com.matttax.drivebetter.history.domain.RideRepository
import com.matttax.drivebetter.history.domain.model.Ride
import kotlinx.coroutines.withContext
import com.matttax.drivebetter.profile.data.token.LoginStorage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import org.lighthousegames.logging.KmLog

class RideRepositoryImpl(
    private val ktorService: KtorService,
    private val loginStorage: LoginStorage
) : RideRepository {

    override suspend fun getRideHistory(): Result<List<Ride>> {
        val token = loginStorage.token ?: return Result.failure(
            IllegalStateException("Unable to access token").also {
                log.e { "no token found" }
            }
        )
        log.d { "token: $token" }
        return try {
            val response = withContext<List<Ride>?>(provideDispatcher().io) {
                ktorService.client
                    .get("${KtorService.BASE_URL}api/rides") {
                        headers {
                            append(HttpHeaders.Authorization, token)
                        }
                    }.body()
            }
            log.d { "response: $response" }
            if (response != null)
                Result.success(response)
            else Result.success(emptyList())
        } catch (ex: Exception) {
            return Result.failure(ex)
        }
    }

    override suspend fun getRideById(id: Int): Result<Ride?> {
        return Result.failure(RuntimeException("Stub!"))
    }

    companion object {
        private val log = KmLog("RideRepositoryImpl")
    }

}
