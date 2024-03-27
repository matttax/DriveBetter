package com.matttax.drivebetter.history.data.model

import com.matttax.drivebetter.history.domain.Ride
import javax.annotation.concurrent.Immutable

@Immutable
data class RideUiModel(
    val id: Int,
    val title: String,
    val location: String,
    val rating: Float? = null
)

fun Ride.toUiModel(): RideUiModel {
    return RideUiModel(
        id = rideId,
        title = "March 20, 2024 16:02",
        location = "Moscow",
        rating = if (detectedRole == "passenger") {
            (3..9).random() + ((1..9).random() / 10f)
        } else null
    )
}
