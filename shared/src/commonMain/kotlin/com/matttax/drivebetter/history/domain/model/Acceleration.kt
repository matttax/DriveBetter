package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Acceleration(
    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("acceleration")
    val acceleration: Float,

    @SerialName("address")
    val address: Address
)
