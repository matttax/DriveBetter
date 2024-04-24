package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("visibility")
    val visibility: Float,

    @SerialName("weather_code")
    val weatherCode: Int,
)
