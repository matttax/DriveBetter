package com.matttax.drivebetter.map.domain.model

import kotlin.math.abs

data class GeoPoint(
    val latitude: Double,
    val longitude: Double
)



fun GeoPoint.isCloseTo(point: GeoPoint?): Boolean {
    if (point == null) return false
    return abs(latitude - point.latitude) < PROXIMITY_DELTA && abs(longitude - point.longitude) < PROXIMITY_DELTA
}

private const val PROXIMITY_DELTA = 0.5
