package com.matttax.drivebetter.history.presentation.model

data class WeatherInterval(
    val weatherType: WeatherType,
    val startTimestamp: Long,
    val endTimestamp: Long
)
