package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Speeding(
    @SerialName("timestamp")
    val timestamp: Long,

    @SerialName("speed")
    val speed: Float,

    @SerialName("address")
    val address: Address
)
