package com.matttax.drivebetter.history.data

import com.matttax.drivebetter.KtorService
import com.matttax.drivebetter.history.domain.RideRepository
import com.matttax.drivebetter.history.domain.model.Ride
import kotlinx.coroutines.withContext
import com.matttax.drivebetter.util.provideDispatcher
import io.ktor.client.call.body
import io.ktor.client.request.get

class RideRepositoryImpl(
    private val ktorService: KtorService
) : RideRepository {

    override suspend fun getRideHistoryById(userId: String): List<Ride> {
        return withContext(provideDispatcher().io) {
            ktorService.client
                .get("${KtorService.BASE_URL}api/ride?uuid=$userId").body()
        }
    }

    override suspend fun getRideById(id: Int): Ride? {
        return null
    }

}
