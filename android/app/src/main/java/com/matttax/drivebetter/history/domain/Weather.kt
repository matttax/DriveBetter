package com.matttax.drivebetter.history.domain

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("visibility")
    val visibility: Float,

    @SerializedName("weather_code")
    val weatherCode: Int,
)
