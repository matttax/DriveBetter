package com.matttax.drivebetter.map.routes.model

data class RouteMetadata(
    val distance: Double,
    val timeSec: Double,
    val timeWithTrafficSec: Double,
    val railwayCrossingCount: Int,
    val pedestrianCrossingCount: Int,
)
