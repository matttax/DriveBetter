package com.matttax.drivebetter.map.location.utils

import com.matttax.drivebetter.map.domain.model.GeoPoint
import kotlin.math.*

object SphericalUtils {

    fun computeHeading(from: GeoPoint, to: GeoPoint): Double {
        val fromLat = from.latitude * DEGREES_TO_RADIANS
        val fromLng = from.longitude * DEGREES_TO_RADIANS
        val toLat = to.latitude * DEGREES_TO_RADIANS
        val toLng = to.longitude * DEGREES_TO_RADIANS
        val dLng = toLng - fromLng
        val heading = atan2(
            sin(dLng) * cos(toLat),
            cos(fromLat) * sin(toLat) - sin(fromLat) * cos(toLat) * cos(dLng)
        )
        return wrap(heading * RADIANS_TO_DEGREES)
    }

    fun computeDistanceBetween(from: GeoPoint, to: GeoPoint): Double {
        return computeAngleBetween(from, to) * EARTH_RADIUS
    }

    private fun computeAngleBetween(from: GeoPoint, to: GeoPoint): Double {
        val fromLat = from.latitude * DEGREES_TO_RADIANS
        val fromLng = from.longitude * DEGREES_TO_RADIANS
        val toLat = to.latitude * DEGREES_TO_RADIANS
        val toLng = to.longitude * DEGREES_TO_RADIANS
        val dLat = fromLat - toLat
        val dLng = fromLng - toLng
        return 2 * asin(
            sqrt(
                sin(dLat / 2).pow(2.0) + cos(fromLat) * cos(toLat) * sin(dLng / 2).pow(2.0)
            )
        )
    }

    private fun wrap(n: Double): Double {
        return if (n >= MIN_ANGLE && n < MAX_ANGLE) n
            else mod(n - MIN_ANGLE) + MIN_ANGLE
    }

    private fun mod(x: Double): Double {
        return (x % ANGLES + ANGLES) % ANGLES
    }

    private const val EARTH_RADIUS = 6371009.0
    private const val MIN_ANGLE = -180.0
    private const val MAX_ANGLE = 180.0
    private const val ANGLES = 360.0

    private const val DEGREES_TO_RADIANS = 0.017453292519943295
    private const val RADIANS_TO_DEGREES = 57.29577951308232
}
