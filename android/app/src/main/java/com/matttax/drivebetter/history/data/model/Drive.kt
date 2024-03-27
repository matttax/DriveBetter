package com.matttax.drivebetter.history.data.model

data class Drive(
    val uuid: String,
    val autoStart: Boolean,
    val autoFinish: Boolean,
    val points: List<Point>
)
