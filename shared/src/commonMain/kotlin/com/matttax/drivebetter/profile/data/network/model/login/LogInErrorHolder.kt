package com.matttax.drivebetter.profile.data.network.model.login

import com.matttax.drivebetter.profile.data.network.LogInException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LogInErrorHolder(
    @SerialName("detail") val error: String,
)

fun LogInErrorHolder.toThrowable(): Throwable {
    return LogInException(error)
}
