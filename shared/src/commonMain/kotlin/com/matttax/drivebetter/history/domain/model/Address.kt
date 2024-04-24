package com.matttax.drivebetter.history.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    @SerialName("short")
    val short: String,

    @SerialName("full")
    val full: String,

    @SerialName("region_type_full")
    val regionTypeFull: String,

    @SerialName("region")
    val regionTypeShort: String
)
