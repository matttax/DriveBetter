package com.matttax.drivebetter.profile.data.network.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogInRequestBody(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)
