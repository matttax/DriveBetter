package com.matttax.drivebetter.map.routes.model

data class RouteMetadata(
    val distance: String,
    val time: String,
    val timeWithTraffic: String,
    val railwayCrossingCount: Int,
    val pedestrianCrossingCount: Int,
)
