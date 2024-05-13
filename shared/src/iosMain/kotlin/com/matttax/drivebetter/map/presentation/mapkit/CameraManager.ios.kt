package com.matttax.drivebetter.map.presentation.mapkit

import com.matttax.drivebetter.map.domain.model.GeoPoint
import com.matttax.drivebetter.map.domain.model.RidePoint
import com.matttax.drivebetter.map.presentation.states.MapViewState

actual object CameraManager {
    actual fun getPosition(ridePoint: RidePoint?): MapViewState {
        return MapViewState(GeoPoint(0.0, 0.0), 0f, 0f, 0f)
    }
}
