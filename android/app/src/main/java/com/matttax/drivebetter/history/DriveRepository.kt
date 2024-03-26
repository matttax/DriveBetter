package com.matttax.drivebetter.history

import com.matttax.drivebetter.network.ApiHelper
import com.matttax.drivebetter.profile.data.AccountRepository
import com.matttax.drivebetter.speedometer.model.LocationPoint
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

data class Point(
    val latitude: Float,
    val longitude: Float,
    val speed: Float = 0f,
    val timestamp: Long = -1,
    val movementAngle: Float = 0f
)

data class Drive(
    val uuid: String,
    val autoStart: Boolean,
    val autoFinish: Boolean,
    val points: List<Point>
)

fun PathItem.toPoint(): Point {
    return Point(
        latitude = locationPoint.latitude.toFloat(),
        longitude = locationPoint.longitude.toFloat(),
        timestamp = locationPoint.timestamp,
        speed = speed
    )
}