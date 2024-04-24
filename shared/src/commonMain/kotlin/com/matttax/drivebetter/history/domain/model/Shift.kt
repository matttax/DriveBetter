package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Shift(
    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("shift_angle")
    val shiftAngle: Float,

    @SerialName("rate_of_angle_change")
    val angleChange: Float,

    @SerialName("address")
    val address: Address
)
