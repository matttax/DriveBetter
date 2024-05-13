package com.matttax.drivebetter.map.domain.model

import kotlinx.datetime.Clock

data class RidePoint(
    val location: GeoPoint,
    val speed: Speed = Speed(0.0),
    val azimuth: Azimuth = Azimuth(0.0),
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)
