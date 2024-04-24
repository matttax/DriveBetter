package com.matttax.drivebetter.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Map
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavigationItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    RIDES_HISTORY(
        route = "/rides",
        title = "Rides",
        icon = Icons.Default.History
    ),
    MAP(
        route = "/map",
        title = "Map",
        icon = Icons.Default.Map
    ),
    PROFILE(
        route = "/profile",
        title = "Profile",
        icon = Icons.Default.AccountCircle
    )
}
