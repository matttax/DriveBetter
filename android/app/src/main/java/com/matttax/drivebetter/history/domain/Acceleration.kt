package com.matttax.drivebetter.history.domain

import com.matttax.drivebetter.network.model.Address

data class Acceleration(
    val timestamp: Long,
    val acceleration: Float,
    val address: Address
)
