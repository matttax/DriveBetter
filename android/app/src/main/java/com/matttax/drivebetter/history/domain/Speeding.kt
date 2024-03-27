package com.matttax.drivebetter.history.domain

import com.matttax.drivebetter.network.model.Address

data class Speeding(
    val timestamp: Long,
    val speed: Float,
    val address: Address
)
