package com.matttax.drivebetter.history.presentation

import androidx.compose.runtime.Immutable
import com.matttax.drivebetter.history.domain.model.Ride
import com.matttax.drivebetter.ui.utils.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime

@Immutable
data class RideUiModel(
    val id: Int,
    val dateTimeStarted: LocalDateTime,
    val locationStarted: String,
    val rating: Float
)

fun Ride.toUiModel(): RideUiModel {
    return RideUiModel(
        id = rideId,
        dateTimeStarted = DateUtils.timestampToLocalDateTime(speedingHistory.firstOrNull()?.timestamp ?: Clock.System.now().epochSeconds),
        locationStarted = speedingHistory.lastOrNull()?.address?.short ?: "",
        rating = (45..100).random() / 10f
    )
}
