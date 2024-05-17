package com.matttax.drivebetter.map.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RideDataBatch(
    @SerialName("points") val points: List<RidePointDto>,
    @SerialName("finished") val isFinished: Boolean
)
