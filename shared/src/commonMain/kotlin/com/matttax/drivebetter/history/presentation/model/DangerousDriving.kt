package com.matttax.drivebetter.history.presentation.model

data class DangerousDriving(
    val event: DangerousDrivingType,
    val address: String,
    val timestamp: Long
)
