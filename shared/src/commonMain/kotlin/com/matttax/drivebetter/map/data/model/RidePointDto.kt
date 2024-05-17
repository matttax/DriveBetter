package com.matttax.drivebetter.map.data.model

import com.matttax.drivebetter.map.domain.model.RidePoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RidePointDto(
    @SerialName("latitude") val latitude: Double,
    @SerialName("longitude") val longitude: Double,
    @SerialName("speed") val speed: Double,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("movement_angle") val movementAngle: Double
)

fun RidePoint.toDto() = RidePointDto(
    latitude = location.latitude,
    longitude = location.longitude,
    speed = speed.value,
    movementAngle = azimuth.value,
    timestamp = timestamp
)
