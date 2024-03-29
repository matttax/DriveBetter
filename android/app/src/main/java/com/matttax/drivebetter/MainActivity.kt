package com.matttax.drivebetter

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Messenger
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.matttax.drivebetter.drive.presentation.DriveViewModel
import com.matttax.drivebetter.drive.presentation.DrivingCommand
import com.matttax.drivebetter.drive.presentation.componenets.DriveScreen
import com.matttax.drivebetter.history.presentation.screens.HistoryScreen
import com.matttax.drivebetter.history.presentation.RideScreen
import com.matttax.drivebetter.history.presentation.RideViewModel
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.profile.presentation.components.ProfileScreen
import com.matttax.drivebetter.profile.presentation.ProfileViewModel
import com.matttax.drivebetter.speedometer.background.DriveService
import com.matttax.drivebetter.speedometer.background.communication.ActivityMessenger
import com.matttax.drivebetter.speedometer.model.DashboardData
import com.matttax.drivebetter.ui.navigation.BottomNavigationBar
import com.matttax.drivebetter.ui.navigation.BottomNavigationItem
import com.matttax.drivebetter.ui.navigation.RideNavigationScreen
import com.matttax.drivebetter.ui.theme.DriveBetterTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity(), ServiceConnection {

    private val driveViewModel by viewModels<DriveViewModel>()
    private val activityMessenger = ActivityMessenger(::onStatusUpdate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, DriveService::class.java))
        lifecycleScope.launch {
            driveViewModel.drivingCommand.collectLatest {
                when (it) {
                    DrivingCommand.START -> activityMessenger.startDrive()
                    DrivingCommand.PAUSE -> activityMessenger.pauseDrive()
                    DrivingCommand.STOP -> activityMessenger.stopDrive()
                }
            }
        }
        setContent {
            DriveBetterTheme {
                val navController = rememberNavController()
                var barYPosition by remember { mutableStateOf(0) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomNavigationBar(
                            modifier = Modifier.onPlaced { barYPosition = it.size.height },
                            navController = navController
                        )
                    }
                ) {
                    NavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = with(LocalDensity.current) { barYPosition.toDp() }),
                        navController = navController,
                        startDestination = BottomNavigationItem.DASHBOARD.route
                    ) {
                        composable(BottomNavigationItem.DASHBOARD.route) {
                            DriveScreen(driveViewModel)
                        }
                        composable(BottomNavigationItem.PROFILE.route) {
                            val profileViewModel by viewModels<ProfileViewModel>()
                            ProfileScreen(profileViewModel)
                        }
                        navigation(
                            route = BottomNavigationItem.RIDES_HISTORY.route,
                            startDestination = RideNavigationScreen.RidesList.route
                        ) {
                            composable(RideNavigationScreen.RidesList.route) {
                                val historyViewModel = hiltViewModel<RidesHistoryViewModel>()
                                HistoryScreen(historyViewModel) {
                                    navController.navigate(RideNavigationScreen.RideScreen.navigateById(it))
                                }
                            }
                            composable(
                                route = RideNavigationScreen.RideScreen.route,
                                arguments = listOf(
                                    navArgument(RideViewModel.RIDE_ID_KEY) { type = NavType.IntType }
                                )
                            ) {
                                val rideViewModel = hiltViewModel<RideViewModel>()
                                RideScreen(rideViewModel)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        activityMessenger.onDisconnect()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        activityMessenger.onConnect(Messenger(service))
        activityMessenger.handShake()
    }

    override fun onStart() {
        super.onStart()
        bindService(
            Intent(this, DriveService::class.java),
            this,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onStop() {
        super.onStop()
        unbindService(this)
    }

    private fun onStatusUpdate(dashboardData: DashboardData) {
        driveViewModel.updateDashboardData(dashboardData)
    }
}
