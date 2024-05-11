package com.matttax.drivebetter.map.routes

import com.matttax.drivebetter.map.domain.GeoPoint

data class RouteSearchRequest(
    val from: GeoPoint,
    val to: GeoPoint,
    val intermediate: List<GeoPoint> = emptyList()
)
