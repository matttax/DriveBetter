package com.matttax.drivebetter.history.domain

import com.google.gson.annotations.SerializedName
import com.matttax.drivebetter.network.model.Address

data class Shift(
    @SerializedName("timestamp")
    val timestamp: Long,

    @SerializedName("shift_angle")
    val shiftAngle: Float,

    @SerializedName("rate_of_angle_change")
    val angleChange: Float,

    @SerializedName("address")
    val address: Address
)
