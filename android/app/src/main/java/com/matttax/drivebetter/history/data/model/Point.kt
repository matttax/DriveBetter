package com.matttax.drivebetter.history.data.model

import com.matttax.drivebetter.speedometer.model.PathItem

data class Point(
    val latitude: Float,
    val longitude: Float,
    val speed: Float = 0f,
    val timestamp: Long = -1,
    val movementAngle: Float = 0f
)

fun PathItem.toPoint(): Point {
    return Point(
        latitude = locationPoint.latitude.toFloat(),
        longitude = locationPoint.longitude.toFloat(),
        timestamp = locationPoint.timestamp,
        speed = speed
    )
}
