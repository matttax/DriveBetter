package com.matttax.drivebetter.map.domain

import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint

interface DriveManager {
    fun addRidePoint(ridePoint: RidePoint)
    suspend fun sendBatch(isFinal: Boolean)
    suspend fun findIntermediatePoints(start: GeoPoint, end: GeoPoint): List<List<GeoPoint>>
}
