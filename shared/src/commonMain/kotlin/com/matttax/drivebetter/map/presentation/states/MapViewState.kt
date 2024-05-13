package com.matttax.drivebetter.map.presentation.states

import com.matttax.drivebetter.map.domain.model.GeoPoint

data class MapViewState(
    val targetPoint: GeoPoint,
    val azimuth: Float,
    val tilt: Float,
    val zoom: Float
)
