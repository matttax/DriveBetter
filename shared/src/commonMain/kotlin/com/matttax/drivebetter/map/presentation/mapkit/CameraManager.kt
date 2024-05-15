package com.matttax.drivebetter.map.presentation.mapkit

import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.presentation.states.MapViewState

expect object CameraManager {
    fun getPosition(ridePoint: RidePoint?, isDriving: Boolean): MapViewState
}

val RUSSIA_CENTER_POINT = GeoPoint(60.0, 60.0)