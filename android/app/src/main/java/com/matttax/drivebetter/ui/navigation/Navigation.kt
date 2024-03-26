package com.matttax.drivebetter.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.matttax.drivebetter.ui.utils.ColorUtils
import com.matttax.drivebetter.R
import com.matttax.drivebetter.history.RideScreen

enum class BottomNavigationItem(
    val route: String,
    val title: String,
    val iconId: Int
) {
    RIDES_HISTORY(
        route = "rides",
        title = "Rides",
        iconId = R.drawable.ic_baseline_car_24
    ),
    DASHBOARD(
        route = "dashboard",
        title = "Dashboard",
        iconId = R.drawable.ic_baseline_dashboard_24
    ),
    PROFILE(
        route = "profile",
        title = "Profile",
        iconId = R.drawable.ic_baseline_account_circle_24
    )
}

fun BottomNavigationItem.isSelected(route: String): Boolean {
    return when(this) {
        BottomNavigationItem.RIDES_HISTORY -> {
            println(RideNavigationScreen.RidesList.route)
            println(RideNavigationScreen.RideScreen.route)
            println(route)
            RideNavigationScreen.RidesList.route == route || RideNavigationScreen.RideScreen.route == route
        }
        else -> this.route == route
    }.also { println(it) }
}

sealed class RideNavigationScreen(val route: String) {
    object RidesList : RideNavigationScreen("rides_list")
    object RideScreen : RideNavigationScreen("ride_screen/{id}") {
        fun navigateById(id: Int): String = "ride_screen/$id"
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Card(
        modifier = modifier.padding(horizontal = 7.dp),
        elevation = CardDefaults.cardElevation(15.dp)
    ) {
        NavigationBar(
            containerColor = Color.White
        ) {
            BottomNavigationItem.values().forEach { item ->
                val isSelected = currentRoute?.let { item.isSelected(it) } ?: true
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    },
                    label = { BottomItemText(item.title, isSelected) },
                    alwaysShowLabel = true,
                    icon = { BottomIcon(ImageVector.vectorResource(item.iconId), isSelected) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White
                    )
                )
            }
        }
    }
}

@Composable
fun BottomIcon(iconVector: ImageVector, isSelected: Boolean) {
    Icon(
        imageVector = iconVector,
        tint = ColorUtils.getBottomColor(isSelected),
        contentDescription = null
    )
}

@Composable
fun BottomItemText(title: String, isSelected: Boolean) {
    Text(
        text = title,
        color = ColorUtils.getBottomColor(isSelected),
    )
}
