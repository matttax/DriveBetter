package com.matttax.drivebetter.map.routes.model

data class Route(
    val metadata: RouteMetadata,
    val polyline: Any,
    val speedLimits: List<Float>
)
