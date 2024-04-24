package com.matttax.drivebetter.navigation

sealed class RideNavigationScreen(val route: String) {
    data object RidesList : RideNavigationScreen("rides_list")
    data object RideScreen : RideNavigationScreen("ride_screen/{id}") {
        fun navigateById(id: Int): String = "ride_screen/$id"
    }
}
