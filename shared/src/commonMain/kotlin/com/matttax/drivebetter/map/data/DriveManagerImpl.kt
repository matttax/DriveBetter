package com.matttax.drivebetter.map.data

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.map.data.model.RideDataBatch
import com.matttax.drivebetter.map.data.model.RidePointDto
import com.matttax.drivebetter.map.data.model.toDto
import com.matttax.drivebetter.map.domain.DriveManager
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.profile.data.token.LoginStorage
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.lighthousegames.logging.KmLog

class DriveManagerImpl(
    private val ktorService: KtorService,
    private val loginStorage: LoginStorage
) : DriveManager {

    private val batch = mutableListOf<RidePointDto>()

    override fun addRidePoint(ridePoint: RidePoint) {
        batch.add(ridePoint.toDto())
    }

    override suspend fun sendBatch(isFinal: Boolean) {
        val token = loginStorage.token ?: run {
            log.e { "no token found" }
            return
        }
        try {
            ktorService.client
                .post("${KtorService.BASE_URL}api/location/batch") {
                    headers {
                        append(AUTH_TOKEN_HEADER, "Token $token")
                    }
                    setBody(
                        Json.encodeToString(RideDataBatch(batch, isFinal))
                            .also { bodyJson -> log.d { "batch: $bodyJson" } }
                    )
                }
            batch.clear()
        } catch (ex: Throwable) {
            log.e(ex) { "Unable to send batch" }
        }
    }

    companion object {
        private val log = KmLog("DriveManagerImpl")
        private const val AUTH_TOKEN_HEADER = "Authorization"
    }
}
