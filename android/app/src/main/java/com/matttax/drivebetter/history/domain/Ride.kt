package com.matttax.drivebetter.history.domain

import com.google.gson.annotations.SerializedName
import com.matttax.drivebetter.network.model.Address

data class Ride(
    @SerializedName("ride_id")
    val rideId: Int,

    @SerializedName("detected_role")
    val detectedRole: String,

    @SerializedName("selected_role")
    val selectedRole: String,

    @SerializedName("max_speed")
    val maxSpeed: String,

    @SerializedName("min_speed")
    val minSpeed: String,

    @SerializedName("speeding")
    val speedingHistory: List<Speeding>,

    @SerializedName("dangerous_accelerations")
    val accelerationHistory: List<Acceleration>,

    @SerializedName("dangerous_shifts")
    val shiftHistory: List<Shift>,

    @SerializedName("light_nighttime")
    val lightNighttimePercentage: Float,

    @SerializedName("nighttime")
    val nighttimePercentage: Float,

    @SerializedName("weather")
    val weatherHistory: List<Weather>
)
