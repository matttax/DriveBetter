package com.matttax.drivebetter.history.data

import com.matttax.drivebetter.history.data.model.Drive
import com.matttax.drivebetter.history.data.model.toPoint
import com.matttax.drivebetter.history.domain.Ride
import com.matttax.drivebetter.network.ApiHelper
import com.matttax.drivebetter.speedometer.model.PathItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DriveRepository @Inject constructor(
    private val apiHelper: ApiHelper
) {
    private var rideHistory: List<Ride>? = null

    suspend fun addDrive(
        userId: String,
        drivePath: List<PathItem>,
        autoStart: Boolean,
        autoFinish: Boolean
    ): Long {
        apiHelper.sendBatch(
            Drive(
                uuid = userId,
                autoStart = autoStart,
                autoFinish = autoFinish,
                points = drivePath.map { it.toPoint() }
            )
        )
        return 1L
    }

    fun getRideHistoryById(userId: String): Flow<List<Ride>> {
        return apiHelper.getRidesHistory(userId).onEach { rideHistory = it }
    }

    fun getRideById(id: Int): Ride? {
        return rideHistory?.first { id == it.rideId }
    }
}
