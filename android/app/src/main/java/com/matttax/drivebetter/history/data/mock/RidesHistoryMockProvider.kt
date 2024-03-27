package com.matttax.drivebetter.history.data.mock

import com.matttax.drivebetter.history.data.model.RideUiModel

object RidesHistoryMockProvider {

    val mockDriversRides = listOf(
        RideUiModel(1, "March 20, 2024 16:02", "Moscow", 7.8f),
        RideUiModel(2, "March 20, 2024 16:34", "Moscow", 10f),
        RideUiModel(3, "March 20, 2024 17:12", "Moscow", 5.3f),
        RideUiModel(4, "March 20, 2024 17:20", "Moscow", 6.3f)
    ).reversed()

    val mockPassengerRides = listOf(
        RideUiModel(1, "March 20, 2024 14:12", "Moscow"),
        RideUiModel(1, "March 20, 2024 16:58", "Moscow")
    )
}