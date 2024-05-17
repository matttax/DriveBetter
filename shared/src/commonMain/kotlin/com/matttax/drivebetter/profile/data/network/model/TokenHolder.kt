package com.matttax.drivebetter.profile.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenHolder(
    @SerialName("token") val token: String
)
