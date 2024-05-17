package com.matttax.drivebetter.map.routes.model

data class Route(
    val metadata: RouteMetadata,
    val polyline: Any,
    val speedLimits: List<Float>
) {
    val safetyIndex = metadata.pedestrianCrossingCount / 10.0 + metadata.railwayCrossingCount / 2.0
}
