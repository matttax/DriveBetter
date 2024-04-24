import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.matttax.drivebetter.history.presentation.componenets.HistoryScreen
import com.matttax.drivebetter.history.presentation.RidesHistoryViewModel
import com.matttax.drivebetter.map.presentation.MapView
import com.matttax.drivebetter.navigation.BottomNavigationBar
import com.matttax.drivebetter.navigation.BottomNavigationItem
import com.matttax.drivebetter.profile.presentation.ProfileViewModel
import com.matttax.drivebetter.profile.presentation.componenets.ProfileScreen
import com.matttax.drivebetter.ui.theme.AppTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.rememberNavigator
import moe.tlaster.precompose.navigation.transition.NavTransition

@Composable
fun NavigatorApp() {
    PreComposeApp {
        val profileViewModel = koinViewModel(vmClass = ProfileViewModel::class)
        val ridesHistoryViewModel = koinViewModel(vmClass = RidesHistoryViewModel::class)
        AppTheme {
            val navigator = rememberNavigator()
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = { BottomNavigationBar(navigator) }
            ) { padding ->
                val defaultModifier = remember {
                    Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = padding.calculateBottomPadding()
                        )
                }
                NavHost(
                    navigator = navigator,
                    navTransition = NavTransition(),
                    initialRoute = BottomNavigationItem.MAP.route,
                ) {
                    scene(
                        route = BottomNavigationItem.PROFILE.route,
                        navTransition = NavTransition(),
                    ) {
                        ProfileScreen(
                            modifier = defaultModifier,
                            viewModel = profileViewModel
                        )
                    }
                    scene(
                        route = BottomNavigationItem.MAP.route,
                        navTransition = NavTransition(),
                    ) {
                        MapView()
                    }
                    scene(
                        route = BottomNavigationItem.RIDES_HISTORY.route,
                        navTransition = NavTransition(),
                    ) {
                        HistoryScreen(
                            modifier = defaultModifier,
                            viewModel = ridesHistoryViewModel
                        )
                    }
                }
            }
        }
    }
}
