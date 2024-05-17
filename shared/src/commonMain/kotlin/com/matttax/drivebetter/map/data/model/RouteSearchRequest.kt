package com.matttax.drivebetter.map.data.model

import com.matttax.drivebetter.map.domain.model.GeoPoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RouteSearchRequest(
    @SerialName("lat_start") val latStart: Double,
    @SerialName("lon_start") val lonStart: Double,
    @SerialName("lat_end") val latEnd: Double,
    @SerialName("lon_end") val lonEnd: Double
) {

    companion object {
        fun build(start: GeoPoint, end: GeoPoint): RouteSearchRequest {
            return RouteSearchRequest(
                latStart = start.latitude,
                lonStart = start.longitude,
                latEnd = end.latitude,
                lonEnd = end.longitude
            )
        }
    }
}
