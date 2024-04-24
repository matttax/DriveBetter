package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ride(
    @SerialName("ride_id")
    val rideId: Int,

    @SerialName("detected_role")
    val detectedRole: String,

    @SerialName("selected_role")
    val selectedRole: String?,

    @SerialName("max_speed")
    val maxSpeed: String,

    @SerialName("min_speed")
    val minSpeed: String,

    @SerialName("speeding")
    val speedingHistory: List<Speeding>,

    @SerialName("dangerous_accelerations")
    val accelerationHistory: List<Acceleration>,

    @SerialName("dangerous_shifts")
    val shiftHistory: List<Shift>,

    @SerialName("light_nighttime")
    val lightNighttimePercentage: Float,

    @SerialName("nighttime")
    val nighttimePercentage: Float,

    @SerialName("weather")
    val weatherHistory: List<Weather>
)
