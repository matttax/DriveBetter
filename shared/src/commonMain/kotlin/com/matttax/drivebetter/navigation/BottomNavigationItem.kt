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
        title = "История",
        icon = Icons.Default.History
    ),
    MAP(
        route = "/map",
        title = "Карта",
        icon = Icons.Default.Map
    ),
    PROFILE(
        route = "/profile",
        title = "Профиль",
        icon = Icons.Default.AccountCircle
    )
}
